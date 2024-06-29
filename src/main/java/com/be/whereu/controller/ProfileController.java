package com.be.whereu.controller;

import com.be.whereu.exception.ResourceNotFoundException;
import com.be.whereu.model.dto.ProfileResponseDto;
import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.repository.MemberRepository;
import com.be.whereu.security.authentication.SecurityContextManager;
import com.be.whereu.security.authentication.SecurityManagerImpl;
import com.be.whereu.service.ProfileService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final MemberRepository memberRepository;
    private final ProfileService profileService;
    private final SecurityContextManager securityContextManager;

    /**
     * 프로필 이미지 업로드하기
     * @param id
     * @param file
     * @return
     */
    @PostMapping(value = "/{id}/upload/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProfileImage(
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
     * @return
     */
    @DeleteMapping("/delete/image")
    public ResponseEntity<String> deleteProfileImage() {
        try {
            profileService.deleteProfileImage();
            return ResponseEntity.ok("Profile image deleted successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to delete profile image");
        }
    }

    /**
     * 닉네임 변경하기
     * @param nick
     * @return
     */
    @PatchMapping("/change/nick")
    public ResponseEntity<String> updateNickname(@RequestParam String nick) {
        try{
            profileService.updateNickname(nick);
            return ResponseEntity.ok("Nickname updated successfully");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to update nickname");
        }
    }

    /**
     * 프로필 불러오기
     * @param +
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<ProfileResponseDto> getProfileDetails() {
        System.out.println("여기 이셉션");
        try{
            System.out.println("profile details");
            ProfileResponseDto profileDetails = profileService.getProfileDetails();
            return ResponseEntity.ok(profileDetails);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ProfileResponseDto());
        }
    }

    /**
     * 저장된 프로필 이미지 클라이언트에게 반환
     * @return
     */
    @GetMapping("/image")
    public ResponseEntity<Resource> getProfileImage() {

        String username = securityContextManager.getAuthenticatedUserName();
        Long id = Long.parseLong(username);

        MemberEntity member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        String imagePath = member.getProfile();
        Path path = Paths.get(profileService.getProfileImagePath(), imagePath);
        Resource resource = new FileSystemResource(path);

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        String contentType;
        try {
            contentType = Files.probeContentType(path);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
        } catch (IOException e) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
