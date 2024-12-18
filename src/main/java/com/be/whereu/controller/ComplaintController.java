package com.be.whereu.controller;

import com.be.whereu.model.dto.ComplaintCommentRequestDto;
import com.be.whereu.model.dto.ComplaintCommentResponseDto;
import com.be.whereu.model.dto.ComplaintPostRequestDto;
import com.be.whereu.model.dto.ComplaintPostResponseDto;
import com.be.whereu.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/complaints")
public class ComplaintController {
    private final ComplaintService complaintService;

    /**
     * 게시글 신고
     * @param requestDto
     * @return
     */
    @PostMapping("/post/{postId}")
    public ResponseEntity<ComplaintPostResponseDto> complaintPost(@RequestBody ComplaintPostRequestDto requestDto) {
        try{
            ComplaintPostResponseDto responseDto = complaintService.complaintPost(requestDto);

            return ResponseEntity.ok(responseDto);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 댓글 신고
     * @param requestDto
     * @return
     */
    @PostMapping("/comment/{commentId}")
    public ResponseEntity<ComplaintCommentResponseDto> complaintComment(@RequestBody ComplaintCommentRequestDto requestDto) {
        try{
            ComplaintCommentResponseDto responseDto = complaintService.complaintComment(requestDto);
            return ResponseEntity.ok(responseDto);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 게시글 신고 목록 리스트
     * @return
     */
    @GetMapping("/posts")
    public ResponseEntity<List<ComplaintPostResponseDto>> getAllComplaintPosts(@RequestParam(value = "pageNum", defaultValue = "0") int pageNum) {
        try{
            List<ComplaintPostResponseDto> complaintPosts = complaintService.getAllComplaintPosts(pageNum);
            return ResponseEntity.ok(complaintPosts);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(Collections.emptyList());
        }

    }

    /**
     * 댓글 신고 목록 리스트
     * @return
     */
    @GetMapping("/comments")
    public ResponseEntity<List<ComplaintCommentResponseDto>> getAllComplaintComments(@RequestParam(value = "pageNum", defaultValue = "0") int pageNum) {
        try{
            List<ComplaintCommentResponseDto> complaintComments = complaintService.getAllComplaintComments(pageNum);
            return ResponseEntity.ok(complaintComments);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(Collections.emptyList());
        }
    }

}
