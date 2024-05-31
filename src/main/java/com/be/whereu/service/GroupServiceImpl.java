package com.be.whereu.service;

import com.be.whereu.model.dto.GroupDto;
import com.be.whereu.model.entity.GroupEntity;
import com.be.whereu.repository.GroupRepository;
import com.be.whereu.security.authentication.SecurityContextManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService{

    private final GroupRepository groupRepository;
    private final SecurityContextManager securityContextManager;
    public boolean createGroup(GroupDto groupDto) {
        try {
            groupDto.setHostId(Long.parseLong(securityContextManager.getAuthenticatedUserName()));
            log.info("hostId: {}", securityContextManager.getAuthenticatedUserName());
            groupDto.setIsMatch("no");
            groupRepository.save(GroupEntity.toEntity(groupDto));

            return true;
        }catch (DataAccessException e) {
            // 데이터베이스 관련 예외 처리
            log.error("Database error creating group: {} ", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("createGroup occurred {}",e.getMessage());
            return false;
        }
    };
}
