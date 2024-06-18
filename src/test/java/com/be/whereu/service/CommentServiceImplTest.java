package com.be.whereu.service;

import com.be.whereu.model.dto.board.CommentRequestDto;
import com.be.whereu.model.dto.board.CommentListResponseDto;
import com.be.whereu.model.entity.CommentEntity;
import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.repository.CommentRepository;
import com.be.whereu.repository.MemberRepository;
import com.be.whereu.security.authentication.SecurityContextManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private SecurityContextManager securityContextManager;

    @InjectMocks
    private CommentServiceImpl commentServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("새로운 댓글 등록 성공")
    void addComment() {
        // 테스트에 필요한 데이터 설정
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setContent("Test comment");

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(1L);

        // Mock 설정
        when(securityContextManager.getAuthenticatedUserName()).thenReturn("1");
        when(memberRepository.findById(1L)).thenReturn(Optional.of(memberEntity));
        when(commentRepository.save(any(CommentEntity.class))).thenAnswer(invocation -> {
            CommentEntity commentEntity = invocation.getArgument(0);
            commentEntity.setId(1L);
            return commentEntity;
        });

        // 테스트 메서드 호출
        CommentListResponseDto result = commentServiceImpl.addComment(commentRequestDto);

        // 결과 검증
        assertNotNull(result, "결과는 null이 아니어야 합니다.");
        assertEquals("Test comment", result.getContent(), "댓글 내용이 일치해야 합니다.");
    }

    @Test
    @DisplayName("댓글 등록 실패 시 null 반환")
    void addComment_ShouldReturnNull() {
        // 테스트에 필요한 데이터 설정
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setContent("Test comment");

        // Mock 설정
        when(securityContextManager.getAuthenticatedUserName()).thenReturn("1");
        when(memberRepository.findById(1L)).thenThrow(DataAccessException.class);

        // 테스트 메서드 호출
        CommentListResponseDto result = commentServiceImpl.addComment(commentRequestDto);

        // 결과 검증
        assertNull(result, "결과는 null이어야 합니다.");
    }
}