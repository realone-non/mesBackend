package com.mes.mesBackend.entity;

import lombok.*;

import javax.persistence.*;

/*
 * 점검 유형
 * 점검유형(일,월,분기)
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "CHECK_TYPES")
@Data
public class CheckType extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '점검유형 고유아이디'")
    private Long id;

    @Column(name = "CHECK_TYPE", columnDefinition = "varchar(255) COMMENT '점검유형'")
    private String checkType;       // 점검유형

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private Boolean useYn = true;      //  사용여부

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부
}
