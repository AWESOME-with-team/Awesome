package com.be.whereu.service.token;

import com.be.whereu.config.properties.TokenPropertiesConfig;
import com.be.whereu.model.WhereUJwt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TokenServiceImplTest {

    @MockBean
    private JwtService jwtService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    TokenPropertiesConfig tokenPropertiesConfig;

    @BeforeEach
    public void setUp() {
        when(jwtService.createAccessTokenFromMemberId(anyLong(), eq(true))).thenReturn("trueAccessJws");
        when(jwtService.createAccessTokenFromMemberId(anyLong(), eq(false))).thenReturn("falseAccessJws");
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
        assertThat(response.getHeader("access-token")).isEqualTo("trueAccessJws");
        assertThat(response.getHeader("refresh-token")).isEqualTo("refreshJws");
    }

    @Test
    void 토큰이_이메일_필수_정보가_인증된_회원인지_테스트() {
        //given
        long memberId = 1L;
        boolean falseUniversityEmail = false;
        boolean trueUniversityEmail = true;

        var trueJwt=WhereUJwt.builder().isEmailExist(true).build();
        try (MockedStatic<WhereUJwt> mockedStatic = mockStatic(WhereUJwt.class)) {
            // Mocking WhereUJwt static method
            mockedStatic.when(() -> WhereUJwt.fromJwt(eq("trueAccessJws"), anyString()))
                    .thenReturn(trueJwt);;

            //when
            String falseResultJws = jwtService.createAccessTokenFromMemberId(memberId, falseUniversityEmail);
            String trueResultJws = jwtService.createAccessTokenFromMemberId(memberId, trueUniversityEmail);

            //then
            assertThat(tokenService.isUniEmailExistFromToken(falseResultJws)).isEqualTo(false);
            assertThat(tokenService.isUniEmailExistFromToken(trueResultJws)).isEqualTo(true);
        }
    }
}
