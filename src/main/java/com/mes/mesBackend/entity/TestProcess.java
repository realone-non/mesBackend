package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 검사방법
// TODO: 삭제
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "TEST_PROCESSES")
@Data
public class TestProcess extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '검사방법 고유아이디'")
    private Long id;

    @Column(name = "TEST_PROCESS", nullable = false, columnDefinition = "varchar(255) COMMENT '검사방법'")
    private String testProcess;

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean deleteYn;

    public void delete() {
        setDeleteYn(true);
    }

    public void put(TestProcess newTestProcess) {
        setTestProcess(newTestProcess.testProcess);
    }
}
