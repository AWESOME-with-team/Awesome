package com.be.whereu.service;

import com.be.whereu.model.dto.board.PostResponseDto;
import com.be.whereu.model.entity.CommonEntity;
import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.model.entity.PostEntity;
import com.be.whereu.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
class PostServiceImplTest {
    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postService;

    private List<PostEntity> postEntities;


    @BeforeEach
    void setUp() {
         CommonEntity commonEntity = new CommonEntity();
         commonEntity.setCodeId(1001L);

         MemberEntity memberEntity= new MemberEntity();
         memberEntity.setId(1L);

         postEntities = new ArrayList<>();
         PostEntity post1= new PostEntity(1L,commonEntity,
                 "post1","content1",memberEntity,
                 0,0,null);
         PostEntity post2=new PostEntity(2L,commonEntity,
                 "post2","content2",memberEntity,
                 0,0,null);

         postEntities.add(post1);
         postEntities.add(post2);

    }

    @Test
    void testGetPostListWIthoutId() {
        Pageable pageable= PageRequest.of(0,10);
        Page<PostEntity> postPage= new PageImpl<>(postEntities,pageable,postEntities.size());

        when(postRepository.findAll(pageable)).thenReturn(postPage);

        List<PostResponseDto> result= postService.getPostList(null,0);
        assertEquals(2,result.size());
        assertEquals("Title 1",result.get(0).getTitle());

    }

    @Test
    void testGetPostListWithoutId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<PostEntity> postPage = new PageImpl<>(postEntities, pageable, postEntities.size());

        CommonEntity commonEntity = new CommonEntity();
        commonEntity.setCodeId(1L);

        when(postRepository.findByCommonOrderByIdDesc(commonEntity, pageable)).thenReturn(postPage);

        List<PostResponseDto> result = postService.getPostList(1L, 0);

        assertEquals(2, result.size());
        assertEquals("Title 1", result.get(0).getTitle());
    }
}