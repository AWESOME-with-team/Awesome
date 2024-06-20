package com.be.whereu.service;


import com.be.whereu.exception.ResourceNotFoundException;
import com.be.whereu.model.dto.board.*;
import com.be.whereu.model.entity.*;

import com.be.whereu.repository.*;
import com.be.whereu.security.authentication.SecurityContextManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final SecurityContextManager securityContextManager;
    private final CommonRepository commonRepository;
    private final PostLikeRepository postLikeRepository;



    private static final int POST_PAGE_SIZE = 15;
    private static final int BOARD_PAGE_SIZE = 10;
    private final CommentRepository commentRepository;


    @Transactional
    public boolean toggleLikePost(Long postId) {
        try {
            Long memberId= Long.parseLong(securityContextManager.getAuthenticatedUserName());
            MemberEntity member = new MemberEntity();
            member.setId(memberId);

            PostEntity post = postRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("Not Found Post"));


            boolean isLiked = postLikeRepository.existsByPostIdAndMemberId(postId, memberId);

            if (isLiked) {
                postLikeRepository.deleteByPostIdAndMemberId(postId, memberId); //좋아요 취소

            } else {
                PostLikeEntity postLike = PostLikeEntity.toEntity(post, member);// 좋아요 성공

                postLikeRepository.save(postLike);
                postRepository.save(post);
            }

            return !isLiked; //true이면 좋아요 성공 false이면 좋아요 취소
        } catch (DataAccessException e) {

            throw new RuntimeException("Database access error", e);
        } catch (Exception e) {

            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    @Override
    public List<CategoryDto> getCategoryList() {
        List<CommonEntity> CategoryList =commonRepository.findByParentCodeId(1000L);
        List<CategoryDto> CategoryListDto = CategoryList.stream()
                .map(entity->{
                    return CategoryDto.builder()
                            .categoryId(entity.getCodeId())
                            .boardName(entity.getCodeName()).build();
                })

                .collect(Collectors.toList());


        return CategoryListDto;
    }

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
            Optional<PostEntity> postEntity= postRepository.findById(id);
            System.out.println();
            if (postEntity.isPresent()) {
                Integer likeCount=postLikeRepository.countLikesByPostId(id);
                postResponseDto = PostResponseDto.toDtoWithLikes(postEntity.get(),likeCount);
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

        Long memberId=Long.parseLong(securityContextManager.getAuthenticatedUserName());
        Pageable pageable = PageRequest.of(pageNumber, POST_PAGE_SIZE, Sort.by("id").descending());
        Page<BoardDetailsListDto> boardDetailsListDto = new PageImpl<BoardDetailsListDto>(new ArrayList<BoardDetailsListDto>());
        try {
            boardDetailsListDto=postRepository.findByCommonIdOrderByIdDescWithCommentCount(id,memberId,pageable);
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
            Page<BoardListDto> boardListDto=postRepository.findByParentIdByWithLastPostTitle(pageable);
            return boardListDto.stream()
                    .map(dto->{
                        if(dto.getLastPostTitle()==null){
                            dto.setLastPostTitle("아직 게시글이 없어요! 먼저 입력해 봅시다!!");
                        }
                        return dto;
                    })
                    .collect(Collectors.toList()); // .Collect(..)변경가능한 List  , toList()변경불가능List
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
