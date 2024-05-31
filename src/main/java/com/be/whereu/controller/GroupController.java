package com.be.whereu.controller;

import com.be.whereu.model.dto.GroupDto;
import com.be.whereu.model.dto.GroupRequestDto;
import com.be.whereu.model.entity.GroupEntity;
import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.repository.MemberRepository;
import com.be.whereu.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/group")
public class GroupController {
    private final GroupService groupService;
    private final MemberRepository memberRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/create")
    public ResponseEntity<GroupEntity> createGroup(@RequestBody GroupDto groupDto) {
        MemberEntity creator = memberRepository.findById(groupDto.getHostId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        GroupEntity group = groupService.createGroup(groupDto);
        return new ResponseEntity<>(group, HttpStatus.CREATED);
    }

    @MessageMapping("/request/{groupId}")
    @SendTo("/topic/invitation")
    public ResponseEntity<Void> groupRequest(@RequestBody GroupRequestDto groupRequestDto) {
        groupService.groupRequest(groupRequestDto.getChatId(), groupRequestDto.getNickname());

        // 초대 요청에 대한 실시간 알림 전송
        String destination = "/topic/invitation/" + groupRequestDto.getChatId();
        messagingTemplate.convertAndSend(destination, "New invitation request for chatId: " + groupRequestDto.getChatId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //초대 수락
    @PostMapping("/accept")
    public ResponseEntity<Void> acceptGroupRequest(@RequestParam Long requestId) {
        groupService.acceptGroupRequest(requestId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 초대 거절
    @PostMapping("/reject")
    public ResponseEntity<Void> rejectGroupRequest(@RequestParam Long requestId) {
        groupService.rejectGroupRequest(requestId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //그룹이름으로 그룹 찾기
    @GetMapping("/search")
    public ResponseEntity<GroupEntity> findByGroupName(@RequestParam String groupName) {
        GroupEntity group = groupService.findByGroupName(groupName);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    //그룹 채팅방 찾기
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupEntity> findByChatId(@PathVariable Long groupId) {
        if(groupId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<GroupEntity> group = groupService.findByGroupId(groupId);
        return group.map(entity -> new ResponseEntity<>(entity, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //초대할 멤버 검색
    @GetMapping("/members/search")
    public ResponseEntity<Optional<MemberEntity>> searchMembers(@RequestParam String nick) {
        if(nick == null || nick.isEmpty()) {
            throw new RuntimeException("Nickname cannot be empty");
        }
        Optional<MemberEntity> members = groupService.searchMembersByNickName(nick);
        if(members.isPresent()) {
            return new ResponseEntity<>(members, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(Optional.empty(), HttpStatus.NOT_FOUND);
        }
    }
}