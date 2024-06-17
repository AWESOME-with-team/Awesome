package com.be.whereu.service;

import com.be.whereu.config.properties.TokenPropertiesConfig;
import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.model.entity.RefreshTokenEntity;
import com.be.whereu.model.property.TokenProperty;
import com.be.whereu.repository.MemberRepository;
import com.be.whereu.repository.RefreshTokenRepository;
import com.be.whereu.service.token.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class JwtServiceTest {
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private TokenPropertiesConfig tokenPropertiesConfig;
    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        tokenPropertiesConfig = new TokenPropertiesConfig();

        TokenProperty registerToken = new TokenProperty();
        registerToken.setSecret("registerSecretKey-dsfksfkldsflksdflkldfgljd");
        registerToken.setExpiration(3600);

        TokenProperty accessToken = new TokenProperty();
        accessToken.setSecret("accessSecretKey-sadkasjdladlasfdglkdfjglkdflggfhgfhfghfghgfhfg");
        accessToken.setExpiration(3600);

        tokenPropertiesConfig.setRegisterToken(registerToken);
        tokenPropertiesConfig.setAccessToken(accessToken);

        jwtService = new JwtService(tokenPropertiesConfig, refreshTokenRepository, memberRepository);
        jwtService.init();
    }


    @Test
    void 토큰이_만료된_경우_엑세스_토큰_생성_실패() {
        // Given
        String refreshJws = "someExpiredToken";
        RefreshTokenEntity refreshTokenEntity = mock(RefreshTokenEntity.class);
        when(refreshTokenRepository.findByTokenWithMember(anyString())).thenReturn(refreshTokenEntity);
        when(refreshTokenEntity.isExpired()).thenReturn(true);

        // When
        String result = jwtService.createAccessTokenFromRefreshToken(refreshJws);

        // Then
        assertThat(result).isNull();
        verify(refreshTokenRepository).deleteByToken(refreshJws);
    }

    @Test
    void 유효한_토큰인_경우_엑세스_토큰_생성_성공() {
        // Given
        String refreshJws = "validToken";
        RefreshTokenEntity refreshTokenEntity = mock(RefreshTokenEntity.class);
        MemberEntity memberEntity = mock(MemberEntity.class);
        when(refreshTokenRepository.findByTokenWithMember(anyString())).thenReturn(refreshTokenEntity);
        when(refreshTokenEntity.isExpired()).thenReturn(false);
        when(refreshTokenEntity.getMember()).thenReturn(memberEntity);
        when(memberEntity.getUniversityEmail()).thenReturn("test@university.com");

        // When
        String result = jwtService.createAccessTokenFromRefreshToken(refreshJws);

        // Then
        assertThat(result).isNotNull();

        // 검증
        SecretKey secretKey = Keys.hmacShaKeyFor("accessSecretKey-sadkasjdladlasfdglkdfjglkdflggfhgfhfghfghgfhfg".getBytes(StandardCharsets.UTF_8));
        Claims claims =  Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(result).getPayload();
        assertThat(claims.getSubject()).isEqualTo("0"); // expected member ID
        assertThat(claims.get("isEmailExist", Boolean.class)).isTrue();
    }

    @Test
    void 토큰이_유효하지_않은_경우_엑세스_토큰_생성_실패() {
        // Given
        String refreshJws = "someInvalidToken";
        when(refreshTokenRepository.findByTokenWithMember(anyString())).thenReturn(null);

        // When
        String result = jwtService.createAccessTokenFromRefreshToken(refreshJws);

        // Then
        assertThat(result).isNull();
        verify(refreshTokenRepository, never()).deleteByToken(anyString());
    }
}