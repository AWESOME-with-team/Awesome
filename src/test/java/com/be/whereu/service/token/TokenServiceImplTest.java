package com.be.whereu.service.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TokenServiceImplTest {

    @MockBean
    private JwtService jwtService;

    @Autowired
    private TokenService tokenService;

    @BeforeEach
    public void setUp() {
        when(jwtService.createAccessTokenFromMemberId(anyLong(), anyBoolean())).thenReturn("accessJws");
        when(jwtService.createRefreshTokenFromMemberId(anyLong())).thenReturn("refreshJws");
    }

    @Test
    void 토큰이_생성_헤더에_추가_되는지_테스트() {
        // given
        Long memberId = 1L;
        boolean universityEmail = true;
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        tokenService.createTokenAndAddCookie(memberId, response, universityEmail);

        // then
        assertThat(response.getHeader("access-token")).isEqualTo("accessJws");
        assertThat(response.getHeader("refresh-token")).isEqualTo("refreshJws");
    }
}
