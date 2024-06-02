package com.be.whereu.service;

import com.be.whereu.model.dto.GroupDto;

import java.util.List;

public interface GroupService {

    public boolean createGroup(GroupDto groupDto);
    public List<GroupDto> getGroupList();
    public GroupDto groupDetails(Long groupId);
}
