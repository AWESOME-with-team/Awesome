package com.be.whereu.service;

import com.be.whereu.exception.ResourceNotFoundException;
import com.be.whereu.model.dto.board.CommentRequestDto;
import com.be.whereu.model.dto.board.CommentResponseDto;
import com.be.whereu.model.dto.board.PostResponseDto;
import com.be.whereu.model.entity.CommentEntity;
import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.model.entity.PostEntity;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final SecurityContextManager securityContextManager;

    private final int COMMENT_PAGE_SIZE = 15;

    @Transactional
    @Override
    public CommentResponseDto getComment(Long id) {
        CommentResponseDto commentResponseDto= new CommentResponseDto();
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

    //댓글 리스트 불러오기
    @Transactional
    @Override
    public List<CommentResponseDto> getCommentList(Long postId,int pageNumber) {

        Pageable pageable = PageRequest.of(pageNumber, COMMENT_PAGE_SIZE, Sort.by("id").descending());
        try{
            List<CommentEntity> commentList = commentRepository.findByPostId(postId,pageable);
            log.info("commentList: {}", commentList);

            return commentList.stream().map(CommentResponseDto::toDto).collect(Collectors.toList());
        }catch (DataAccessException e){
            log.error("DataBase access error",e);
            return Collections.emptyList();  //빈리스트 반환
        }catch (Exception e){
            log.error("An unexpected error",e);
            return Collections.emptyList();
        }
    }

    //댓글 등록
    @Transactional
    @Override
    public CommentResponseDto addComment(CommentRequestDto commentRequestDto) {
        try{

            MemberEntity member = memberRepository.findById(Long.parseLong(securityContextManager.getAuthenticatedUserName()))
                    .orElseThrow(()-> new ResourceNotFoundException("Member not found"));
            CommentEntity commentEntity= CommentEntity.toEntity(commentRequestDto);
            commentEntity.setMember(member);
            CommentEntity saveComment = commentRepository.save(commentEntity);
            return CommentResponseDto.toDto(saveComment);
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
    public CommentResponseDto updateComment(CommentRequestDto commentRequestDto) {

        try{
            CommentEntity commentEntity = commentRepository.findById(commentRequestDto.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

            MemberEntity member = memberRepository.findById(Long.parseLong(securityContextManager.getAuthenticatedUserName()))
                    .orElseThrow(()-> new ResourceNotFoundException("Member not found"));
            commentEntity.setMember(member);
            commentEntity.setContent(commentRequestDto.getContent());

            return CommentResponseDto.toDto(commentEntity);
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

    @Override
    @Transactional
    public CommentResponseDto likeComment(Long id) {
        try{
            CommentEntity commentEntity = commentRepository.findById(id)
                    .orElseThrow(()-> new ResourceNotFoundException("not found comment ID"));
            commentEntity.setLikeCount(commentEntity.getLikeCount() + 1);
            return CommentResponseDto.toDto(commentEntity);
        }catch (DataAccessException e){
            log.error("DataBase access error",e);
            return null;
        }catch (Exception e){
            log.error("An unexpected error",e);
            return null;
        }
    }

    @Override
    @Transactional
    public CommentResponseDto unlikeComment(Long id) {
        try{
            CommentEntity commentEntity = commentRepository.findById(id)
                    .orElseThrow(()-> new ResourceNotFoundException("not found comment ID"));
            commentEntity.setLikeCount(commentEntity.getLikeCount() - 1);
            return CommentResponseDto.toDto(commentEntity);
        }catch (DataAccessException e){
            log.error("DataBase access error",e);
            return null;
        }catch (Exception e){
            log.error("An unexpected error",e);
            return null;
        }
    }

}
