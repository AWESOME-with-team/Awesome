package com.be.whereu.controller;


import com.be.whereu.model.dto.board.CommentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {



    //대댓글 등록하기

    //페이지 네이션


    //댓글 수정

    //댓글 삭제

    //댓글 리스트 가져오기

    @GetMapping("/list")
    public ResponseEntity<List<CommentDto>> getComments() {



        return null;
    }
}
