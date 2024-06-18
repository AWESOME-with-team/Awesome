package com.be.whereu.service;

import com.be.whereu.exception.ResourceNotFoundException;
import com.be.whereu.model.dto.board.CommentRequestDto;
import com.be.whereu.model.dto.board.CommentListResponseDto;
import com.be.whereu.model.dto.board.CommentResponseDto;
import com.be.whereu.model.entity.*;
import com.be.whereu.repository.CommentLikeRepository;
import com.be.whereu.repository.CommentRepository;
import com.be.whereu.repository.MemberRepository;
import com.be.whereu.repository.PostRepository;
import com.be.whereu.security.authentication.SecurityContextManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final SecurityContextManager securityContextManager;

    private final int COMMENT_PAGE_SIZE = 15;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    @Override
    public CommentResponseDto getComment(Long id) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        try{
            Optional<CommentEntity> commentEntity= commentRepository.findById(id);
            if (commentEntity.isPresent()) {
                commentResponseDto = CommentResponseDto.toDto(commentEntity.get());
            }else{
                log.error("no Comment found");
            }
        }catch (DataAccessException e){
            log.error("DataBase access error", e);
        }catch (Exception e){
            log.error("An unexpected error", e);
        }
        return commentResponseDto;
    }
    @Transactional
    @Override
    public List<CommentListResponseDto> getCommentList(Long postId, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, COMMENT_PAGE_SIZE, Sort.by("id").descending());
        try {
            // 최상위 댓글들을 포함한 모든 댓글들을 한 번에 가져오는 쿼리 실행
            List<CommentListResponseDto> allComments = commentRepository.findByPostIdWithLikeCount(postId, pageable);

            // 최상위 댓글들을 추출하여 리스트에 담음
            List<CommentListResponseDto> topLevelComments = new ArrayList<>();
            Map<Long, CommentListResponseDto> commentMap = new HashMap<>();

            for (CommentListResponseDto comment : allComments) {
                commentMap.put(comment.getId(), comment);
                if (comment.getParentId() == null) {
                    topLevelComments.add(comment);
                }
            }

            // 자식 댓글들을 부모 댓글에 연결
            for (CommentListResponseDto comment : allComments) {
                Long parentId = comment.getParentId();
                if (parentId != null) {
                    CommentListResponseDto parentComment = commentMap.get(parentId);
                    if (parentComment != null) {
                        if (parentComment.getChildren() == null) {
                            parentComment.setChildren(new ArrayList<>());
                        }
                        parentComment.getChildren().add(comment);
                    }
                }
            }

            return topLevelComments;
        } catch (DataAccessException e) {
            log.error("DataBase access error", e);
            return Collections.emptyList();  // 빈 리스트 반환
        } catch (Exception e) {
            log.error("An unexpected error", e);
            return Collections.emptyList();
        }
    }

    //댓글 등록
    @Transactional
    @Override
    public CommentListResponseDto addComment(CommentRequestDto commentRequestDto) {
        try{

            MemberEntity member = memberRepository.findById(Long.parseLong(securityContextManager.getAuthenticatedUserName()))
                    .orElseThrow(()-> new ResourceNotFoundException("Member not found"));
            CommentEntity commentEntity= CommentEntity.toEntity(commentRequestDto);
            commentEntity.setMember(member);
            CommentEntity saveComment = commentRepository.save(commentEntity);
            return CommentListResponseDto.toDto(saveComment);
        }catch (DataAccessException e){
            log.error("DataBase access error",e);
            return null;
        }catch (Exception e){
            log.error("An unexpected error",e);
            return null;
        }

    }

    //댓글 수정  content만 변경
    @Transactional
    @Override
    public CommentListResponseDto updateComment(CommentRequestDto commentRequestDto) {

        try{
            CommentEntity commentEntity = commentRepository.findById(commentRequestDto.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

            MemberEntity member = memberRepository.findById(Long.parseLong(securityContextManager.getAuthenticatedUserName()))
                    .orElseThrow(()-> new ResourceNotFoundException("Member not found"));
            commentEntity.setMember(member);
            commentEntity.setContent(commentRequestDto.getContent());

            return CommentListResponseDto.toDto(commentEntity);
        }catch (DataAccessException e){
            log.error("DataBase access error",e);
            return null;
        }catch (Exception e){
            log.error("An unexpected error",e);
            return null;
        }

    }

    //댓글 삭제
    @Transactional
    @Override
    public boolean deleteComment(Long id) {
        try {
            commentRepository.deleteById(id);
            return true;
        } catch (DataAccessException e) {
            log.error("DataBase access error", e);
            return false;
        } catch (Exception e) {
            log.error("An unexpected error", e);
            return false;
        }
    }


    @Transactional
    @Override
    public boolean toggleLikeComment(Long commentId) {
        try{
            Long memberId= Long.parseLong(securityContextManager.getAuthenticatedUserName());
            MemberEntity member = new MemberEntity();
            member.setId(memberId);

            CommentEntity comment = commentRepository.findById(commentId)
                    .orElseThrow(()-> new IllegalArgumentException("not found comment"));
            boolean isLiked = commentLikeRepository.existsByCommentIdAndMemberId(commentId, memberId);
            if (isLiked) {
                commentLikeRepository.deleteByCommentIdAndMemberId(commentId, memberId); //좋아요 취소
            } else {
                CommentLikeEntity commentLike = CommentLikeEntity.toEntity(comment, member);// 좋아요 성공
                commentLikeRepository.save(commentLike);
            }
            return !isLiked; //true이면 좋아요 성공 false이면 좋아요 취소
        } catch (DataAccessException e) {

            throw new RuntimeException("Database access error", e);
        } catch (Exception e) {

            throw new RuntimeException("An unexpected error occurred", e);

        }
    }

}
