package com.be.whereu.controller;

import com.be.whereu.service.UserService;
import com.be.whereu.service.token.TokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginController {

    private final TokenService tokenService;
    private final UserService userService;

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

    /**
     * logout
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout(){
        boolean isSuccess= tokenService.deleteRefreshTokenWithContextHolderFromMemberId();
        if(isSuccess){
            return ResponseEntity.status(HttpStatus.OK).body(isSuccess);
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(isSuccess);
        }

    }


    @PostMapping("/login/kakao")
    public  ResponseEntity<String> loginKakao(@RequestBody Map<String,String> email, HttpServletResponse response){
        
        userService.checkAndJoinUser(email.get("email"), response);
        return ResponseEntity.status(HttpStatus.OK).body("login kakao");
    }
}

