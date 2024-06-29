package com.be.whereu.service;

import com.be.whereu.model.dto.ComplaintCommentRequestDto;
import com.be.whereu.model.dto.ComplaintCommentResponseDto;
import com.be.whereu.model.dto.ComplaintPostRequestDto;
import com.be.whereu.model.dto.ComplaintPostResponseDto;

import java.util.List;

public interface ComplaintService {
    ComplaintPostResponseDto complaintPost(ComplaintPostRequestDto requestDto);
    ComplaintCommentResponseDto complaintComment(ComplaintCommentRequestDto requestDto);
    List<ComplaintPostResponseDto> getAllComplaintPosts(int pageNum);
    List<ComplaintCommentResponseDto> getAllComplaintComments(int pageNum);

}
