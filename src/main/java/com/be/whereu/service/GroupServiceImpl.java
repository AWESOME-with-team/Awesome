package com.be.whereu.service;

import com.be.whereu.exception.CustomServiceException;
import com.be.whereu.exception.ResourceNotFoundException;
import com.be.whereu.model.dto.GroupDto;
import com.be.whereu.model.dto.MemberDto;
import com.be.whereu.model.entity.GroupEntity;
import com.be.whereu.model.entity.MemberGroupEntity;
import com.be.whereu.repository.ChatRepository;
import com.be.whereu.repository.GroupRepository;
import com.be.whereu.repository.MemberGroupRepository;
import com.be.whereu.security.authentication.SecurityContextManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final SecurityContextManager securityContextManager;

    public boolean createGroup(GroupDto groupDto) {
        try {
            log.info("groupDto :{}", groupDto.toString());
            groupDto.setGender("남자");
            groupDto.setTotal(1);
            Long memberId = Long.parseLong(securityContextManager.getAuthenticatedUserName());
            groupDto.setHostId(memberId);
            log.info("hostId: {}", securityContextManager.getAuthenticatedUserName());
            groupDto.setIsMatch("no");
            GroupEntity groupEntity = GroupEntity.toEntity(groupDto);
            GroupEntity savedGroupEntity = groupRepository.save(groupEntity);

            // MemberGroupEntity 생성 및 저장
            MemberGroupEntity memberGroupEntity = MemberGroupEntity.ToMemberGroupEntity(savedGroupEntity, memberId);
            memberGroupRepository.save(memberGroupEntity);



            return true;
        } catch (DataAccessException e) {
            // 데이터베이스 관련 예외 처리
            log.error("Database error creating group: {} ", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("createGroup occurred {}", e.getMessage());
            return false;
        }
    }

    @Override
    public List<GroupDto> getGroupList() {
        try {
            Long memberId = Long.parseLong(securityContextManager.getAuthenticatedUserName());
            List<MemberGroupEntity> memberGroups = memberGroupRepository.findListWithGroupByMemberId(memberId)
                    .orElseThrow(() -> new ResourceNotFoundException("Member ID :" + memberId + " not found Group"));

            return memberGroups.stream()
                    .map(memberGroupEntity -> GroupDto.toDto(memberGroupEntity.getGroup()))
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            // 데이터베이스 관련 예외 처리
            log.error("Database error getListGroup: {} ", e.getMessage());
            throw new CustomServiceException("Failed to get group list due to database error", e);
        } catch (Exception e) {
            log.error("getListGroup occurred {}", e.getMessage());
            throw new CustomServiceException("Failed to get group list", e);
        }
    }

    @Override
    public GroupDto groupDetails(Long groupId) {
        try {
            GroupEntity groupEntity = groupRepository.findGroupWithMembers(groupId)
                    .orElseThrow(() -> new ResourceNotFoundException("Group Id:"+groupId+ " not found"));
            GroupDto groupDto=GroupDto.toDto(groupEntity);
            List<MemberDto> memberList= groupEntity.getMemberGroup().stream()
                    .map(MemberGroupEntity::getMember)
                    .map(MemberDto::toDto)
                    .toList();
           // System.out.println(memberList.getFirst().getBirth());
            groupDto.setMembers(memberList);
           // System.out.println(memberList.getFirst().getNick());
            return groupDto;
        } catch (DataAccessException e) {
                // 데이터베이스 관련 예외 처리
                log.error("Database error GroupDetail: {} ", e.getMessage());
                throw new CustomServiceException("Failed to get group detail due to database error", e);
        } catch (Exception e) {
            log.error("groupDetails occurred {}", e.getMessage());
            throw new CustomServiceException("Failed to get group Detail", e);
        }

    }

}
