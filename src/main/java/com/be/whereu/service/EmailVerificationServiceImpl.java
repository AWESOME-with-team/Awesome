package com.be.whereu.service;

import com.be.whereu.config.properties.UnivPropertiesConfig;
import com.be.whereu.model.dto.UniversityEmaiRequestlDto;
import com.univcert.api.UnivCert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final UnivPropertiesConfig univPropertiesConfig;

    @Override
    public boolean verifyUniversityName(UniversityEmaiRequestlDto dto) throws IOException {
        boolean result = false;
        try {
            log.info("universityName:{}",dto.getUniversityName());
            Map<String, Object> response = UnivCert.check(dto.getUniversityName());
            log.info("response get success is:{}",response.get("success"));
            result =(Boolean) response.get("success");
        }catch (Exception e){
            log.debug("exception msg :{}",e.getMessage());
        }

        return result;
    }
}
