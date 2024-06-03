package com.be.whereu.security.handler;

import com.be.whereu.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    private final UserService userService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User user = (OAuth2User) authentication.getPrincipal();

        Map<String, Object> attributes = user.getAttributes();
        String email = (String) attributes.get("email");
        log.info("email is: {}",email);

        userService.checkAndJoinUser(email, response);
        response.sendRedirect("http://172.30.1.16:9000/api/login/success");

        userService.checkAndJoinUser(email, response);
        //response.sendRedirect("http://172.18.37.83:9000/api/login/success");
        response.sendRedirect("http://localhost:9000/index2.html");
        //response.sendRedirect("http://localhost:9000/api/login/success");
    }
}

