package com.be.whereu.security.handler;

import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.repository.MemberRepository;
import com.be.whereu.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = user.getAttributes();
        String email = (String) attributes.get("email");
        System.out.println(email);

        MemberEntity memberEntity = memberRepository.findByEmail(email);
        if (memberEntity == null) { // 정보가 존재하지 않음
            //member객체를 생성해서 email 담아주기
            MemberEntity member = new MemberEntity();
            member.setEmail(email);

            // DB에 저장을 함과 동시에 savedMember 객체에 담기  entity에서 id를 만들어내기 때문에 id 값도 담김
            MemberEntity savedMember=memberRepository.save(member);
            Long memberId= savedMember.getId();


            //AccessToken 쿠키등록
            String refreshJws = jwtService.createRefreshTokenFromMemberId(memberId);



        } else { // 존재하는 경우
            // access token, refresh token 재발급
            Long memberId= memberEntity.getId();
            //refreshToken 쿠키등록
            String accessJws = jwtService.createAccessTokenFromMemberId(memberId);
            log.debug("AccessJws:{}",accessJws);
            Cookie AccessCookie = new Cookie("access-token", accessJws);
            AccessCookie.setHttpOnly(true);// 자바스크립트 접근 금지
            AccessCookie.setPath("/");// 모든 경로에서 쿠키 사용가능
            response.addCookie(AccessCookie);


            //AccessToken 쿠키등록
            String refreshJws = jwtService.createRefreshTokenFromMemberId(memberId);
            Cookie refreshCookie = new Cookie("refresh-token", refreshJws);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setPath("/");
            response.addCookie(refreshCookie);
            response.sendRedirect("http://172.18.40.255:9000/test3");
        }


    }
}

