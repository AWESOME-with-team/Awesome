package com.be.whereu.service;

import com.be.whereu.exception.ResourceNotFoundException;
import com.be.whereu.model.dto.ComplaintCommentRequestDto;
import com.be.whereu.model.dto.ComplaintCommentResponseDto;
import com.be.whereu.model.dto.ComplaintPostRequestDto;
import com.be.whereu.model.dto.ComplaintPostResponseDto;
import com.be.whereu.model.dto.board.ScrapAndSaveListDto;
import com.be.whereu.model.entity.ComplaintEntity;
import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.repository.ComplaintRepository;
import com.be.whereu.repository.MemberRepository;
import com.be.whereu.security.authentication.SecurityContextManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final SecurityContextManager securityContextManager;
    private final MemberRepository memberRepository;

    private static final int COMPLAINT_POST_PAGE_SIZE = 30;
    private static final int COMPLAINT_COMMENT_PAGE_SIZE = 30;

    //게시글 신고
    @Transactional
    @Override
    public ComplaintPostResponseDto complaintPost(ComplaintPostRequestDto requestDto) {
        ComplaintEntity complaint = ComplaintEntity.toEntity(requestDto);

        Optional<MemberEntity> member = memberRepository.findById(Long.parseLong(securityContextManager.getAuthenticatedUserName()));

        if (complaintRepository.existsByMemberIdAndPostId(member.stream().count(), requestDto.getPostId())) {
            log.info("게시글을 이미 신고 하였습니다.");
            throw new IllegalArgumentException("You have already complaint this post");
        }

        if (member.isPresent()) {
            MemberEntity memberEntity = member.get();
            complaint.setComplaintBy(memberEntity);
        } else {
            throw new ResourceNotFoundException("Member not found");
        }
        complaintRepository.save(complaint);
        return null;
    }

    //댓글 신고
    @Override
    @Transactional
    public ComplaintCommentResponseDto complaintComment(ComplaintCommentRequestDto requestDto) {
        ComplaintEntity complaint = ComplaintEntity.toEntityComment(requestDto);

        Optional<MemberEntity> member = memberRepository.findById(Long.parseLong(securityContextManager.getAuthenticatedUserName()));

        if (complaintRepository.existsByMemberIdAndCommentId(member.stream().count(), requestDto.getCommentId())) {
            log.info("댓글을 이미 신고 하였습니다.");
            throw new IllegalArgumentException("You have already complaint this comment");
        }

        if (member.isPresent()) {
            MemberEntity memberEntity = member.get();
            assert complaint != null;
            complaint.setComplaintBy(memberEntity);
        } else {
            throw new ResourceNotFoundException("Member not found");
        }
        complaintRepository.save(complaint);
        return null;
    }

    //신고된 게시물 리스트
    public List<ComplaintPostResponseDto> getAllComplaintPosts(int pageNum) {

        Pageable pageable = PageRequest.of(pageNum, COMPLAINT_POST_PAGE_SIZE, Sort.by("id").descending());
        Page<ComplaintEntity> complaints = complaintRepository.findAllByPostIsNotNull(pageable);

        return complaints.stream()
                .map(item -> {
                            ComplaintPostResponseDto c = ComplaintPostResponseDto.toDto(item);
                            c.setNickByComplaint(item.getComplaintBy().getNick());
                            c.setWriterByPost(item.getPost().getMember().getNick());
                            return c;
                        }
                )
                .collect(Collectors.toList());
    }

    //신고된 댓글 리스트
    public List<ComplaintCommentResponseDto> getAllComplaintComments(int pageNum) {

        Pageable pageable = PageRequest.of(pageNum, COMPLAINT_POST_PAGE_SIZE, Sort.by("id").descending());
        Page<ComplaintEntity> complaints = complaintRepository.findAllByCommentIsNotNull(pageable);

        return complaints.stream()
                .map(item -> {
                            ComplaintCommentResponseDto c = ComplaintCommentResponseDto.toDto(item);
                            c.setNickByComplaint(item.getComplaintBy().getNick());
                            c.setWriterByComment(item.getComment().getMember().getNick());
                            return c;
                        }
                )
                .collect(Collectors.toList());
    }
}
