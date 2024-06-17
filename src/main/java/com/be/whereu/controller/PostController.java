package com.be.whereu.controller;



import com.be.whereu.model.dto.board.BoardDetailsListDto;
import com.be.whereu.model.dto.board.BoardListDto;
import com.be.whereu.model.dto.board.PostRequestDto;
import com.be.whereu.model.dto.board.PostResponseDto;
import com.be.whereu.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
     * 게시글 등록 ( id 제외하고 입력)
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
            return ResponseEntity.status(500).body(false);
        }

    }


    /**
     * 게시물 가져오기
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
            return ResponseEntity.status(500).body(null);
        }



    }


    /**
     * 게시물 리스트 가져오기
     * @param id
     * @param pageNum
     * @return
     */
    @GetMapping("/post/list")
    public ResponseEntity<List<BoardDetailsListDto>> getPostList(@RequestParam("commonId") Long id, @RequestParam("pageNum") int pageNum) {
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
    public ResponseEntity<List<BoardListDto>> boardList(@RequestParam("pageNum") int pageNum){
        try {

            List<BoardListDto> boardListDtos = postService.getBoardList(pageNum);

            return ResponseEntity.ok(boardListDtos);
        } catch (Exception e) {
            //INTERNAL_SERVER_ERROR
            e.printStackTrace();
            return ResponseEntity.status(500).body(Collections.emptyList());
        }
    }

    /**
     * 좋아요 +
     * @param id
     * @return
     */
    @PostMapping("/post/like")
    public ResponseEntity<PostResponseDto> likePost(@RequestParam("postId") Long id){
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
    @PostMapping("/post/unlike")
    public ResponseEntity<PostResponseDto> unLikePost(@RequestParam("postId") Long id){
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
    @PostMapping("/post/view-count")
    public ResponseEntity<PostResponseDto> viewPostCount(@RequestParam("postId") Long id){
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
