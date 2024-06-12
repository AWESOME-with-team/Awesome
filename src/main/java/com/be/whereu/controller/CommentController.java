package com.be.whereu.controller;

import com.be.whereu.model.dto.board.CommentDto;
import com.be.whereu.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    //댓글 리스트
    @GetMapping("/list")
    public ResponseEntity<List<CommentDto>> commentList(@RequestParam Long postId) {
        List<CommentDto> commentList = commentService.getCommentList(postId);
        return  ResponseEntity.ok(commentList);
    }

    //댓글 추가
    @PostMapping("/add")
    public ResponseEntity<CommentDto> addComment(@RequestBody CommentDto commentDto) {
        CommentDto dto = commentService.addComment(commentDto);
        return  ResponseEntity.ok(dto);
    }

    //댓글 수정
    @PutMapping("/update")
    public ResponseEntity<CommentDto> updateComment(@RequestBody CommentDto commentDto) {
        CommentDto dto = commentService.updateComment(commentDto);
        return  ResponseEntity.ok(dto);
    }

    //댓글 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommentDto> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return  ResponseEntity.noContent().build();
    }

    //댓글 좋아요
    @PostMapping("/like/{id}")
    public ResponseEntity<CommentDto> likeComment(@PathVariable Long id) {
        CommentDto dto = commentService.likeComment(id);
        return  ResponseEntity.ok(dto);
    }

    //댓글 좋아요 취소
    @PostMapping("/unlike/{id}")
    public ResponseEntity<CommentDto> unlikeComment(@PathVariable Long id) {
        CommentDto dto = commentService.unlikeComment(id);
        return  ResponseEntity.ok(dto);

    }
}
