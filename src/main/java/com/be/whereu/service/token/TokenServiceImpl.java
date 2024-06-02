package com.be.whereu.service.token;

import com.be.whereu.config.properties.TokenPropertiesConfig;
import com.be.whereu.model.WhereUJwt;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {
    private final TokenPropertiesConfig tokenPropertiesConfig;
    private final JwtService jwtService;

    @Override
    public void createTokenAndAddCookie(Long memberId, HttpServletResponse response, boolean universityEmail) {
        String accessJws = jwtService.createAccessTokenFromMemberId(memberId,universityEmail);
        log.debug("AccessJws: {}", accessJws);
        Cookie accessCookie = new Cookie("access-token", accessJws);
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        response.addCookie(accessCookie);

        String refreshJws = jwtService.createRefreshTokenFromMemberId(memberId);
        log.debug("refreshJws: {}", refreshJws);
        Cookie refreshCookie = new Cookie("refresh-token", refreshJws);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        response.addCookie(refreshCookie);
    }

    @Override
    public boolean isUniEmailExistFromToken(String accessJws) {
        boolean isUniverseEmailExist = false;
        WhereUJwt jwt = WhereUJwt.fromJwt(accessJws,tokenPropertiesConfig.getAccessToken().getSecret());
        if(jwt != null) {
            isUniverseEmailExist = jwt.getIsEmailExist();
        }
        return isUniverseEmailExist;
    }

    @Override
    public WhereUJwt validateAccessTokenAndToMakeObjectJwt(String accessJws) {
        return WhereUJwt.fromJwt(accessJws, tokenPropertiesConfig.getAccessToken().getSecret());
    }
}