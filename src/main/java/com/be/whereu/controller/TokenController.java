package com.be.whereu.controller;

import com.be.whereu.config.properties.TokenPropertiesConfig;
import com.be.whereu.model.dto.TokenRequest;
import com.be.whereu.service.token.JwtService;
import com.be.whereu.service.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TokenController {
    private static final Logger log = LoggerFactory.getLogger(TokenController.class);
    private final JwtService jwtService;
    private final TokenPropertiesConfig tokenPropertiesConfig;
    private final TokenService tokenService;


    /**
     * 
     * @return access 가 유효검사 필터에서 통과시 200번 응답 대학 email 없을시 201번응답
     */
    @GetMapping("/access")
    public ResponseEntity<String> myMember(@RequestHeader("access-token") String accessJws){
        log.info("accessJws: {}", accessJws);
        log.info("access 200 response ");
        return ResponseEntity.status(HttpStatus.OK).body("valid accessToken");
    }

    /**
     * accessToken refreshToken으로 재발급
     *
     * @param tokenRequest
     * @return
     */
    @PostMapping("/refresh")
    public ResponseEntity<String> refreshAccessToken(@RequestBody TokenRequest tokenRequest) {
        String accessToken = jwtService.createAccessTokenFromRefreshToken(tokenRequest.getRefreshJws());
        log.info("refreshJws:{}", tokenRequest.getRefreshJws());
        if (accessToken == null) {
            log.debug("access token from refresh is null 401 response");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("refresh access 200 response ");
        ResponseCookie AccessTokenCookie = ResponseCookie
                .from("access-token", accessToken)
                .httpOnly(true) //자바스크립트 접근 금지
                .path("/")
                .maxAge(tokenPropertiesConfig.getAccessToken().getExpiration())
                .build();

        return ResponseEntity.ok().
                header(HttpHeaders.SET_COOKIE, AccessTokenCookie.toString())
                .body(accessToken);
    }


}
