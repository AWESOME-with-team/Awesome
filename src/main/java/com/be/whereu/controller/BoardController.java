package com.be.whereu.controller;



import com.be.whereu.model.dto.board.BoardListDto;
import com.be.whereu.model.dto.board.PostDto;
import com.be.whereu.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
@Slf4j
public class BoardController {
    private final BoardService boardService;

    //게시글 등록
    @PostMapping("/post")
    public ResponseEntity<Boolean> addPost( @RequestBody PostDto postDto){

        boolean isSuccess=boardService.insertPost(postDto);

        if(!isSuccess){
            return ResponseEntity.badRequest().build();
        }else{
            return ResponseEntity.ok(true);

        }

    }

    //게시글 수정
    @PutMapping("/post")
    public ResponseEntity<Boolean> putPost(@RequestBody PostDto postDto){
        boolean isSuccess=boardService.updatePost(postDto);

        if(!isSuccess){
            return ResponseEntity.badRequest().build();
        }else{
            return ResponseEntity.ok(true);

        }
    }


    //게시글 삭제
    @DeleteMapping("/post")
    public ResponseEntity<Boolean> deletePost(@RequestBody PostDto postDto){
        System.out.println("test1");
        boolean isSuccess=boardService.deletePost(postDto);
        System.out.println("test2");
        if(!isSuccess){
            return ResponseEntity.badRequest().build();
        }else{
            return ResponseEntity.ok(true);
        }

    }





    //게시물 가져오기
    @GetMapping("/post")
    public ResponseEntity<PostDto> getPost(PostDto dto){
        try {
            PostDto postDto=boardService.getPost(dto);
            return ResponseEntity.ok(postDto);
        } catch (Exception e) {
            //INTERNAL_SERVER_ERROR
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }



    }


    //게시물 리스트 가져오기
    @GetMapping("/post/list")
    public ResponseEntity<List<PostDto>> getPostList(PostDto dto, int pageNumber) {
        try {
            List<PostDto> postDtoList = boardService.getPostList(dto ,pageNumber);
            return ResponseEntity.ok(postDtoList);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 게시판 List
     * @return List<boardListDto>
     */
    @GetMapping("/list")
    public ResponseEntity<List<BoardListDto>> boardList(int pageNumber){
        try {

            List<BoardListDto> boardListDtos = boardService.getBoardList(pageNumber);

            return ResponseEntity.ok(boardListDtos);
        } catch (Exception e) {
            //INTERNAL_SERVER_ERROR
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }




}
