package com.be.whereu.service;

import com.be.whereu.model.dto.board.CommentDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> getCommentList(Long postId);
    CommentDto addComment(CommentDto commentDto);
    CommentDto updateComment(CommentDto commentDto);
    void deleteComment(Long id);
    CommentDto likeComment(Long id);
    CommentDto unlikeComment(Long id);
}
