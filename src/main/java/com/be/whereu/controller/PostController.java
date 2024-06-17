package com.be.whereu.controller;



import com.be.whereu.model.dto.board.BoardListDto;
import com.be.whereu.model.dto.board.PostRequestDto;
import com.be.whereu.model.dto.board.PostResponseDto;
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
     * 게시글 등록 ( 등록시 id 제외가능)
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
            return ResponseEntity.status(500).body(null);
        }



    }

    /**
     * 게시글 수정
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
            return ResponseEntity.status(500).body(null);
        }


    }

    /**
     * 게시글 삭제
     * @param id
     * @return
     */
    @DeleteMapping("/post{id}")
    public ResponseEntity<Boolean> deletePost(@PathVariable Long id){

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
            return ResponseEntity.status(500).body(false);
        }

    }


    /**
     * 게시물 가져오기
     * @param id
     * @return
     */
    @GetMapping("/post/{id}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable("id") Long id){
        try {
            PostResponseDto postResponseDto = postService.getPost(id);
            return ResponseEntity.ok(postResponseDto);
        } catch (Exception e) {
            //INTERNAL_SERVER_ERROR
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }



    }


    /**
     * 게시물 리스트 가져오기
     * @param id
     * @param pageNumber
     * @return
     */
    @GetMapping("/post/list/{id}")
    public ResponseEntity<List<PostResponseDto>> getPostList(@PathVariable("id") Long id, int pageNumber) {
        try {
            List<PostResponseDto> postResponseDtoList = postService.getPostList(id ,pageNumber);
            return ResponseEntity.ok(postResponseDtoList);
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
    public ResponseEntity<List<BoardListDto>> boardList(
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber) {
        try {
            List<BoardListDto> boardListDtos = postService.getBoardList(pageNumber);
            return ResponseEntity.ok(boardListDtos);
        } catch (Exception e) {
            // INTERNAL_SERVER_ERROR
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }


    /**
     * 좋아요 +
     * @param id
     * @return
     */
    @PostMapping("/post/like/{id}")
    public ResponseEntity<PostResponseDto> likePost(@PathVariable("id") Long id){
        try {
            PostResponseDto postResponseDto = postService.likePost(id);
            return ResponseEntity.ok(postResponseDto);
        } catch (Exception e) {
            //INTERNAL_SERVER_ERROR
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }

    }

    /**
     * 좋아요 -
     * @param id
     * @return
     */
    @PostMapping("/post/unlike/{id}")
    public ResponseEntity<PostResponseDto> unLikePost(@PathVariable("id") Long id){
        try {
            PostResponseDto postResponseDto = postService.unlikePost(id);
            return ResponseEntity.ok(postResponseDto);
        } catch (Exception e) {
            //INTERNAL_SERVER_ERROR
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }

    }

    /**
     * 조회수 +
     * @param id
     * @return
     */
    @PostMapping("/post/view-count/{id}")
    public ResponseEntity<PostResponseDto> viewPostCount(@PathVariable("id") Long id){
        try {
            PostResponseDto postResponseDto = postService.viewCountPost(id);
            return ResponseEntity.ok(postResponseDto);
        } catch (Exception e) {
            //INTERNAL_SERVER_ERROR
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }




}
