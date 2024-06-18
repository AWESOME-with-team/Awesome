package com.be.whereu.service;


import com.be.whereu.config.properties.TokenPropertiesConfig;
import com.be.whereu.exception.ResourceNotFoundException;
import com.be.whereu.model.Gender;
import com.be.whereu.model.WhereUJwt;
import com.be.whereu.model.dto.MemberDto;
import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.model.entity.SchoolEntity;
import com.be.whereu.repository.MemberRepository;
import com.be.whereu.service.token.JwtService;
import com.be.whereu.service.token.TokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberServiceImpl implements MemberSerivce{
    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final TokenPropertiesConfig tokenPropertiesConfig;

    @Transactional
    @Override
    public String updateAndGiveNewAccessToken(MemberDto dto, String accessToken) {

        WhereUJwt jwt=WhereUJwt.fromJwt(accessToken, tokenPropertiesConfig.getAccessToken().getSecret());
        assert jwt != null;
        Long memberId =Long.parseLong(jwt.getSubject());
        Optional<MemberEntity> memberEntity = memberRepository.findById(memberId);
        if (memberEntity.isPresent()) {
            MemberEntity memberEntity1 = memberEntity.get();
            memberEntity1.setNick(dto.getNick());
            memberEntity1.setUniversityEmail(dto.getUniversityEmail());
            memberEntity1.setBirth(dto.getBirth());
            memberEntity1.setUniversityName(dto.getUniversityName());
            memberEntity1.setGender(Gender.fromString(dto.getGender()));
            try {
                // Save the entity
                memberRepository.save(memberEntity1);
                // If save is successful, create and return the new access token
                return jwtService.createAccessTokenFromMemberId(memberEntity1.getId(), true);
            } catch (Exception e) {

                log.error("error while saving access token : {}", e.getMessage());

                return null;
            }
        }

        log.info("member not found");

        return null;
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
        MemberEntity entity= memberRepository.findByNick(nick).get();
        if(entity != null) {
            log.info("NickName is: {}", entity.getNick());
            isNickExist = true;
        }

        return isNickExist;
    }

    /**
     *
     * @param nick
     * @param groupId
     * @return 닉네임 리스트 (그룹에 이미 속해있는 맴버와 이미 해당그룹에 요청을 받은 맴버를 제외하고)
     */
    @Override
    public List<String> searchNickListByGroupId(String nick , Long groupId) {

        return  memberRepository.findMemberListExcludingGroupMemberAndAnotherGenderAndAlreadyRequestedList(nick, groupId).orElseThrow(()-> new ResourceNotFoundException("no resource about "+nick))
                .stream()
                .map(MemberEntity::getNick)
                .toList();

    }


}
