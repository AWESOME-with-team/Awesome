package com.be.whereu.model;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class WhereUJwt {
    private String issuer;
    private String subject;
    private Set<String> audience;
    private Date expirationTime;
    private Date notBefore;
    private Date issuedAt;
    private String jwtId;

    // 서명 키
    private String jwtSecret;

    public static WhereUJwt fromJwt(String jws, String jwtSecret) {
        var hmacKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        JwtParser parser =  Jwts.parser().verifyWith(hmacKey).build();
        Claims claims;

        try {
            claims=parser.parseClaimsJws(jws).getPayload();
        }catch (Exception e){
            System.out.println("jwt 파싱오류 : " + e.getMessage() );
            return null;
        }
        System.out.println(claims.getIssuer());
        System.out.println(claims.getSubject());
        System.out.println(claims.getAudience());
        System.out.println(claims.getExpiration());
        System.out.println(claims.getNotBefore());
        System.out.println(claims.getIssuedAt());




        return WhereUJwt.builder()
                .issuer(claims.getIssuer())
                .subject(claims.getSubject())
                .audience(claims.getAudience())
                .expirationTime(claims.getExpiration())
                .notBefore(claims.getNotBefore())
                .issuedAt(claims.getIssuedAt())
                .jwtId(claims.getId())
                .build();

    }

    @Override
    public String toString() {
        var hmacKey = jwtSecret == null ? null : Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        var builder = Jwts.builder();
        if (audience != null) {
            for (var aud : audience) {
                builder.audience().add(aud);
            }
        }
        return builder.issuer(issuer)
                .subject(subject)
                .expiration(expirationTime)
                .notBefore(notBefore)
                .issuedAt(issuedAt)
                .id(jwtId)
                .signWith(hmacKey)
                .compact();
    }

    public boolean isExpired(){
        if(expirationTime==null){
            return false;
        }
        return expirationTime.before(new Date());
    }

 }
