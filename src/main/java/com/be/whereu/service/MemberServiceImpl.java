package com.be.whereu.service;


import com.be.whereu.model.dto.MemberDto;
import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.Console;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberSerivce{
    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    @Transactional
    @Override
    public void update(MemberDto dto, HttpServletResponse response) throws IOException {
        Optional<MemberEntity> memberEntity = memberRepository.findById(dto.getId());
        if(memberEntity.isPresent()) {
            MemberEntity entity = memberEntity.get();
            entity.setNick(dto.getNick());
            entity.setUniversityName(dto.getUniversityName());
            entity.setUniversityMajor(dto.getUniversityMajor());
            entity.setBirth(dto.getBirth());

            long memberId = dto.getId();

            //refreshToken 쿠키등록
            String accessJws = jwtService.createAccessTokenFromMemberId(memberId);
            Cookie AccessCookie = new Cookie("access", accessJws);

            AccessCookie.setHttpOnly(true);// 자바스크립트 접근 금지
            AccessCookie.setPath("/");// 모든 경로에서 쿠키 사용가능
            response.addCookie(AccessCookie);


            //AccessToken 쿠키등록
            String refreshJws = jwtService.createRefreshTokenFromMemberId(memberId);
            Cookie refreshCookie = new Cookie("refresh", refreshJws);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setPath("/");
            response.addCookie(refreshCookie);
            response.sendRedirect("http://localhost:9000/test");

        }else{
            response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
        }
    }

    @Override
    public MemberDto getData(long memberId) {
        Optional<MemberEntity> optionalEntity=memberRepository.findById(memberId);
        MemberEntity entity = optionalEntity.get();
        return MemberDto.toDto(entity);
    }


}
