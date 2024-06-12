package com.be.whereu.service;

import com.be.whereu.model.dto.board.BoardListDto;
import com.be.whereu.model.dto.board.PostRequestDto;
import com.be.whereu.model.dto.board.PostResponseDto;


import java.util.List;

public interface PostService {
    public PostResponseDto addPost(PostRequestDto dto);
    public PostResponseDto updatePost(PostRequestDto dto);
    public boolean deletePost(Long id);
    public PostResponseDto getPost(Long id);
    public List<PostResponseDto> getPostList(Long id, int pageNumber);
    public List<BoardListDto> getBoardList(int pageNumber);
    public PostResponseDto likePost(Long id);
    public PostResponseDto unlikePost(Long id);
    public PostResponseDto viewCountPost(Long id);
}