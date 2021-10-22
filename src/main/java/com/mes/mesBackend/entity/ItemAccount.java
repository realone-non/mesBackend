package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
* 품목계정
* 품목계정 (원재료,부재료)
* */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "ITEM_ACCOUNTS")
@Data
public class ItemAccount extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '품목계정 고유아이디'")
    private Long id;

    @Column(name = "ACCOUNT", nullable = false, columnDefinition = "bigint COMMENT '품목계정'")
    private String account;

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bigint COMMENT '사용여부'")
    private Boolean useYn = true;      //  사용여부

    @Column(name = "DELETE_YN", columnDefinition = "bigint COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부
}

