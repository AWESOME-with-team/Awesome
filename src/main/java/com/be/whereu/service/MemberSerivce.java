package com.be.whereu.service;

import com.be.whereu.model.dto.MemberDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


public interface MemberSerivce {
    public void update(MemberDto dto, HttpServletResponse response) throws IOException;
    public MemberDto getData(long memberId);
}
