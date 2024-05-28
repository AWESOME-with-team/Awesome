package com.be.whereu.service;


import com.be.whereu.model.dto.MemberDto;
import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.Console;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberSerivce{
    private static final Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);
    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    @Transactional
    @Override
    public void update(MemberDto dto ) {
        Optional<MemberEntity> memberEntity = memberRepository.findById(dto.getId());
    }

    @Override
    public MemberDto getData(long memberId) {
        Optional<MemberEntity> optionalEntity=memberRepository.findById(memberId);
        MemberEntity entity = optionalEntity.get();
        return MemberDto.toDto(entity);
    }

    @Override
    public Boolean findMemberNick(String nick) {
        boolean isNickExist = false;
        MemberEntity entity= memberRepository.findByNick(nick);
        if(entity != null) {
            log.info("NickName is: {}", entity.getNick());
            isNickExist = true;
        }

        return isNickExist;
    }


}
