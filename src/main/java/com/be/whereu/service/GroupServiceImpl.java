package com.be.whereu.service;

import com.be.whereu.model.dto.GroupDto;
import com.be.whereu.model.dto.GroupRequestDto;
import com.be.whereu.model.entity.GroupEntity;
import com.be.whereu.model.entity.GroupRequestEntity;
import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.model.entity.MemberGroupEntity;
import com.be.whereu.repository.GroupRepository;
import com.be.whereu.repository.GroupRequestRepository;
import com.be.whereu.repository.MemberGroupRepository;
import com.be.whereu.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final GroupRequestRepository groupRequestRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public GroupEntity createGroup(GroupDto groupDto) {
        //그룹 생성
        GroupEntity group = GroupEntity.builder()
                .groupName(groupDto.getGroupName())
                .groupLoc(groupDto.getGroupLoc())
                .gender(groupDto.getGender())
                .hostId(groupDto.getHostId())
                .total(1)
                .build();
        groupRepository.save(group);

        return group;
    }

    @Transactional
    @Override
    public void groupRequest(Long groupId, List<String> nick) {
        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        for (String nickName : nick) {
            MemberEntity member = memberRepository.findByNick(nickName);

            // 초대 요청 생성
            GroupRequestEntity request = GroupRequestEntity.builder()
                    .group(group)
                    .member(member)
                    .build();
            groupRequestRepository.save(request);
        }
    }

    @Override
    public void acceptGroupRequest(Long requestId) {
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
    public Optional<GroupEntity> findByGroupId(Long groupId) {
        return groupRepository.findById(groupId);
    }

    @Override
    public GroupEntity findByGroupName(String groupName) {
        return groupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new RuntimeException("Group not found"));
    }

    @Override
    public GroupDto getData(long groupId) {
        GroupEntity entity = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));
        return GroupDto.toDto(entity);
    }

    @Override
    public Optional<MemberEntity> searchMembersByNickName(String nick) {
        return Optional.ofNullable(memberRepository.findByNick(nick));
    }

    @Override
    public GroupRequestDto getGroupRequestData(Long requestId) {
        GroupRequestEntity entity = groupRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + requestId));
        return GroupRequestDto.toDto(entity);
    }
}