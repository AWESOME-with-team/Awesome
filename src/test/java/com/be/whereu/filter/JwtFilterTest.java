package com.be.whereu.filter;
import com.be.whereu.model.WhereUJwt;
import com.be.whereu.service.token.TokenService;
import com.be.whereu.service.token.TokenValidatorService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("Filter 에서 jwt 다양한 상태에 따라서 응답코드 테스트")
public class JwtFilterTest {


    @Autowired
    JwtFilter jwtFilter;

    @MockBean
    TokenValidatorService validatorService;

    @MockBean
    private TokenService tokenService;

    @Test
    public void 스웨거_경로_요청시_필터_적용되지_않는다() throws Exception {
        // Given
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        var filterChain = mock(FilterChain.class);

        //When
        request.setRequestURI("/swagger-ui/index.html");

        //then
        jwtFilter.doFilter(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void 토큰이_없는_경우_필터_작동_확인() throws ServletException, IOException {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        // when
        request.setAttribute("accessJws", null);
        request.setAttribute("refreshJws", null);
        doAnswer(invocation -> {
            HttpServletResponse resp = invocation.getArgument(2);
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return true;
        }).when(validatorService).isNotExistToken(null, null, response);

        //then
        jwtFilter.doFilter(request, response, filterChain);
        assertThat(response.getStatus()).isEqualTo(404);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    public void 유효하지_않은_액세스_토큰인_경우_401_응답코드가_반환된다() throws ServletException, IOException {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        // When
        request.setRequestURI("/test");
        request.addHeader("access-token", "invalidAccessToken");
        when(validatorService.isNotExistToken(anyString(), anyString(), any())).thenReturn(false);
        when(tokenService.validateAccessTokenAndToMakeObjectJwt("invalidAccessToken")).thenReturn(null);
        doAnswer(invocation -> {
            HttpServletResponse resp = invocation.getArgument(1); // 1번 인덱스는 response
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return true;
        }).when(validatorService).isInvalidAccessToken(any(), any(HttpServletResponse.class));

        // Then
        jwtFilter.doFilter(request, response, filterChain);
        assertThat(response.getStatus()).isEqualTo(401);
        verify(filterChain, never()).doFilter(any(), any());
    }

    @Test
    public void 필수_이메일이_존재하지_않으면_201_응답코드가_반환된다() throws ServletException, IOException {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        // When
        request.setRequestURI("/test");
        request.addHeader("access-token", "invalidAccessToken");
        when(validatorService.isNotExistToken(anyString(), anyString(), any())).thenReturn(false);
        when(tokenService.validateAccessTokenAndToMakeObjectJwt("invalidAccessToken")).thenReturn(null);
        doAnswer(invocation -> {
            HttpServletResponse resp = invocation.getArgument(1); // 1번 인덱스는 response
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }).when(validatorService).isInvalidAccessToken(any(), any(HttpServletResponse.class));
        doAnswer(invocation -> {
            HttpServletResponse resp = invocation.getArgument(1); // 1번 인덱스는 response
            resp.setStatus(HttpServletResponse.SC_CREATED); // 201 상태 코드 설정
            return true;
        }).when(validatorService).isNotTokenExistUniEmail(anyString(), any(HttpServletResponse.class));

        // Then
        jwtFilter.doFilter(request, response, filterChain);

        assertThat(response.getStatus()).isEqualTo(201);
        verify(filterChain, never()).doFilter(any(), any());
    }
}

