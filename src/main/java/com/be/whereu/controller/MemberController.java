package com.be.whereu.controller;


import com.be.whereu.model.dto.MemberDto;
import com.be.whereu.service.MemberSerivce;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberSerivce memberSerivce;


    @PostMapping("/member/update")
    public ResponseEntity<String> addMember(@RequestHeader("access-token") String accessJws, @RequestBody MemberDto memberDto) throws IOException {
        System.out.println("birthday ="+ memberDto.getBirth());
        System.out.println("university ="+ memberDto.getUniversityName());

        String newAccessJws =memberSerivce.updateAndGiveNewAccessToken(memberDto,accessJws);
        log.info("newAccessJws: {}", newAccessJws);
        if(newAccessJws !=null){
            log.info("newAccessJws: {}", newAccessJws);
            return ResponseEntity.ok().body(newAccessJws);
        };
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
    }

    /**
     *
     * @param nick
     * @return nick 존재하면 true 존재하지 않으면 false
     */
    @GetMapping("/member/nick")
    public ResponseEntity<Boolean> getMemberByNick(@RequestParam String nick) {

        Boolean isNickExist = memberSerivce.findMemberNick(nick);
        log.info("isNickExist: {}", isNickExist);
        return  ResponseEntity.ok().body(isNickExist);
    }

    @GetMapping("/member/search/{nick}/{groupId}")
    public  ResponseEntity<List<String>> getNickList(@PathVariable String nick , @PathVariable Long groupId) {
        System.out.println("nick ="+ nick);
        List<String> result = memberSerivce.searchNickListByGroupId(nick ,groupId);
        return ResponseEntity.ok(result);
    }

    /**
     * member 삭제 (회원 탈퇴)
     * @return
     */
    @DeleteMapping("/member/leave")
    public ResponseEntity<String> leaveMember() {
        boolean isSuccess = memberSerivce.deleteMember();
        if(isSuccess){
            return ResponseEntity.ok().body("success");
        }else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }

    }


}
