package com.be.whereu.service;

import com.be.whereu.model.dto.MemberDto;

import java.util.List;


public interface MemberSerivce {
    public String updateAndGiveNewAccessToken(MemberDto dto, String access_token);
    public MemberDto getData(long memberId);
    public Boolean findMemberNick(String nick);
    public List<String> searchNickListByGroupId(String nick , Long groupId);
}
