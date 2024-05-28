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
            log.debug("exception schoolName msg  :{}",e.getMessage());
        }

        return result;
    }

    @Override
    public boolean sendEmailFromUniNameAndEmail(UniversityEmaiRequestlDto dto) throws IOException {
        boolean result = false;
        try {
            log.info("universityName is:{}",dto.getUniversityName());
            log.info("email is:{}",dto.getEmail());
            Map<String,Object> response=UnivCert.certify(univPropertiesConfig.getUnivKey(),
                    dto.getEmail(),
                    dto.getUniversityName(),
                    true);
            result  = (Boolean) response.get("success");
            log.info("response get success is in sendEmailMethod:{}",response.get("success"));
        }catch (Exception e){
            log.debug("exception about sendEmail msg :{}",e.getMessage());
        }




        return result;
    }

    @Override
    public boolean verifyEmailCode(UniversityEmaiRequestlDto dto) throws IOException {
        boolean result = false;
        System.out.println("email code is:"+dto.getEmail());
        System.out.println("universityName is:"+dto.getUniversityName());
        System.out.println("code is:"+dto.getCode());
        try {
            Map<String, Object> response = UnivCert.certifyCode(univPropertiesConfig.getUnivKey(),
                    dto.getEmail(),
                    dto.getUniversityName(),
                    dto.getCode());
            log.info("response get success is in verifyEmail :{}",response.get("success"));
            result= (Boolean) response.get("success");
            if(result){
                deleteEmailAuth(dto);
            }
        }catch (Exception e){
            log.debug("exception sendCode msg  :{}",e.getMessage());
        }

       return result;
    }

    @Override
    public void deleteEmailAuth(UniversityEmaiRequestlDto dto) throws IOException {
        try {
            Map<String,Object> response=UnivCert.clear(univPropertiesConfig.getUnivKey(),
                    dto.getEmail());
            log.info( "is delete auth success:{}", (Boolean)response.get("success"));
        }catch (Exception e){
            log.debug("delete email auth is: {}",e.getMessage());
        }
    }
}
