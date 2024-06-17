package com.be.whereu.controller;

import com.be.whereu.model.dto.GroupDto;
import com.be.whereu.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group")
public class GroupController {
    private final GroupService groupService;

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


