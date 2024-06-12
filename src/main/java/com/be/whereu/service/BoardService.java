package com.be.whereu.service;

import com.be.whereu.model.dto.board.BoardListDto;
import com.be.whereu.model.dto.board.PostDto;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface BoardService {
    public boolean insertPost(PostDto dto);
    public boolean updatePost(PostDto dto);
    public boolean deletePost(PostDto dto);
    public PostDto getPost(PostDto dto);
    public List<PostDto> getPostList(PostDto dto, int pageNumber);
    public List<BoardListDto> getBoardList(int pageNumber);
}
