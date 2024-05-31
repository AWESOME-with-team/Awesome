package com.be.whereu.model;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Slf4j
public class WhereUJwt {

    private String issuer;
    private String subject;
    private Set<String> audience;
    private Date expirationTime;
    private Date notBefore;
    private Date issuedAt;
    private String jwtId;
    private Boolean isEmailExist;
    private Boolean isExpired;

    // 서명 키
    private String jwtSecret;

    public static WhereUJwt fromJwt(String jws, String jwtSecret) {
        var hmacKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        log.info("jws secret: {}", jwtSecret);
        JwtParser parser =  Jwts.parser().verifyWith(hmacKey).build();
        Claims claims;

        try {
            log.info("WhereUJwt");
            log.debug("debug test");
            claims=parser.parseSignedClaims(jws).getPayload();
            log.info("claims: {}", claims);
        }catch (ExpiredJwtException e){
            log.debug("Fail to get jwt because ExpiredJwtExecption :{} ",e.getMessage());
            return WhereUJwt.builder().isExpired(true).build();
        }catch (Exception e){
            log.debug("Fail to get jwt payload :{} ",e.getMessage());
            return null;
        }



        return WhereUJwt.builder()
                .isEmailExist(claims.get("isEmailExist", Boolean.class))
                .issuer(claims.getIssuer())
                .subject(claims.getSubject())
                .audience(claims.getAudience())
                .expirationTime(claims.getExpiration())
                .notBefore(claims.getNotBefore())
                .issuedAt(claims.getIssuedAt())
                .jwtId(claims.getId())
                .isExpired(false)
                .build();

    }


 }
