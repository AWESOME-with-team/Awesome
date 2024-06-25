package com.be.whereu.service;

import com.be.whereu.config.properties.ProfileImageConfig;
import com.be.whereu.exception.ResourceNotFoundException;
import com.be.whereu.model.dto.ProfileResponseDto;
import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.repository.MemberRepository;
import com.be.whereu.security.authentication.SecurityContextManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    private final ProfileImageConfig profileImageConfig;
    private final SecurityContextManager securityContextManager;
    private final MemberRepository memberRepository;

    //프로필 이미지 업로드
    @Override
    @Transactional
    public String uploadProfileImage(MultipartFile file) throws IOException {
        String username = securityContextManager.getAuthenticatedUserName();
        Optional<MemberEntity> optionalMember = memberRepository.findById(Long.parseLong(username));

        if (optionalMember.isEmpty()) {
            throw new ResourceNotFoundException("Member not found");
        }

        MemberEntity member = optionalMember.get();
        String oldFileName = member.getProfile();

        String newFileName = member.getId() + "_" + UUID.randomUUID() + file.getOriginalFilename();
        Path filePath = Paths.get(profileImageConfig.getProfileImagePath(), newFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);


        //기존에 프로필 이미지가 있으면 삭제
        if(oldFileName != null && !oldFileName.isEmpty()){
            Path oldFilePath = Paths.get(profileImageConfig.getProfileImagePath(), oldFileName);
            if(Files.exists(oldFilePath)){
                Files.delete(oldFilePath);
            }
        }

        // DB에는 새 파일 이름만 저장
        return newFileName;
    }

    //프로필 이미지 삭제
    @Override
    public void deleteProfileImage(Long memberId) throws IOException {
        String username = securityContextManager.getAuthenticatedUserName();
        Optional<MemberEntity> optionalMember = memberRepository.findById(Long.parseLong(username));

        if (optionalMember.isPresent()) {
            MemberEntity member = optionalMember.get();
            String oldFileName = member.getProfile();
            if (oldFileName != null && !oldFileName.isEmpty()) {
                Path oldFilePath = Paths.get(profileImageConfig.getProfileImagePath(), oldFileName);
                if (Files.exists(oldFilePath)) {
                    Files.delete(oldFilePath);
                }
                member.setProfile(null);
                memberRepository.save(member);
            }
        }
    }

    //닉네임 변경
    @Override
    public void updateNickname(Long memberId, String nick) {
        String username = securityContextManager.getAuthenticatedUserName();
        Optional<MemberEntity> optionalMember = memberRepository.findById(Long.parseLong(username));

        if (optionalMember.isPresent()) {
            MemberEntity member = optionalMember.get();
            member.setNick(nick);
            memberRepository.save(member);
        }
    }

    //프로필 정보 불러오기
    @Override
    public ProfileResponseDto getProfileDetails(Long memberId) {
        String username = securityContextManager.getAuthenticatedUserName();
        Optional<MemberEntity> optionalMember = memberRepository.findById(Long.parseLong(username));

        if (optionalMember.isPresent()) {
            MemberEntity member = optionalMember.get();
            ProfileResponseDto response = new ProfileResponseDto();
            response.setNick(member.getNick());
            response.setEmail(member.getEmail());
            response.setUniversityEmail(member.getUniversityEmail());
            response.setUniversityName(member.getUniversityName());
            response.setProfile(member.getProfile());

            return response;
        } else {
            throw new ResourceNotFoundException("Member not found");
        }
    }
}
