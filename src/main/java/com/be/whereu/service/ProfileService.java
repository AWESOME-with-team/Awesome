package com.be.whereu.service;

import com.be.whereu.model.dto.ProfileResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileService {
    String uploadProfileImage(MultipartFile file) throws IOException;
    void deleteProfileImage(Long memberId) throws IOException;
    void updateNickname(Long memberId, String nickname);
    ProfileResponseDto getProfileDetails(Long memberId);
}
