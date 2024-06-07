package com.be.whereu.controller;

import com.be.whereu.model.dto.GroupDto;
import com.be.whereu.service.ChatService;
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
    private final ChatService chatService;

    /**
     *
     * @param dto
     * @return 성공하면 200 실패하면 400
     */
    @PostMapping("/create")
    public ResponseEntity<Boolean> createGroup(@RequestBody GroupDto dto) {
        boolean isSuccess=groupService.createGroup(dto) ;
        if(isSuccess) {
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
}
