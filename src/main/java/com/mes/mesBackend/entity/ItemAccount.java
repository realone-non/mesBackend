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
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(name = "ACCOUNT", nullable = false)
    private String account;

    @Column(name = "USE_YN", nullable = false)
    private boolean useYn;

    @Column(name = "DELETE_YN")
    private boolean deleteYn = false;  // 삭제여부
}

