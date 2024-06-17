package com.be.whereu.service;


import com.be.whereu.exception.ResourceNotFoundException;
import com.be.whereu.model.dto.board.BoardDetailsListDto;
import com.be.whereu.model.dto.board.BoardListDto;
import com.be.whereu.model.dto.board.PostRequestDto;
import com.be.whereu.model.dto.board.PostResponseDto;
import com.be.whereu.model.entity.CommonEntity;
import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.model.entity.PostEntity;

import com.be.whereu.repository.CommentRepository;
import com.be.whereu.repository.CommonRepository;
import com.be.whereu.repository.MemberRepository;
import com.be.whereu.repository.PostRepository;
import com.be.whereu.security.authentication.SecurityContextManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final SecurityContextManager securityContextManager;
    private final CommonRepository commonRepository;


    private static final int POST_PAGE_SIZE = 15;
    private static final int BOARD_PAGE_SIZE = 5;
    private final CommentRepository commentRepository;

    // 게시물 등록
    @Transactional
    @Override
    public PostResponseDto addPost(PostRequestDto dto) {
        try {
            // PostEntity로 변환
            PostEntity post = PostEntity.ToPostEntity(dto);

            // 사용자 정보 조회
            Optional<MemberEntity> member = memberRepository.findById(Long.parseLong(securityContextManager.getAuthenticatedUserName()));
            if (member.isPresent()) {
                MemberEntity memberEntity = member.get();
                post.setMember(memberEntity);
                log.info("Insert post: {}", post);
            } else {
                log.error("no member found");
                throw new IllegalArgumentException("Member not found");  // 예외를 던져 트랜잭션 롤백
            }
            PostEntity postEntity=postRepository.save(post);

            return PostResponseDto.toDto(postEntity);
        } catch (DataAccessException e) {
            log.error("Database access error", e);
            throw e;  // 예외를 다시 던져 트랜잭션 롤백
        } catch (Exception e) {
            log.error("An unexpected error", e);
            throw e;  // 예외를 다시 던져 트랜잭션 롤백
        }
    }

    //게시물 수정
    @Transactional
    @Override
    public PostResponseDto updatePost(PostRequestDto dto) {
        try {
            MemberEntity memberEntity = memberRepository.findById(Long.parseLong(securityContextManager.getAuthenticatedUserName()))
                    .orElseThrow(()-> new ResourceNotFoundException("Post not found"));
            PostEntity postEntity = postRepository.findById(dto.getId()).orElseThrow(()-> new ResourceNotFoundException("Post not found"));
            postEntity.setMember(memberEntity);
            postEntity.setContent(dto.getContent());
            postEntity.setTitle(dto.getTitle());
            PostEntity updatePost=postRepository.save(postEntity);

            return PostResponseDto.toDto(updatePost);

        } catch (DataAccessException e) {
            log.error("DataBase access error", e);
            return null;
        } catch (Exception e) {
            log.error("An unexpected error", e);
            return null;
        }
    }
    //게시물 삭제
    @Override
    public boolean deletePost(Long id) {
        try {
            postRepository.deleteById(id);
            return true;
        } catch (DataAccessException e) {
            log.error("DataBase access error", e);
            return false;
        } catch (Exception e) {
            log.error("An unexpected error", e);
            return false;
        }
    }


    //게시물 가져오기
    @Transactional
    @Override
    public PostResponseDto getPost(Long id) {
        PostResponseDto postResponseDto = new PostResponseDto();
        try {
            Optional<PostEntity> PostEntity= postRepository.findById(id);
            System.out.println();
            if (PostEntity.isPresent()) {
                postResponseDto = PostResponseDto.toDto(PostEntity.get());
            }else{

                log.error("no Post found");

            }
        }catch (DataAccessException e){
            log.error("DataBase access error", e);
        }catch (Exception e){
            log.error("An unexpected error", e);
        }


        return postResponseDto;
    }

    //게시물 리스트 불러오기
    @Transactional
    @Override
    public List<BoardDetailsListDto> getBoardDetailsList(Long id, int pageNumber) {

        Pageable pageable = PageRequest.of(pageNumber, POST_PAGE_SIZE, Sort.by("id").descending());
        Page<BoardDetailsListDto> boardDetailsListDto = new PageImpl<BoardDetailsListDto>(new ArrayList<BoardDetailsListDto>());
        try {
            boardDetailsListDto=postRepository.findByCommonIdOrderByIdDescWithCommentCount(id,pageable);
        } catch (DataAccessException e) {
            log.error("DataBase access error", e);
        } catch (Exception e) {
            log.error("An unexpected error", e);
        }

        return boardDetailsListDto.stream().toList();
    }
    //게시판 List 리스트 불러오기
    @Transactional
    @Override
    public List<BoardListDto> getBoardList(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, BOARD_PAGE_SIZE, Sort.by("id").descending());
        try {
            List<BoardListDto> result = new ArrayList<>();
            for (long i = 1001L; i <= 1010L; i++) {
                CommonEntity commonEntity = commonRepository.findById(i)
                        .orElseThrow(() -> new IllegalArgumentException("Not Found CommonData"));
                List<BoardListDto> list = postRepository.findByCommonOrderByIdDesc(commonEntity,pageable).stream()
                        .map(BoardListDto::fromEntity)
                        .toList();
                if (list.isEmpty()) {
                    BoardListDto boardListDto = new BoardListDto();
                    boardListDto.setId(commonEntity.getCodeId());
                    boardListDto.setTitle(commonEntity.getCodeName());
                    result.add(boardListDto);

                }else{
                    result.add(list.get(0));
                }
            }

            return result;

        }catch (DataAccessException e){
            log.error("DataBase access error",e);
            return Collections.emptyList();  //빈리스트 반환
        }catch (Exception e){
            log.error("An unexpected error",e);
            return Collections.emptyList();
        }





    }
    @Transactional
    @Override
    public PostResponseDto likePost(Long id) {
        try{

            PostEntity postEntity = postRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Not Found Post"));
            postEntity.setLikeCount(postEntity.getLikeCount()+1);
            return PostResponseDto.toDto(postEntity);
        }catch (DataAccessException e){
            log.error("DataBase access error",e);
            return null;  //빈리스트 반환
        }catch (Exception e){
            log.error("An unexpected error",e);
            return null;
        }

    }
    @Transactional
    @Override
    public PostResponseDto unlikePost(Long id) {
        try{
            PostEntity postEntity = postRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Not Found Post"));
            postEntity.setLikeCount(postEntity.getLikeCount()-1);
            return PostResponseDto.toDto(postEntity);
        }catch (DataAccessException e){
            log.error("DataBase access error",e);
            return null;  //빈리스트 반환
        }catch (Exception e){
            log.error("An unexpected error",e);
            return null;
        }

    }

    @Transactional
    @Override
    public PostResponseDto viewCountPost(Long id) {
        try{
            PostEntity postEntity = postRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Not Found Post"));
            postEntity.setViewCount(postEntity.getViewCount()+1);
            return PostResponseDto.toDto(postEntity);
        }catch (DataAccessException e){
            log.error("DataBase access error",e);
            return (PostResponseDto) Collections.emptyList();  //빈리스트 반환
        }catch (Exception e){
            log.error("An unexpected error",e);
            return (PostResponseDto)Collections.emptyList();
        }

    }


}
