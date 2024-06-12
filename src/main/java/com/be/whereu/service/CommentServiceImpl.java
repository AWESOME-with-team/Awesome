package com.be.whereu.service;

import com.be.whereu.exception.ResourceNotFoundException;
import com.be.whereu.model.dto.CommentDto;
import com.be.whereu.model.entity.CommentEntity;
import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.model.entity.PostEntity;
import com.be.whereu.repository.CommentRepository;
import com.be.whereu.repository.MemberRepository;
import com.be.whereu.repository.PostRepository;
import com.be.whereu.security.authentication.SecurityContextManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final SecurityContextManager securityContextManager;

    @Override
    public List<CommentDto> getCommentList(Long postId) {
        List<CommentEntity> commentList = commentRepository.findByPostId(postId);
        log.info("commentList: {}", commentList);
        return commentList.stream().map(CommentDto::toDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(CommentDto commentDto) {
        PostEntity post = postRepository.findById(commentDto.getPost().getId())
                .orElseThrow(()-> new ResourceNotFoundException("Post not found"));
        MemberEntity member = memberRepository.findById(Long.parseLong(securityContextManager.getAuthenticatedUserName()))
                .orElseThrow(()-> new ResourceNotFoundException("Member not found"));

        CommentEntity parentComment = null;
        if(commentDto.getParent() != null){
            parentComment = commentRepository.findById(commentDto.getParent().getId())
                    .orElseThrow(()-> new ResourceNotFoundException("Parent not found"));
        }

        CommentEntity commentEntity = CommentEntity.builder()
                .post(post)
                .member(member)
                .content(commentDto.getContent())
                .parent(parentComment)
                .build();

        CommentEntity saveComment = commentRepository.save(commentEntity);
        return CommentDto.toDto(saveComment);
    }

    @Override
    public CommentDto updateComment(CommentDto commentDto) {
        CommentEntity commentEntity = commentRepository.findById(commentDto.getId())
                .orElseThrow(()-> new ResourceNotFoundException("not found comment ID"));
        commentEntity.setContent(commentDto.getContent());

        CommentEntity updateComment = commentRepository.save(commentEntity);
        log.info("entityToDto: {} ",CommentDto.toDto(updateComment));
        return CommentDto.toDto(updateComment);
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CommentDto likeComment(Long id) {
        CommentEntity commentEntity = commentRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("not found comment ID"));
        commentEntity.setLikeCount(commentEntity.getLikeCount() + 1);
        return CommentDto.toDto(commentEntity);
    }

    @Override
    @Transactional
    public CommentDto unlikeComment(Long id) {
        CommentEntity commentEntity = commentRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("not found comment ID"));
        commentEntity.setLikeCount(commentEntity.getLikeCount() - 1);
        return CommentDto.toDto(commentEntity);
    }

}
