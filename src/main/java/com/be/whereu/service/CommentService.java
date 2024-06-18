package com.be.whereu.service;

import com.be.whereu.model.dto.board.CommentRequestDto;
import com.be.whereu.model.dto.board.CommentListResponseDto;
import com.be.whereu.model.dto.board.CommentResponseDto;

import java.util.List;

public interface CommentService {
    public CommentResponseDto getComment(Long id);
    public List<CommentListResponseDto> getCommentList(Long id, int pageNumber);
    public CommentListResponseDto addComment(CommentRequestDto commentRequestDto);
    public CommentListResponseDto updateComment(CommentRequestDto commentRequestDto);
    public boolean deleteComment(Long id);
    public boolean toggleLikeComment(Long id);
}
