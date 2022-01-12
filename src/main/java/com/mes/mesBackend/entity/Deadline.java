package com.mes.mesBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PUBLIC;

/*
* 18-5. 마감일자
* */
@AllArgsConstructor
@NoArgsConstructor(access = PUBLIC)
@Entity(name = "DEADLINES")
@Data
public class Deadline extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '마감일자 고유아이디'")
    private Long id;

    // 일자
    @Column(name = "DEADLINE", columnDefinition = "date COMMENT '마감일자'", nullable = false)
    private LocalDate deadline;

    // 삭제여부
    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;

    public void delete() {
        setDeleteYn(true);
    }
}
