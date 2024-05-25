package com.be.whereu.controller;

import com.be.whereu.config.properties.UnivPropertiesConfig;
import com.be.whereu.model.dto.UniversityEmaiRequestlDto;
import com.be.whereu.service.EmailVerificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.univcert.api.UnivCert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EmailVerificationController {
    private final UnivPropertiesConfig univPropertiesConfig;
    private final EmailVerificationService emailVerificationService;
    /**
     * email로 인증 번호 보내기
     * @return
     * @throws IOException
     */

    @PostMapping("/code_to_email")
    public Boolean sendCode(@RequestBody UniversityEmaiRequestlDto dto) throws IOException {

            Map<String,Object> response=UnivCert.certify(univPropertiesConfig.getUnivKey(),
                    dto.getEmail(),
                    dto.getUniversityName(),
                    true);


        return (Boolean) response.get("success");



    }

    /**
     *
     * @param dto universityName
     * @return isExist true or inNotExist false
     * @throws IOException
     */
    @PostMapping("/university/check")
    public Boolean checkUniversityName(@RequestBody UniversityEmaiRequestlDto dto) throws IOException {
        return emailVerificationService.verifyUniversityName(dto);
    }

    /**
     * 인증번호 확인
     * @param email
     * @param universityName
     * @param code
     * @return
     * @throws IOException
     */
    @PostMapping("/code_to_verification")
    public String sendCode(String email, String universityName,int code) throws IOException {
        Map<String, Object> response = UnivCert.certifyCode(univPropertiesConfig.getUnivKey(),
                email,
                universityName,
                code);

        // objectMapper를 통해서 상태를 받아온 status를 json으로 변환해준다.
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            e.printStackTrace();
            return "서버 에러";
        }

        return json;

    }

    /**
     * 인증된 특정 이메일 기록 삭제
     * @param email
     * @return
     * @throws IOException
     */
    @PostMapping("/delete_to_email")
    public String deleteEmail(String email) throws IOException {
        Map<String,Object> response=UnivCert.clear(univPropertiesConfig.getUnivKey(),
                email);

        // objectMapper를 통해서 상태를 받아온 status를 json으로 변환해준다.
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            e.printStackTrace();
            // 예외 처리 코드 작성
            return "서버 에러";
        }

        return json;

    }
}
