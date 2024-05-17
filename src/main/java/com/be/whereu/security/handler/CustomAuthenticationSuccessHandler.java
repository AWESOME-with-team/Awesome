package com.be.whereu.security.handler;

import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.repository.MemberRepository;
import com.be.whereu.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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


    private static final String URI = "/auth/success";
    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // user 안에 getAttribute가 있음
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        Map<String,Object> attributes = user.getAttributes();
        // System.out.println("test:"+attributes);
        String email=(String)attributes.get("email");
        System.out.println(email);

        MemberEntity memberEntity = memberRepository.findByEmail(email);
        if(memberEntity==null) { // 정보가 존재하지 않음
            //member객체를 생성해서 email 담아주기
            MemberEntity member = new MemberEntity();
            member.setEmail(email);

            // DB에 저장을 함과 동시에 savedMember 객체에 담기  entity에서 id를 만들어내기 때문에 id 값도 담김
            MemberEntity savedMember=memberRepository.save(member);
            Long memberId= savedMember.getId();

            String accessJws = jwtService.createAccessTokenFromMemberId(memberId);
            Cookie AccessCookie = new Cookie("access", accessJws);
            AccessCookie.setHttpOnly(true);// 자바스크립트 접근 금지
            AccessCookie.setPath("/");// 모든 경로에서 쿠키 사용가능
            response.addCookie(AccessCookie);



            //AccessToken 쿠키등록
            String refreshJws = jwtService.createRefreshTokenFromMemberId(memberId);
            Cookie refreshCookie = new Cookie("refresh", refreshJws);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setPath("/");
            response.addCookie(refreshCookie);


        } else { // 존재하는 경우
            // access token, refresh token 재발급
            Long memberId= memberEntity.getId();
            //refreshToken 쿠키등록
            String accessJws = jwtService.createAccessTokenFromMemberId(memberId);
            Cookie AccessCookie = new Cookie("access", accessJws);
            AccessCookie.setHttpOnly(true);// 자바스크립트 접근 금지
            AccessCookie.setPath("/");// 모든 경로에서 쿠키 사용가능
            response.addCookie(AccessCookie);


            //AccessToken 쿠키등록
            String refreshJws = jwtService.createRefreshTokenFromMemberId(memberId);
            Cookie refreshCookie = new Cookie("refresh", refreshJws);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setPath("/");
            response.addCookie(refreshCookie);



        }





    }
}
