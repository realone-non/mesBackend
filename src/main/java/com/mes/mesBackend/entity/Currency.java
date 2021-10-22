package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
 * 화폐
 * 화폐 (KRW￦)
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "CURRENCIES")
@Data
public class Currency extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '화폐 고유아이디'")
    private Long id;

    @Column(name = "CURRENCY", nullable = false, columnDefinition = "bigint COMMENT '화페'")
    private String currency;        // 화폐

    @Column(name = "USE_YN", columnDefinition = "bigint COMMENT '사용여부'")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN", columnDefinition = "bigint COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부
}
