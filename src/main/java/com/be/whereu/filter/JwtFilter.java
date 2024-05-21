package com.be.whereu.filter;

import com.be.whereu.config.properties.TokenPropertiesConfig;
import com.be.whereu.model.WhereUJwt;
import com.be.whereu.repository.RefreshTokenRepository;
import com.be.whereu.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final TokenPropertiesConfig tokenPropertiesConfig;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {"/api/refresh", "/login","/test","/test3"};
        String path = request.getRequestURI();
        log.info(path);
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("JwtFilter1");
        String accessJws = request.getHeader("access-token");
        String refreshJws = request.getHeader("refresh-token");
        log.info("accessJws:{} " ,accessJws);
        log.info("refreshJws:{} ",refreshJws);
        //access 와 refresh 없을떄 404 번 응답코드
        if(accessJws == null || refreshJws == null) {
            log.debug("Access token is null");
            response.setStatus(HttpStatus.NOT_FOUND.value());
            filterChain.doFilter(request, response);
            return;
        }
        WhereUJwt accessToken = WhereUJwt.fromJwt(accessJws,tokenPropertiesConfig.getAccessToken().getSecret());
        //access 유효기간 만료 시 동작 혹은 잘못된 accessJws 요청시
        //401 응답
        log.debug("accessToken:{} ",accessToken);
        if(accessToken != null && accessToken.getIsExpired()) {
            log.debug("Access token is expired");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }else if(accessToken == null) {
            log.debug("Access token is null");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("contextHolder에 저장된 정보 : " + authentication.getName());
        filterChain.doFilter(request, response);
    }
}
