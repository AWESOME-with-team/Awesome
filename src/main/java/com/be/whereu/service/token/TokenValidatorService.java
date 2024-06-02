package com.be.whereu.service.token;

import com.be.whereu.model.WhereUJwt;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenValidatorService {

    private final TokenService tokenService;

    public boolean isNotExistToken(String accessJws, String refreshJws, HttpServletResponse response) throws IOException {
        if (accessJws == null || refreshJws == null) {
            log.debug("Access token or Refresh token is null");
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return true;
        }
        return false;
    }

    public boolean isInvalidAccessToken(WhereUJwt accessToken, HttpServletResponse response) throws IOException {
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

    public boolean isNotTokenExistUniEmail(String accessJws, HttpServletResponse response) throws IOException {
        if (!tokenService.isUniEmailExistFromToken(accessJws)) {
            log.info("access 201 response ");
            response.setStatus(HttpStatus.CREATED.value());
            return true;
        }
        return false;
    }
}