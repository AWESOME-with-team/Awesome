package com.be.whereu.controller;

import com.be.whereu.exception.ResourceNotFoundException;
import com.be.whereu.model.dto.GroupDto;
<<<<<<< HEAD
=======
import com.be.whereu.model.dto.GroupRequestDto;
import com.be.whereu.model.entity.GroupRequestEntity;
import com.be.whereu.model.entity.MemberEntity;
>>>>>>> 3c133f643c2d0b824d587adc474f9bb60ad66b51
import com.be.whereu.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group")
public class GroupController {
    private final GroupService groupService;
<<<<<<< HEAD
=======
    private final SimpMessagingTemplate messagingTemplate;
>>>>>>> 3c133f643c2d0b824d587adc474f9bb60ad66b51

    /**
     * @param dto
     * @return 성공하면 200 실패하면 400
     */
    @PostMapping("/create")
    public ResponseEntity<Boolean> createGroup(@RequestBody GroupDto dto) {
        boolean isSuccess = groupService.createGroup(dto);
        if (isSuccess) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }

    @GetMapping("/list")
    public ResponseEntity<List<GroupDto>> getAllGroups() {

        List<GroupDto> groupList = groupService.getGroupList();
        return ResponseEntity.ok(groupList);

    }

    @GetMapping("/detail/{groupId}")
    public ResponseEntity<GroupDto> getGroupById(@PathVariable Long groupId) {

        GroupDto groupDto = groupService.groupDetails(groupId);
        return ResponseEntity.ok().body(groupDto);
    }

<<<<<<< HEAD
    /**
     * 
     * @param groupId
     * @param nick 
     * @return 그룹 초대 요청을 보내는 컨트롤러
     */
    @GetMapping("/join/request")
    public ResponseEntity<Boolean> joinRequest(@RequestParam Long groupId,@RequestParam String nick) {
        boolean isSuccess=groupService.addMemberInRequestGroup(groupId,nick);
        return ResponseEntity.ok(isSuccess);
    }


    /**
     * 
     * @return 유저가 받은 그룹 초대 요청 리스트
     */
    @GetMapping("/request/list")
    public ResponseEntity<List<GroupDto>> getRequestList() {
        List<GroupDto> dtoList = groupService.getRequestGroupListToMe();
        return ResponseEntity.ok(dtoList);
    }

    /**
     *
     * @param groupId
     * @return 그룹 요청 초대 수락 시 보내는 요청 
     */
    @GetMapping("/request/accept")
    public ResponseEntity<Boolean> acceptRequest(@RequestParam Long groupId) {
        boolean isSuccess =  groupService.deleteRequestAndJoinGroup(groupId);

        return ResponseEntity.ok(isSuccess);
    }
}
=======
    @MessageMapping("/request/{groupId}")
    @SendTo("/topic/invitation")
    public ResponseEntity<List<GroupRequestEntity>> groupRequest(@RequestBody GroupRequestDto groupRequestDto) {
        List<GroupRequestEntity> requests = groupService.groupRequest(groupRequestDto.getChatId(), groupRequestDto.getNickname());
        // 초대 요청에 대한 실시간 알림 전송
        String destination = "/topic/invitation/" + groupRequestDto.getChatId();
        messagingTemplate.convertAndSend(destination, "New invitation request for chatId: " + groupRequestDto.getChatId());

        return new ResponseEntity<>(requests, HttpStatus.OK);
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

    //초대할 멤버 검색
    @GetMapping("/members/search")
    public ResponseEntity<Optional<MemberEntity>> searchMembers(@RequestParam String nick) {
        if (nick == null || nick.trim().isEmpty()) {
            throw new ResourceNotFoundException("Nickname cannot be null or empty.");
        }

        Optional<MemberEntity> members = groupService.searchMembersByNickName(nick);
        if (members.isPresent()) {
            return new ResponseEntity<>(members, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Optional.empty(), HttpStatus.NOT_FOUND);
        }
    }

    //그룹 탈퇴
    @DeleteMapping("{groupId}/leave/{memberId}")
    public ResponseEntity<Void> leaveGroup(@PathVariable Long groupId, Long memberId) {
        groupService.leaveGroup(groupId, memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
>>>>>>> 3c133f643c2d0b824d587adc474f9bb60ad66b51
