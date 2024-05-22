package com.be.whereu.service;



import com.be.whereu.config.properties.TokenPropertiesConfig;
import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.model.entity.RefreshTokenEntity;
import com.be.whereu.repository.MemberRepository;
import com.be.whereu.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService { //jwt를 사용해서 jwt 생성하고 유효한 토큰인지 검증하는 클래스

    private final TokenPropertiesConfig tokenPropertiesConfig;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    private String registerSecret;
    private long registerExpiration;
    private String accessSecret;
    private long accessExpiration;



    @PostConstruct
    public void init(){
        registerSecret = tokenPropertiesConfig.getRegisterToken().getSecret();
        registerExpiration = tokenPropertiesConfig.getRegisterToken().getExpiration();
        accessSecret = tokenPropertiesConfig.getAccessToken().getSecret();
        accessExpiration = tokenPropertiesConfig.getAccessToken().getExpiration();
    }


    public String createRegisterToken(Long memberId){
        SecretKey secretKey=Keys.hmacShaKeyFor(registerSecret.getBytes(StandardCharsets.UTF_8));
        long currentMs=System.currentTimeMillis();
        return Jwts.builder()
                .subject(memberId.toString())
                .expiration(new Date(currentMs+1000*registerExpiration))
                .issuedAt(new Date(currentMs))
                .signWith(secretKey)
                .compact();

    }


    public String createAccessTokenFromMemberId(Long memberId , boolean universityEmail){
        SecretKey secretKey=Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
        long currentMs=System.currentTimeMillis();
        return Jwts.builder()
                .subject(memberId.toString())
                .claim("isEmailExist", universityEmail)
                .expiration(new Date(currentMs+1000*accessExpiration))
                .signWith(secretKey)
                .issuedAt(new Date(currentMs))
                .compact();
    }

    /**
     * email 로 refreshToken 삭제  logout 시에 삭제
     * @param refreshJws
     * @return
     */
    public boolean deleteRefreshTokenByemail(String refreshJws){
        boolean isSuccess=false;

        int result = refreshTokenRepository.deleteByToken(refreshJws);

        if(result>0){
            isSuccess=true;
        }

        return isSuccess;

    }


    /**
     * refreshToken으로 AccessToken 생성
     * @param refreshJws
     * @return
     */
    @Transactional
    public String createAccessTokenFromRefreshToken(String refreshJws){
        RefreshTokenEntity refreshTokenEntity= refreshTokenRepository.findByTokenWithMember(refreshJws);
        if(refreshTokenEntity==null || refreshTokenEntity.isExpired()){

            if (refreshTokenEntity != null) {
                log.debug("refresh token expired");
                refreshTokenRepository.deleteByToken(refreshJws);
                log.info("refresh token delete");
            }
            log.debug("refresh token is null");
            return null;
        }
        boolean isEmailExist = refreshTokenEntity.getMember().getUniversityEmail() != null;
        return createAccessTokenFromMemberId(refreshTokenEntity.getId(),isEmailExist);
    }

    /**
     * member id로 refresh token 생성
     * @param memberId
     * @return
     */

    @Transactional
    public String createRefreshTokenFromMemberId(Long memberId){
        Optional<MemberEntity> optionalEntity=memberRepository.findById(memberId);
        MemberEntity memberentity = optionalEntity.get();

        RefreshTokenEntity entity= new RefreshTokenEntity();
        entity.setId(memberId);
        entity.setId(memberentity.getId());
        entity.setToken(UUID.randomUUID().toString());
        entity.setExpire_date(LocalDateTime.now().plusSeconds(tokenPropertiesConfig.getRefreshToken().getExpiration()));


        RefreshTokenEntity existingToken = refreshTokenRepository.findByMemberId(memberentity.getId());
        if(existingToken!=null){

            //refreshTokenRepository.save(entity); //  update
            existingToken.setToken(entity.getToken());
            existingToken.setExpire_date(entity.getExpire_date());


        }else{
            refreshTokenRepository.save(entity); // 존재하지 않으면 insert

        }

        return entity.getToken();
    }
 }
