package com.be.whereu.model.entity;

import com.be.whereu.model.entity.test.TestEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestPropertySource("classpath:application-test.yml")
@SpringBootTest
public class BaseEntityTest {


    @Test
    public void 주어진_새로운_엔티티_생성시_생성일과_수정일이_올바르게_설정됨() {
        // Given: 새로운 엔티티가 생성됨
        TestEntity testEntity = new TestEntity();

        // When: 생성 날짜를 포맷팅함
        testEntity.formattingBeforeCreateDate();

        // Then: 생성 시간과 수정 시간이 현재 시간보다 이전으로 설정됨
        LocalDateTime now = LocalDateTime.now();
        System.out.println("테스중입니다");
        assertAll(
                "생성일과 수정일 검증",
                () -> assertThat(testEntity.getCreateAt()).isNotNull(),
                () -> assertThat(testEntity.getModifiedAt()).isNotNull(),
                () -> assertThat(testEntity.getCreateAt()).isBefore(now.plusSeconds(1)),
                () -> assertThat(testEntity.getModifiedAt()).isBefore(now.plusSeconds(1))
        );
    }

    @Test
    public void 이미_존재하는_엔티티_수정시_수정일이_업데이트됨() throws InterruptedException {
        // Given: 이미 생성된 엔티티가 존재함
        TestEntity testEntity = new TestEntity();
        testEntity.formattingBeforeCreateDate(); // Ensure initial values are set



        // When: 엔티티를 수정함
        Thread.sleep(1000);
        testEntity.formattingBeforeModifiedDate();

        // Then: 수정일이 변경되었는지 확인
        assertAll(
                "수정일 검증",
                () -> assertThat(testEntity.getModifiedAt()).isNotNull(),
                () -> assertThat(testEntity.getModifiedAt()).isAfter(testEntity.getCreateAt())
        );
    }
}