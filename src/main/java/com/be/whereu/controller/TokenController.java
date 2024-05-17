package com.be.whereu.controller;

import com.be.whereu.config.properties.TokenPropertiesConfig;
import com.be.whereu.model.WhereUJwt;
import com.be.whereu.model.dto.MemberDto;
import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.model.entity.RefreshTokenEntity;
import com.be.whereu.repository.RefreshTokenRepository;
import com.be.whereu.service.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
@RequestMapping(value = "/api")
public class TokenController {
    private final JwtService jwtService;
    private final TokenPropertiesConfig tokenPropertiesConfig;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * access token 검증
     *
     * @param accessTokenJws
     * @return
     */

    @PostMapping("/access-validation")
    public ResponseEntity<Void> accessTokenValidation(String accessTokenJws) {
        WhereUJwt accessToken = WhereUJwt.fromJwt(accessTokenJws, tokenPropertiesConfig.getAccessToken().getSecret());
        if (accessToken != null && !accessToken.isExpired()) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * refresh-token 검증
     *
     * @param refreshTokenJws
     * @return
     */
    @PostMapping("/refresh-validation")
    public ResponseEntity<Void> refreshTokenValidation(String refreshTokenJws) {
        RefreshTokenEntity refreshToken = refreshTokenRepository.findByToken(refreshTokenJws);
        if (refreshToken != null) {
//            boolean isExpired = refreshToken.getExpire_date().isBefore(LocalDateTime.now());
//            if (isExpired) {
//                int isDelete = refreshTokenRepository.removeByToken(refreshTokenJws);
//                if (isDelete > 0) {
//                    return ResponseEntity.status(HttpStatus.OK).build();
//                } else {
//                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//                }
//            } else {
//                 refreshAccessToken(refreshTokenJws);
//            }

            System.out.println("이건 되나연");
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            System.out.println("실패");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    /**
     * accessToken refreshToken으로 재발급
     *
     * @param refreshToken
     * @return
     */
    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshAccessToken(String refreshToken) {
        String accessToken = jwtService.createAccessTokenFromRefreshToken(refreshToken);
        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ResponseCookie AccessTokenCookie = ResponseCookie
                .from("access", accessToken)
                .httpOnly(true) //자바스크립트 접근 금지
                .path("/")
                .maxAge(tokenPropertiesConfig.getAccessToken().getExpiration())
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, AccessTokenCookie.toString()).build();
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
