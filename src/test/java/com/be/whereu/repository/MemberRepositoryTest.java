package com.be.whereu.repository;

import com.be.whereu.model.Gender;
import com.be.whereu.model.entity.GroupEntity;
import com.be.whereu.model.entity.GroupRequestEntity;
import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.model.entity.MemberGroupEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@DisplayName("MemberRepository 맴버 검색시 다양한 조건 테스트")
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setUp() {
        GroupEntity group1 = new GroupEntity();
        group1.setGender(Gender.M);
        group1.setGroupName("Group1");
        entityManager.persist(group1);

        GroupEntity group2 = new GroupEntity();
        group2.setGender(Gender.F);
        group2.setGroupName("Group2");
        entityManager.persist(group2);

        MemberEntity member1 = new MemberEntity();
        member1.setNick("John");
        member1.setGender(Gender.M);
        entityManager.persist(member1);

        MemberEntity member2 = new MemberEntity();
        member2.setNick("Jane");
        member2.setGender(Gender.F);
        entityManager.persist(member2);

        MemberGroupEntity memberGroup1 = new MemberGroupEntity();
        memberGroup1.setMember(member1);
        memberGroup1.setGroup(group1);
        entityManager.persist(memberGroup1);

        GroupRequestEntity groupRequest1 = new GroupRequestEntity();
        groupRequest1.setMember(member2);
        groupRequest1.setGroup(group1);
        entityManager.persist(groupRequest1);
    }

    @Test
    public void 테스트_성별이_다른_경우() {
        // Given
        String nick = "Jane";
        Long groupId = 1L;

        // When
        Optional<List<MemberEntity>> result = memberRepository.findMemberListExcludingGroupMemberAndAnotherGenderAndAlreadyRequestedList(nick, groupId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEmpty(); // 다른 성별이므로 결과 없음
    }

    @Test
    public void 테스트_이미_그룹에_있는_경우() {
        // Given
        String nick = "John";
        Long groupId = 1L;

        // When
        Optional<List<MemberEntity>> result = memberRepository.findMemberListExcludingGroupMemberAndAnotherGenderAndAlreadyRequestedList(nick, groupId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEmpty(); // 이미 그룹에 있으므로 결과 없음
    }

    @Test
    public void 테스트_그룹_요청이_없는_경우() {
        // Given
        GroupRequestEntity groupRequest = new GroupRequestEntity();
        groupRequest.setGroup(entityManager.find(GroupEntity.class, 1L));
        groupRequest.setMember(entityManager.find(MemberEntity.class, 1L));
        entityManager.persist(groupRequest);

        String nick = "John";
        Long groupId = 1L;

        // When
        Optional<List<MemberEntity>> result = memberRepository.findMemberListExcludingGroupMemberAndAnotherGenderAndAlreadyRequestedList(nick, groupId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEmpty(); // 그룹 요청이 없으므로 결과 없음
    }

    @Test
    public void 테스트_조회가_되는_경우() {
        // Given
        MemberEntity member3 = new MemberEntity();
        member3.setNick("김");
        member3.setGender(Gender.M);
        entityManager.persist(member3);

        String nick = "김";
        Long groupId = 1L;

        // When
        Optional<List<MemberEntity>> result = memberRepository.findMemberListExcludingGroupMemberAndAnotherGenderAndAlreadyRequestedList(nick, groupId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).hasSize(1); // member3 should be the only match
        assertThat(result.get().get(0).getNick()).isEqualTo("김");
    }
}
