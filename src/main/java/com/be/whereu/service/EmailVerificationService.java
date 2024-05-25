package com.be.whereu.service;

import com.be.whereu.model.dto.UniversityEmaiRequestlDto;

import java.io.IOException;

public interface EmailVerificationService {

    public boolean verifyUniversityName(UniversityEmaiRequestlDto dto) throws IOException;
}
