package com.be.whereu.security.authentication;

import com.be.whereu.model.WhereUJwt;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SecurityContextManager {

    public void setUpSecurityContext(WhereUJwt accessToken, HttpServletRequest request) {
        String memberId = accessToken.getSubject();
        log.info("memberId:{}", memberId);
        UserDetails userDetails = new User(memberId, "", List.of());
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
