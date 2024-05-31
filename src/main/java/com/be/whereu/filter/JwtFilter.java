package com.be.whereu.filter;

import com.be.whereu.model.WhereUJwt;
import com.be.whereu.security.authentication.SecurityContextManager;
import com.be.whereu.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final SecurityContextManager securityContextManager;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {
                "/swagger-ui/swagger-initializer.js", "/swagger-ui/index.css", "/swagger-ui/swagger-ui-bundle.js",
                "/swagger-ui/swagger-ui-standalone-preset.js", "/v3/api-docs/swagger-config", "/swagger-ui/swagger-ui-standalone-preset.js",
                "/swagger-ui/index.html", "/swagger-ui/swagger-ui.css", "/v3/api-docs", //swagger
                "/api/refresh", "/login", "/api/login", "/api/login/success", "/api/login/fail","/api/university/code","/api/university/email",
                "/api/member/nick", "/api/university/check","/api/member/update" // Corrected path
//                ,"/index2.html","/ws"
        };
        String path = request.getRequestURI();
        log.info(path);
//        boolean result = Arrays.stream(excludePath).anyMatch(exclude -> {
//            boolean match = path.startsWith(exclude.trim());
//            log.info("Comparing '{}' with '{}': {}", path, exclude, match);
//            return match;
//        });
        return Arrays.stream(excludePath).anyMatch(exclude -> path.startsWith(exclude.trim())); // Change from false to result
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("왜 안와");
        String accessJws = request.getHeader("access-token");
        String refreshJws = request.getHeader("refresh-token");
        log.info("accessJws:{} " ,accessJws);
        log.info("refreshJws:{} ",refreshJws);
        if(accessJws == null && refreshJws == null) {
            Cookie[] cookies = request.getCookies();
            if(cookies != null) {
                for(Cookie cookie : cookies) {
                    if(cookie.getName().equals("access-token")) {
                        accessJws = cookie.getValue();
                    }else if(cookie.getName().equals("refresh-token")) {
                        refreshJws = cookie.getValue();
                    }
                }
            }
        }
        //access 와 refresh 없을떄 404 번 응답코드
        if (isNotExistToken(accessJws, refreshJws, response, filterChain, request)) return;
        WhereUJwt accessToken = tokenService.validateAccessTokenAndToMakeObjectJwt(accessJws);
        //access 유효기간 만료 시 동작 혹은 잘못된 accessJws 요청시 401 응답
        log.debug("accessToken:{} ",accessToken);
        //if (isInvalidAccessToken(accessToken, response, filterChain, request)) return;
        //필수 이메일이 존재하지 않으면 201번
        //if(isNotTokenExistUniEmail(accessJws,response,filterChain,request)) return;
        //모든 access 유효성 검사 통과시 context 주입
        securityContextManager.setUpSecurityContext(accessToken, request);
        log.info("contextHolder in {}" ,SecurityContextHolder.getContext().toString());
        filterChain.doFilter(request, response);
    }


    private boolean isNotExistToken(String accessJws, String refreshJws, HttpServletResponse response, FilterChain filterChain, HttpServletRequest request) throws IOException, ServletException {
        if (accessJws == null || refreshJws == null) {
            log.debug("Access token or Refresh token is null");
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return true;
        }
        return false;

    }
    private boolean isInvalidAccessToken(WhereUJwt accessToken, HttpServletResponse response, FilterChain filterChain, HttpServletRequest request) throws IOException, ServletException {
        if (accessToken != null && accessToken.getIsExpired()) {
            log.debug("Access token is expired");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return true;
        } else if (accessToken == null) {
            log.debug("Access token is null");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return true;
        }
        return false;
    }

    private boolean isNotTokenExistUniEmail(String accessJws, HttpServletResponse response, FilterChain filterChain, HttpServletRequest request) throws ServletException, IOException {
        if(!tokenService.isUniEmailExistFromToken(accessJws)){
            log.info("access 201 response ");
            response.setStatus(HttpStatus.CREATED.value());
            return true;
        }
        return false;
    };
}
