package com.be.whereu.controller;



import com.be.whereu.model.dto.board.*;
import com.be.whereu.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
@Slf4j
public class PostController {
    private final PostService postService;


    /**
     * 내가 쓴 게시글 list 가져오기
     * @param pageNum
     * @return
     */
    @GetMapping("/post/list/me")
    public ResponseEntity<List<ScrapAndSaveListDto>> getMyPostList(@RequestParam int pageNum){
        try{
            return ResponseEntity.ok(postService.getMyPostList(pageNum));
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * 스크랩 List 가져오기
     * @param pageNum
     * @return
     */
    @GetMapping("/scrap/list")
    public ResponseEntity<List<ScrapAndSaveListDto>> getScrapList(@RequestParam int pageNum) {
        try{
            return ResponseEntity.ok(postService.getScrapList(pageNum));
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 카테고리 list 가져오기
     * @return
     */
    @GetMapping("/category")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        try{
            return ResponseEntity.ok(postService.getCategoryList());
        }catch (Exception e) {
            //INTERNAL_SERVER_ERROR
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 게시글 등록 ( commonId, title, content 필요)
     * @param postRequestDto
     * @return
     */
    @PostMapping("/post")
    public ResponseEntity<PostResponseDto> addPost(@RequestBody PostRequestDto postRequestDto){
        try {
            PostResponseDto postResponseDto= postService.addPost(postRequestDto);

            return ResponseEntity.ok(postResponseDto);
        }catch (Exception e) {
            //INTERNAL_SERVER_ERROR
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }



    }

    /**
     * 게시글 수정(id, title, content 필요 -형님 죄송합니다.. 3개만 필요해요)
     * @param postRequestDto
     * @return
     */
    @PatchMapping("/post")
    public ResponseEntity<PostResponseDto> putPost(@RequestBody PostRequestDto postRequestDto){

        try {
            PostResponseDto postResponseDto= postService.updatePost(postRequestDto);

            return ResponseEntity.ok(postResponseDto);
        }catch (Exception e) {
            //INTERNAL_SERVER_ERROR
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }


    }

    /**
     * 게시글 삭제
     * @param id
     * @return
     */
    @DeleteMapping("/post")
    public ResponseEntity<Boolean> deletePost(@RequestParam("postId") Long id){

        try{
            boolean isSuccess= postService.deletePost(id);

            if(!isSuccess){
                return ResponseEntity.badRequest().build();
            }else{
                return ResponseEntity.ok(true);
            }
        }catch (Exception e) {
            //INTERNAL_SERVER_ERROR
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }

    }


    /**
     * 게시글 가져오기
     * @param id
     * @return
     */
    @GetMapping("/post")
    public ResponseEntity<PostResponseDto> getPost(@RequestParam("postId") Long id){
        try {
            PostResponseDto postResponseDto = postService.getPost(id);
            return ResponseEntity.ok(postResponseDto);
        } catch (Exception e) {
            //INTERNAL_SERVER_ERROR
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }



    }


    /**
     * 게시물 list 가져오기
     * @param id
     * @param pageNum
     * @return
     */
    @GetMapping("/post/list")
    public ResponseEntity<List<BoardDetailsListDto>> getPostList(@RequestParam("commonId") Long id,  @RequestParam(value = "pageNum", defaultValue = "0") int pageNum) {
        try {
            List<BoardDetailsListDto> boardDetailsListDto = postService.getBoardDetailsList(id , pageNum);
            return ResponseEntity.ok(boardDetailsListDto);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(Collections.emptyList());
        }
    }

    /**
     * 게시판 List
     * @return List<boardListDto>
     */
    @GetMapping("/list")
    public ResponseEntity<List<BoardListDto>> boardList(@RequestParam(value = "pageNum", defaultValue = "0") int pageNum){
        try {
            List<BoardListDto> boardListDtos = postService.getBoardList(pageNum);

            return ResponseEntity.ok(boardListDtos);
        } catch (Exception e) {
            // INTERNAL_SERVER_ERROR
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    /**
     * 스크랩 toggle
     * @param id
     * @return
     */
    @PostMapping("/post/scrap")
    public ResponseEntity<Boolean> postScrap(@RequestParam("postId") Long id){
        try {
            boolean scrapStatus= postService.toggleScrapPost(id);
            return ResponseEntity.ok(scrapStatus);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 좋아요 toggle
     * @param id
     * @return
     */
    @PostMapping("/post/like")
    public ResponseEntity<Boolean> postLike(@RequestParam("postId") Long id){
        try {
            boolean likeStatus=postService.toggleLikePost(id);
            return ResponseEntity.ok(likeStatus);
        }catch (Exception e){
            //INTERNAL_SERVER_ERROR
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }




}
