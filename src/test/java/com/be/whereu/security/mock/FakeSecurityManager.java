package com.be.whereu.security.mock;

import com.be.whereu.model.WhereUJwt;
import com.be.whereu.security.authentication.SecurityContextManager;
import jakarta.servlet.http.HttpServletRequest;

public class FakeSecurityManager implements SecurityContextManager {
    @Override
    public void setUpSecurityContext(WhereUJwt accessToken, HttpServletRequest request) {

    }

    @Override
    public String getAuthenticatedUserName() {
        return "1";
    }
}
