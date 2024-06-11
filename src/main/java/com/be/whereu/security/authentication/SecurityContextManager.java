package com.be.whereu.security.authentication;

import com.be.whereu.model.WhereUJwt;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.util.List;



public interface SecurityContextManager {

    public void setUpSecurityContext(WhereUJwt accessToken, HttpServletRequest request);

    public String getAuthenticatedUserName();

}
