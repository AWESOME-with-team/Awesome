package com.be.whereu.filter;

import com.be.whereu.model.WhereUJwt;
import com.be.whereu.security.authentication.SecurityContextManager;

import com.be.whereu.service.token.TokenService;
import com.be.whereu.service.token.TokenValidatorService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    private final TokenValidatorService tokenValidatorService;


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {
                "/swagger-ui/swagger-initializer.js", "/swagger-ui/index.css", "/swagger-ui/swagger-ui-bundle.js",
                "/swagger-ui/swagger-ui-standalone-preset.js", "/v3/api-docs/swagger-config", "/swagger-ui/swagger-ui-standalone-preset.js",
                "/swagger-ui/index.html", "/swagger-ui/swagger-ui.css", "/v3/api-docs", //swagger
                "/api/refresh", "/login", "/api/login", "/api/login/success", "/api/login/fail","/api/university/code","/api/university/email",
                "/api/member/nick", "/api/university/check","/api/member/update" ,"/h2-console","/favicon.ico"// Corrected path

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


        extractTokensFromRequest(request);
        String accessJws = (String) request.getAttribute("accessJws");
        String refreshJws = (String) request.getAttribute("refreshJws");
        log.info("accessJws:{} " ,accessJws);
        log.info("refreshJws:{} ",refreshJws);
        //access 와 refresh 없을떄 404 번 응답코드
        if (tokenValidatorService.isNotExistToken(accessJws, refreshJws, response)) return;
        WhereUJwt accessToken = tokenService.validateAccessTokenAndToMakeObjectJwt(accessJws);
        //access 유효기간 만료 시 동작 혹은 잘못된 accessJws 요청시 401 응답
        log.debug("accessToken:{} ",accessToken);
        if (tokenValidatorService.isInvalidAccessToken(accessToken, response)) return;
        //필수 이메일이 존재하지 않으면 201번
        if(tokenValidatorService.isNotTokenExistUniEmail(accessJws,response)) return;
        //모든 access 유효성 검사 통과시 context 주입
        securityContextManager.setUpSecurityContext(accessToken, request);
        filterChain.doFilter(request, response);
    }
    private String getTokenFromCookies(Cookie[] cookies, String tokenName) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(tokenName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private void extractTokensFromRequest(HttpServletRequest request) {
        String accessJws = request.getHeader("access-token");
        String refreshJws = request.getHeader("refresh-token");

        if (accessJws == null && refreshJws == null) {
            Cookie[] cookies = request.getCookies();
            accessJws = getTokenFromCookies(cookies, "access-token");
            refreshJws = getTokenFromCookies(cookies, "refresh-token");
        }

        request.setAttribute("accessJws", accessJws);
        request.setAttribute("refreshJws", refreshJws);
    }


}
