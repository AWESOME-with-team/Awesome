package com.be.whereu.service;

import com.be.whereu.model.dto.GroupDto;
import com.be.whereu.model.dto.GroupRequestDto;
import com.be.whereu.model.entity.GroupRequestEntity;
import com.be.whereu.model.entity.MemberEntity;

import java.util.List;

public interface GroupService {

    public boolean createGroup(GroupDto groupDto);
    public List<GroupDto> getGroupList();
    public GroupDto groupDetails(Long groupId);
    List<GroupRequestEntity> groupRequest(Long groupId, List<String> nick);
    void acceptGroupRequest(Long requestId);
    void rejectGroupRequest(Long requestId);
    GroupDto getData(long groupId);
    GroupRequestDto getGroupRequestData(Long requestId);
    List<MemberEntity> searchMembersByNickName(String nick);
    void leaveGroup(Long memberId, Long groupId);
}
