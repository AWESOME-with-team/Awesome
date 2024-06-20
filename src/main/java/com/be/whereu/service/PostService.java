package com.be.whereu.service;

import com.be.whereu.model.dto.board.*;


import java.util.List;

public interface PostService {
    public PostResponseDto addPost(PostRequestDto dto);
    public PostResponseDto updatePost(PostRequestDto dto);
    public boolean deletePost(Long id);
    public PostResponseDto getPost(Long id);
    public List<BoardDetailsListDto> getBoardDetailsList(Long id, int pageNum);
    public List<BoardListDto> getBoardList(int pageNum);
    public PostResponseDto viewCountPost(Long id);
    public boolean toggleLikePost(Long id);
    public boolean toggleScrapPost(Long id);
    public List<CategoryDto> getCategoryList();
    public List<ScrapAndSaveListDto> getScrapList(int pageNum);
    public List<ScrapAndSaveListDto> getMyPostList(int pageNum);
}
