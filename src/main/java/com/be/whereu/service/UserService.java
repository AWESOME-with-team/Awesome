package com.be.whereu.service;

import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    public void checkAndJoinUser(String email, HttpServletResponse response);
}
