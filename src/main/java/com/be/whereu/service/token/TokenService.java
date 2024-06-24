package com.be.whereu.service.token;

import com.be.whereu.model.WhereUJwt;
import jakarta.servlet.http.HttpServletResponse;

public interface TokenService {
    public void createTokenAndAddCookie(Long memberId, HttpServletResponse response, boolean universityEmail);
    public boolean isUniEmailExistFromToken(String accessJws);
    public WhereUJwt validateAccessTokenAndToMakeObjectJwt(String accessJws);
    public boolean deleteRefreshTokenWithContextHolderFromMemberId();
}
