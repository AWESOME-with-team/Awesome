package com.be.whereu.service;

import com.be.whereu.model.dto.board.CommentRequestDto;
import com.be.whereu.model.dto.board.CommentResponseDto;

import java.util.List;

public interface CommentService {
    public CommentResponseDto getComment(Long id);
    public List<CommentResponseDto> getCommentList(Long id, int pageNumber);
    public CommentResponseDto addComment(CommentRequestDto commentRequestDto);
    public CommentResponseDto updateComment(CommentRequestDto commentRequestDto);
    public boolean deleteComment(Long id);
    public boolean toggleLikeComment(Long id);
}
