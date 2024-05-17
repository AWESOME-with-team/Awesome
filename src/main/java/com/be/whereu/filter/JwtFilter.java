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
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final TokenPropertiesConfig tokenPropertiesConfig;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            System.out.println("쿠키 없음");
            filterChain.doFilter(request, response);
            return;
        }

        String registerTokenJws = Arrays.stream(cookies)
                .filter(cookie -> "register".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        String accessTokenJws = Arrays.stream(cookies)
                .filter(cookie -> "access".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        String refreshTokenJws = Arrays.stream(cookies)
                .filter(cookie -> "refresh".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        // jws를 복호화 시켜주는 역할
        WhereUJwt registerToken = registerTokenJws != null ?
                WhereUJwt.fromJwt(registerTokenJws, tokenPropertiesConfig.getRegisterToken().getSecret()) : null;

        if (registerToken != null && !registerToken.isExpired() && refreshTokenJws == null) {
            System.out.println("refresh token 없음");
            String id = registerToken.getSubject();
            System.out.println("id:" + id);

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_GUEST")); // "ROLE_" 접두어는 Spring Security의 관례입니다.

            UserDetails ud = new User(id, "", authorities);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        WhereUJwt accessToken = accessTokenJws != null ?
                WhereUJwt.fromJwt(accessTokenJws, tokenPropertiesConfig.getAccessToken().getSecret()) : null;

        if (accessToken == null || accessToken.isExpired()) {
            System.out.println("access token 없음");
            filterChain.doFilter(request, response);
            return;
        }

        if (!accessToken.isExpired()) {
            String id = accessToken.getSubject();
            System.out.println("id:" + id);

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER")); // "ROLE_" 접두어는 Spring Security의 관례입니다.

            UserDetails ud = new User(id, "", authorities);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
            System.out.println("access 토큰이 없음");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("contextHolder에 저장된 정보 : " + authentication.getName());
        filterChain.doFilter(request, response);
    }
}
