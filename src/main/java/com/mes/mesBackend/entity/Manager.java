package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
 * 담당자
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "MANAGERS")
@Data
public class Manager extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '담당자 고유아이디'")
    private Long id;

    @Column(name = "NAME", nullable = false, columnDefinition = "varchar(255) COMMENT '담당자명'")
    private String name;

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    @Column(name = "USE_YN", columnDefinition = "bit(1) COMMENT '사용여부'", nullable = false)
    private boolean useYn = true;       // 사용여부
}
