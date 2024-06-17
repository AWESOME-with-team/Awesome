package com.be.whereu.service;

import com.be.whereu.exception.CustomServiceException;
import com.be.whereu.exception.ResourceNotFoundException;
import com.be.whereu.model.dto.GroupDto;
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
                    .orElseThrow(() -> new ResourceNotFoundException("Group Id:" + groupId + " not found"));
            GroupDto groupDto = GroupDto.toDto(groupEntity);
            List<MemberDto> memberList = groupEntity.getMemberGroup().stream()
                    .map(MemberGroupEntity::getMember)
                    .map(MemberDto::toDto)
                    .toList();
            groupDto.setMembers(memberList);
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
    /**
     *
     * @param groupId
     * @param nick
     * @return 그룹 초대 받았을때 group_rquest_tbl 에 추가 하는 메소드
     */
    @Transactional
    @Override
    public boolean addMemberInRequestGroup(Long groupId, String nick) {
        try {
            MemberEntity memberEntity=memberRepository.findByNick(nick).orElseThrow(()-> new ResourceNotFoundException("no found userNick"+nick));
            //요청받은 목록에 이미 있는 맴버일 경우 방지
            if (groupRequestRepository.existsByGroupIdAndMemberId(groupId, memberEntity.getId())) {
                // 이미 존재하는 요청이 있을 경우 false 반환
                return false;
            }
            GroupEntity groupEntity=new GroupEntity();
            groupEntity.setId(groupId);
            var groupRequest =new GroupRequestEntity();
            groupRequest.setMember(memberEntity);
            groupRequest.setGroup(groupEntity);
            var entity = groupRequestRepository.save(groupRequest);
            return entity.getId() > 0;
        }catch (DataAccessException e) {
            log.error("Database error addMemberInRequestGroup: {} ", e.getMessage());
            throw new CustomServiceException("Failed to addMemberInRequestGroup due to database error", e);
        } catch (Exception e) {
            log.error("addMemberInRequestGroup occurred {}", e.getMessage());
            throw new CustomServiceException("Failed to addMemberInRequestGroup", e);
        }
    }

    @Override
    public List<GroupDto> getRequestGroupListToMe() {
        try {
            Long memberId = Long.parseLong(securityContextManager.getAuthenticatedUserName());
            List<GroupRequestEntity> entityList= groupRequestRepository.requestGroupList(memberId).orElseThrow(
                    ()->new ResourceNotFoundException("not found List")
            );


            return entityList.stream()
                    .map(groupRequestEntity -> GroupDto.toDto(groupRequestEntity.getGroup()))
                    .toList();
        }catch (DataAccessException e) {
            log.error("Database error getRequestGroupListToMe: {} ", e.getMessage());
            throw new CustomServiceException("Failed to getRequestGroupListToMe due to database error", e);
        } catch (Exception e) {
            log.error("getRequestGroupListToMe occurred {}", e.getMessage());
            throw new CustomServiceException("Failed to getRequestGroupListToMe", e);
        }
    }

    /**
     * 
     * @param groupId
     * @return group 요청 수락시 수행로직 요청을 목록을 지우고 채팅방과 그룹에 참여시킨다
     */
    @Transactional
    @Override
    public boolean deleteRequestAndJoinGroup(Long groupId) {
        try {
            boolean isSuccess = false;
            Long memberId = Long.parseLong(securityContextManager.getAuthenticatedUserName());

            // 특정 그룹 ID와 회원 ID를 기반으로 그룹 요청 엔티티 삭제
            groupRequestRepository.deleteByGroupRequestIdAndMemberId(groupId, memberId);

            // 그룹 엔티티를 ID로 조회, 없으면 예외 발생
            var groupEntity = groupRepository.findById(groupId).orElseThrow(
                    () -> new ResourceNotFoundException("No group found with id: " + groupId)
            );

            // 그룹의 총 인원 수 증가
            groupEntity.setTotal(groupEntity.getTotal() + 1);

            // 회원 엔티티 생성 및 설정
            var memberEntity = new MemberEntity();
            memberEntity.setId(memberId);

            // 회원-그룹 엔티티 생성 및 설정
            MemberGroupEntity memberGroupEntity = new MemberGroupEntity();
            memberGroupEntity.setMember(memberEntity);
            memberGroupEntity.setGroup(groupEntity);

            // 회원-그룹 엔티티 저장
            var memberGroup = memberGroupRepository.save(memberGroupEntity);

            // 그룹 채팅방에 참여
            chatService.addMemberGroupChat(memberId,groupId);


            // 저장된 엔티티의 ID가 null이 아닌지 확인하여 성공 여부 설정
            if (memberGroup.getId() != null) {
                isSuccess = true;
            }


            return isSuccess;
        }catch (DataAccessException e) {
            log.error("Database error deleteRequestAndJoinGroup: {} ", e.getMessage());
            throw new CustomServiceException("Failed to deleteRequestAndJoinGroup due to database error", e);
        } catch (Exception e) {
            log.error("deleteRequestAndJoinGroup occurred {}", e.getMessage());
            throw new CustomServiceException("Failed to deleteRequestAndJoinGroup", e);
        }
    }

}






