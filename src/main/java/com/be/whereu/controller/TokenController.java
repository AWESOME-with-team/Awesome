package com.be.whereu.controller;

import com.be.whereu.config.properties.TokenPropertiesConfig;
import com.be.whereu.model.WhereUJwt;
import com.be.whereu.model.dto.MemberDto;
import com.be.whereu.model.dto.TokenRequest;
import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.model.entity.RefreshTokenEntity;
import com.be.whereu.repository.RefreshTokenRepository;
import com.be.whereu.service.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TokenController {
    private static final Logger log = LoggerFactory.getLogger(TokenController.class);
    private final JwtService jwtService;
    private final TokenPropertiesConfig tokenPropertiesConfig;
    private final RefreshTokenRepository refreshTokenRepository;


    /**
     * 
     * @return access 가 유효검사 필터에서 통과시 200번 응답
     */
    @GetMapping("/access")
    public ResponseEntity<String> myMember(){
        log.info("access 200 response ");
        return ResponseEntity.status(HttpStatus.OK).body("vaild accessToken");
    }

    /**
     * accessToken refreshToken으로 재발급
     *
     * @param tokenRequest
     * @return
     */
    @PostMapping("/refresh")
    public ResponseEntity<String> refreshAccessToken(@RequestBody TokenRequest tokenRequest) {
        log.info("refresh access 200 response ");
        String accessToken = jwtService.createAccessTokenFromRefreshToken(tokenRequest.getRefreshJws());
        log.info("refreshJws:{}", tokenRequest.getRefreshJws());
        if (accessToken == null) {
            log.debug("access token from refresh is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

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

    /**
     * 로그아웃시  refreshToken 삭제
     *
     * @param email
     * @return
     */
    @Transactional
    @DeleteMapping("/refresh")
    public ResponseEntity<Void> deleteRefreshToken(@CookieValue("refresh") String email) {

        boolean isDeleted = jwtService.deleteRefreshTokenByemail(email);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
