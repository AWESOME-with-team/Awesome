package com.be.whereu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")

public class LoginController {

    @GetMapping("/login")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("go to WebView");
    }

    @GetMapping("/login/success")
    public  ResponseEntity<String> loginSuccess(){
        return ResponseEntity.status(HttpStatus.CREATED).body("login success");

    }

    @GetMapping("/login/fail")
    public  ResponseEntity<String>  loginFail(){

        return ResponseEntity.status(HttpStatus. UNAUTHORIZED).body("login fail");

    }
}
