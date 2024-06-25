package com.be.whereu.service.token;

import com.be.whereu.config.properties.TokenPropertiesConfig;
import com.be.whereu.model.WhereUJwt;
import com.be.whereu.repository.RefreshTokenRepository;
import com.be.whereu.security.authentication.SecurityContextManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {
    private final TokenPropertiesConfig tokenPropertiesConfig;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SecurityContextManager securityContextManager;

    @Override
    public void createTokenAndAddCookie(Long memberId, HttpServletResponse response, boolean universityEmail) {
        String accessJws = jwtService.createAccessTokenFromMemberId(memberId,universityEmail);
        log.debug("AccessJws: {}", accessJws);
        Cookie accessCookie = new Cookie("access-token", accessJws);
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        response.addCookie(accessCookie);
        response.addHeader("access-token" , accessJws);


        String refreshJws = jwtService.createRefreshTokenFromMemberId(memberId);
        log.debug("refreshJws: {}", refreshJws);
        Cookie refreshCookie = new Cookie("refresh-token", refreshJws);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        response.addCookie(refreshCookie);
        response.addHeader("refresh-token" , refreshJws);
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

    @Transactional
    @Override
    public boolean deleteRefreshTokenWithContextHolderFromMemberId() {
        Long memberId = Long.parseLong(securityContextManager.getAuthenticatedUserName());
        int result=refreshTokenRepository.deleteByMemberId(memberId);
        if(result>0) {
            // SecurityContextHolder 비우기
            SecurityContextHolder.clearContext();
            return true;
        }else{
            return false;
        }


    }
}