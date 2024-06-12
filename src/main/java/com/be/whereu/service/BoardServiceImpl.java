package com.be.whereu.service;


import com.be.whereu.model.dto.board.BoardListDto;
import com.be.whereu.model.dto.board.PostDto;
import com.be.whereu.model.entity.CommonEntity;
import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.model.entity.PostEntity;
import com.be.whereu.repository.BoardRepository;
import com.be.whereu.repository.CommonRepository;
import com.be.whereu.repository.MemberRepository;
import com.be.whereu.security.authentication.SecurityContextManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final SecurityContextManager securityContextManager;
    private final CommonRepository commonRepository;


    private static final int PAGE_SIZE = 10;

    //게시물 등록
    @Transactional
    @Override
    public boolean insertPost(PostDto dto) {

        try {
            PostEntity post = PostEntity.toPostEntity(dto);
            Optional<MemberEntity> member = memberRepository.findById(Long.parseLong(securityContextManager.getAuthenticatedUserName()));
            if (member.isPresent()) {
                MemberEntity memberEntity = member.get();
                post.setMember(memberEntity);
            } else {
                log.error("no member found");
            }


            boardRepository.save(post);
            return true;
        } catch (DataAccessException e) {
            log.error("DataBase access error", e);
            return false;
        } catch (Exception e) {
            log.error("An unexpected error", e);
            return false;
        }


    }

    @Transactional
    @Override
    public boolean updatePost(PostDto dto) {
        try {
            PostEntity post = PostEntity.toPostEntity(dto);
            Optional<MemberEntity> member = memberRepository.findById(Long.parseLong(securityContextManager.getAuthenticatedUserName()));
            if (member.isPresent()) {
                MemberEntity memberEntity = member.get();
                post.setMember(memberEntity);
            } else {
                log.error("no member found");
            }

            boardRepository.save(post);
            return true;
        } catch (DataAccessException e) {
            log.error("DataBase access error", e);
            return false;
        } catch (Exception e) {
            log.error("An unexpected error", e);
            return false;
        }
    }
    //게시물 삭제
    @Override
    public boolean deletePost(PostDto dto) {
        try {
            boardRepository.deleteById(dto.getId());
            return true;
        } catch (DataAccessException e) {
            log.error("DataBase access error", e);
            return false;
        } catch (Exception e) {
            log.error("An unexpected error", e);
            return false;
        }
    }


    //게시물 가져오기
    @Transactional
    @Override
    public PostDto getPost(PostDto dto) {
        PostDto result = new PostDto();

        try {
            Optional<PostEntity> PostEntity=boardRepository.findById(dto.getId());
            System.out.println();
            if (PostEntity.isPresent()) {
                PostDto postDto=PostDto.toDto(PostEntity.get());
                MemberEntity memberEntity=PostEntity.get().getMember();
                postDto.setNick(memberEntity.getNick());
                postDto.setSchool(memberEntity.getUniversityName());
                postDto.CreatedDate(PostEntity.get().getCreateAt());
                postDto.ModifiedDate(PostEntity.get().getModifiedAt());
                postDto.setCommonId(PostEntity.get().getCommon().getCodeId());

                //postDto.setCommentList("로직 필요");
                result=postDto;
            }else{
                log.error("no Post found");

            }
        }catch (DataAccessException e){
            log.error("DataBase access error", e);
        }catch (Exception e){
            log.error("An unexpected error", e);
        }



        return result;
    }

    @Transactional
    @Override
    public List<PostDto> getPostList(PostDto dto, int pageNumber) {

        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.by("id").descending());
        List<PostDto> postDtoList = new ArrayList<>();

        try {
            if (dto.getCommonId() == 0) { // commonId가 0이면 모든 게시물을 가져옴
                Page<PostEntity> postEntities = boardRepository.findAll(pageable);
                for (PostEntity postEntity : postEntities) {
                    PostDto postDto = PostDto.toDto(postEntity);
                    if(postDto.getContent().length()>50){
                        String content = postDto.getContent().substring(0, 50)+"...";
                        postDto.setContent(content);
                    }
                    MemberEntity memberEntity = postEntity.getMember();
                    postDto.setNick(memberEntity.getNick());
                    postDto.setSchool(memberEntity.getUniversityName());
                    postDto.CreatedDate(postEntity.getCreateAt());
                    postDto.ModifiedDate(postEntity.getModifiedAt());
                    postDto.setCommonId(postEntity.getCommon().getCodeId());
                    postDtoList.add(postDto);
                }
            } else { // commonId가 0이 아니면 해당 commonId에 해당하는 게시물만 가져옴
                CommonEntity commonEntity = new CommonEntity();
                commonEntity.setCodeId(dto.getCommonId());
                Page<PostEntity> postEntities = boardRepository.findByCommonOrderByIdDesc(commonEntity, pageable);
                for (PostEntity postEntity : postEntities) {
                    PostDto postDto = PostDto.toDto(postEntity);
                    if(postDto.getContent().length()>50){
                        String content = postDto.getContent().substring(0, 50)+"...";
                        postDto.setContent(content);
                    }
                    MemberEntity memberEntity = postEntity.getMember();
                    postDto.setNick(memberEntity.getNick());
                    postDto.setSchool(memberEntity.getUniversityName());
                    postDto.CreatedDate(postEntity.getCreateAt());
                    postDto.ModifiedDate(postEntity.getModifiedAt());
                    postDto.setCommonId(postEntity.getCommon().getCodeId());
                    postDtoList.add(postDto);
                }
            }
        } catch (DataAccessException e) {
            log.error("DataBase access error", e);
        } catch (Exception e) {
            log.error("An unexpected error", e);
        }


        return postDtoList;
    }
    //게시판 List
    @Transactional
    @Override
    public List<BoardListDto> getBoardList(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 5, Sort.by("id").descending());
        try {
            List<BoardListDto> result = new ArrayList<>();
            for (long i = 1001L; i <= 1003L; i++) {
                CommonEntity commonEntity = commonRepository.findById(i)
                        .orElseThrow(() -> new IllegalArgumentException("Not Found CommonData"));
                List<BoardListDto> list = boardRepository.findByCommonOrderByIdDesc(commonEntity,pageable).stream()
                        .map(BoardListDto::fromEntity)
                        .toList();
                result.add(list.get(0));
            }

            return result;

        }catch (DataAccessException e){
            log.error("DataBase access error",e);
            return Collections.emptyList();  //빈리스트 반환
        }catch (Exception e){
            log.error("An unexpected error",e);
            return Collections.emptyList();
        }





    }


}
