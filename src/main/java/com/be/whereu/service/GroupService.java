package com.be.whereu.service;

import com.be.whereu.model.dto.GroupDto;
import com.be.whereu.model.dto.GroupRequestDto;
import com.be.whereu.model.entity.GroupEntity;
import com.be.whereu.model.entity.MemberEntity;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    GroupEntity createGroup(GroupDto groupDto);
    void groupRequest(Long groupId, List<String> nick);
    void acceptGroupRequest(Long requestId);
    void rejectGroupRequest(Long requestId);
    Optional<GroupEntity> findByGroupId(Long groupId);
    GroupEntity findByGroupName(String groupName);
    GroupDto getData(long groupId);
    Optional<MemberEntity> searchMembersByNickName(String nick);
    GroupRequestDto getGroupRequestData(Long requestId);

}
