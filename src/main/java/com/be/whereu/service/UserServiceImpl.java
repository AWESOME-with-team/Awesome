package com.be.whereu.service;

import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final MemberRepository memberRepository;
    private final TokenService tokenService;

    @Override
    public void checkAndJoinUser(String email, HttpServletResponse response) {
        MemberEntity memberEntity = memberRepository.findByEmail(email);
        if (memberEntity == null) {
            MemberEntity member = new MemberEntity();
            member.setEmail(email);
            MemberEntity savedMember = memberRepository.save(member);
            Long memberId = savedMember.getId();
            tokenService.createTokenAndAddCookie(memberId, response, false);
        } else {
            Long memberId = memberEntity.getId();
            log.info("isUniversityEmail is: {}",  memberEntity.getUniversityEmail());
            boolean isEmailExist = memberEntity.getUniversityEmail() != null;
            tokenService.createTokenAndAddCookie(memberId, response,isEmailExist);
        }

    }
}