package com.be.whereu.controller;

import com.be.whereu.exception.ResourceNotFoundException;
import com.be.whereu.exception.handler.GlobalExceptionHandler;
import com.be.whereu.model.dto.ProfileResponseDto;
import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.repository.MemberRepository;
import com.be.whereu.service.ProfileService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final MemberRepository memberRepository;
    private final ProfileService profileService;

    /**
     * 프로필 이미지 업로드하기
     * @param id
     * @param file
     * @return
     */
    @PostMapping(value = "/{id}/upload/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProfileImage(
            @PathVariable Long id,
            @Parameter(
                    description = "Profile image file",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            )
            @RequestParam("file") MultipartFile file) {
        try {
            String imagePath = profileService.uploadProfileImage(file);
            MemberEntity member = memberRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
            member.setProfile(imagePath);
            memberRepository.save(member);
            return ResponseEntity.ok("Profile image updated successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload profile image");
        }
    }

    /**
     * 프로필 이미지 삭제하기
     * @param id
     * @return
     */
    @DeleteMapping("/{id}/delete/image")
    public ResponseEntity<?> deleteProfileImage(@PathVariable Long id) {
        try {
            profileService.deleteProfileImage(id);
            return ResponseEntity.ok("Profile image deleted successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to delete profile image");
        }
    }

    /**
     * 닉네임 변경하기
     * @param id
     * @param nick
     * @return
     */
    @PatchMapping("/{id}/nick")
    public ResponseEntity<?> updateNickname(@PathVariable Long id, @RequestParam String nick) {
        try{
            profileService.updateNickname(id, nick);
            return ResponseEntity.ok("Nickname updated successfully");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to update nickname");
        }
    }

    /**
     * 프로필 불러오기
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponseDto> getProfileDetails(@PathVariable Long id) {
        try{
            ProfileResponseDto profileDetails = profileService.getProfileDetails(id);
            return ResponseEntity.ok(profileDetails);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ProfileResponseDto());
        }
    }
}
