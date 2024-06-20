package com.be.whereu.controller;

import com.be.whereu.model.dto.board.CommentRequestDto;
import com.be.whereu.model.dto.board.CommentListResponseDto;
import com.be.whereu.model.dto.board.CommentResponseDto;
import com.be.whereu.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;


    /**
     * 댓글 가져오기
     * @param id
     * @return
     */
    @GetMapping("/comment")
    public ResponseEntity<CommentResponseDto> getComment(@RequestParam("commentId") Long id) {
        try{
            CommentResponseDto commentResponseDto = commentService.getComment(id);
            return ResponseEntity.ok(commentResponseDto);
        } catch (Exception e){
            //INTERNAL_SERVER_ERROR
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }


    /**
     * 댓글 리스트 가져오기
     * @param id
     * @param pageNum
     * @return
     */
    @GetMapping("/comment/list")
    public ResponseEntity<List<CommentListResponseDto>> commentList(@RequestParam("postId") Long id, @RequestParam(value = "pageNum", defaultValue = "0") int pageNum) {
        try{
            List<CommentListResponseDto> commentList = commentService.getCommentList(id, pageNum);
            return  ResponseEntity.ok(commentList);
        } catch (Exception e) {
            //INTERNAL_SERVER_ERROR
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 댓글 ,대댓글 등록 ( postId, content, parentId(대댓글인 경우만) 필요)
     * @param commentRequestDto
     * @return
     */
    @PostMapping("/comment")
    public ResponseEntity<CommentListResponseDto> addComment(@RequestBody CommentRequestDto commentRequestDto) {
        try {
            CommentListResponseDto dto = commentService.addComment(commentRequestDto);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            //INTERNAL_SERVER_ERROR
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 댓글 수정( Id와 content 필요)
     * @param commentRequestDto
     * @return
     */
    @PatchMapping("/comment")
    public ResponseEntity<CommentListResponseDto> updateComment(@RequestBody CommentRequestDto commentRequestDto) {
        try {
            CommentListResponseDto dto = commentService.updateComment(commentRequestDto);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            //INTERNAL_SERVER_ERROR
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 댓글 삭제
     * @param id
     * @return
     */
    @DeleteMapping("/comment")
    public ResponseEntity<Boolean> deleteComment(@RequestParam("commentId") Long id) {
        try{
            boolean isSuccess = commentService.deleteComment(id);
            return ResponseEntity.ok(isSuccess);
        }catch (Exception e) {
            //INTERNAL_SERVER_ERROR
            e.printStackTrace();
            return ResponseEntity.status(500).body(false);
        }

    }

    /**
     * 댓글 좋아요 +
     * @param id
     * @return
     */
    @PostMapping("/like")
    public ResponseEntity<Boolean> commentLike(@RequestParam("commentId") Long id) {
        try {
            boolean likeStatus=commentService.toggleLikeComment(id);
            return ResponseEntity.ok(likeStatus);
        } catch (Exception e) {
            //INTERNAL_SERVER_ERROR
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }


}
