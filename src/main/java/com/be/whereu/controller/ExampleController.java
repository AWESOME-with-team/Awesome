package com.be.whereu.controller;

import com.be.whereu.model.dto.ExampleDto;

import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.service.ExampleService;
import com.be.whereu.service.JwtService;
import com.univcert.api.UnivCert;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
//생성자주입
@RequiredArgsConstructor
public class ExampleController {
    // 상수로 정의하면 만들어질떄 autowired 보다 시점이 빠르게만들어저서 안정성이 높아짐
    private final ExampleService service;
    private final JwtService jwtService;

    public ResponseEntity<ExampleDto> example2(@RequestBody ExampleDto dto){

        return ResponseEntity.status(HttpStatus.OK)
                .body(dto);
    }

    @GetMapping("/test")
    public String test(){

        return "ok";

    }
    @GetMapping("/input")
    public String input(){
        return "ok";
    }


    @PostMapping("/member2")
    public String member(@RequestBody MemberEntity member){

        return "ok";
    }

    @PostMapping("/mailtest")
        public String mailTest(){
        try {
            UnivCert.certify("437be0d6-c070-408c-90e1-e9a6c7577233",
                    "201979006@sj.sangji.ac.kr", "상지대학교", true);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "ok";
    }
}
