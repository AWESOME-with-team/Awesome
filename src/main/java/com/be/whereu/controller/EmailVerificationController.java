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

    @PostMapping("/university/email")
    public Boolean sendEmail(@RequestBody UniversityEmaiRequestlDto dto) throws IOException {




        return emailVerificationService.sendEmailFromUniNameAndEmail(dto);



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
     * @param
     * @return
     * @throws IOException
     */
    @PostMapping("/university/code")
    public Boolean sendEmailWithCode(@RequestBody UniversityEmaiRequestlDto dto) throws IOException {
        return emailVerificationService.verifyEmailCode(dto);
    }

}
