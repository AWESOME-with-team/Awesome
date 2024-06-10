package com.be.whereu.service;

import com.be.whereu.exception.CustomServiceException;
import com.be.whereu.exception.ResourceNotFoundException;
import com.be.whereu.model.dto.GroupDto;
import com.be.whereu.model.dto.GroupRequestDto;
import com.be.whereu.model.dto.MemberDto;
import com.be.whereu.model.entity.GroupEntity;
import com.be.whereu.model.entity.GroupRequestEntity;
import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.model.entity.MemberGroupEntity;
import com.be.whereu.repository.GroupRepository;
import com.be.whereu.repository.GroupRequestRepository;
import com.be.whereu.repository.MemberGroupRepository;
import com.be.whereu.repository.MemberRepository;
import com.be.whereu.security.authentication.SecurityContextManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final ChatService chatService;
    private final SecurityContextManager securityContextManager;
    private final MemberRepository memberRepository;
    private final GroupRequestRepository groupRequestRepository;

    @Transactional
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

            // group 생성시 채팅방도 개설
            chatService.createChatByGroup(savedGroupEntity.getId());



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

    @Transactional
    @Override
    public List<GroupRequestEntity> groupRequest(Long groupId, List<String> nick) {
        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        List<GroupRequestEntity> requests = nick.stream().map(nickName -> {
            MemberEntity member = memberRepository.findByNick(nickName);

            return GroupRequestEntity.builder()
                    .group(group)
                    .member(member)
                    .build();
        }).collect(Collectors.toList());

        return groupRequestRepository.saveAll(requests);
    }
    @Transactional
    @Override
    public void acceptGroupRequest(Long requestId) {
        securityContextManager.getAuthenticatedUserName();
        GroupRequestEntity request = groupRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        // 초대 요청을 수락 처리
        MemberGroupEntity memberGroup = MemberGroupEntity.builder()
                .group(request.getGroup())
                .member(request.getMember())
                .build();
        memberGroupRepository.save(memberGroup);

        // 그룹 멤버 수 업데이트
        GroupEntity group = request.getGroup();
        group.setTotal(group.getTotal() + 1);
        groupRepository.save(group);

        // 초대 요청 삭제
        groupRequestRepository.delete(request);
    }

    @Override
    public void rejectGroupRequest(Long requestId) {
        GroupRequestEntity request = groupRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        // 초대 요청 삭제
        groupRequestRepository.delete(request);
    }

    @Override
    public GroupDto getData(long groupId) {
        GroupEntity entity = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));
        return GroupDto.toDto(entity);
    }

    @Override
    public List<MemberEntity> searchMembersByNickName(String nick) {
        return memberRepository.searchByNick(nick).orElseThrow();
    }

    //그룹 탈퇴
    @Override
    public void leaveGroup(Long memberId, Long groupId) {
        if(!memberRepository.existsById(memberId)) {
            throw new ResourceNotFoundException("Member ID not found");
        }
        if(!groupRepository.existsById(groupId)) {
            throw new ResourceNotFoundException("Group ID not found");
        }
        memberGroupRepository.deleteByMemberIdAndGroupId(memberId, groupId);
    }

    @Override
    public GroupRequestDto getGroupRequestData(Long requestId) {
        GroupRequestEntity entity = groupRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + requestId));
        return GroupRequestDto.toDto(entity);
    }
}

