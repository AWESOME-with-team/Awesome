package com.be.whereu.controller;


import com.be.whereu.model.dto.MemberDto;
import com.be.whereu.repository.MemberRepository;
import com.be.whereu.service.MemberSerivce;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Optional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.io.IOException;
import java.util.Optional;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberSerivce memberSerivce;
    private final MemberRepository memberRepository;

    /*public ResponseEntity<MemberDto> member(@RequestBody MemberDto memberDto) {
        return ResponseEntity.status(HttpStatus.OK).body(memberDto);
    }*/

    @PostMapping("/member")
    public String addMember(@RequestBody MemberDto memberDto, HttpServletResponse response) throws IOException {
        memberSerivce.update(memberDto,response);
        return "ok";
    }


}
