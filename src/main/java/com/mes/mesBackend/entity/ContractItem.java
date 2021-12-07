package com.mes.mesBackend.entity;

import com.mes.mesBackend.entity.enumeration.ContractType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

/*
 * 수주 품목 리스트
 * 품번 (AA01-MC2-E003B)       ->Item
 * 품명 (EMI FILTER)           -> Item
 * 규격 (MC2-E003B)            -> Item
 * 수주단위 (개)                  -> Unit
 * 수주수량 (10)
 * 단가 (1960000)
 * 수주금액 (19600000)
 * 수주금액(원화) (19600000)
 * 부가세 (1960000)
 * 수주유형 (방산)
 * 납기일자 (2021.10.5)
 * 고객발주번호 (AD-P2-210712-004)     -> Contract
 * 규격화 품번 ()
 * 비고
 * 첨부파일 (2 Files)       -> 미구현
 * */
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "CONTRACT_ITEMS")
@Data
public class ContractItem extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '수주 품목 리스트 고유아이디'")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ITEM", nullable = false, columnDefinition = "bigint COMMENT '품목'")
    private Item item;      // 품번, 품명, 규격, 수주단위, 단가

    @Column(name = "AMOUNT", nullable = false, columnDefinition = "int COMMENT '수주수량'")
    private int amount;     // 수주수량

    @Enumerated(STRING)
    @Column(name = "CONTRACT_TYPE", columnDefinition = "varchar(255) COMMENT '수주유형'", nullable = false)
    private ContractType contractType;

    @Column(name = "PERIOD_DATE", columnDefinition = "date COMMENT '납기일자'", nullable = false)
    private LocalDate periodDate;       // 납기일자

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CONTRACT", nullable = false, columnDefinition = "bigint COMMENT '수주'")
    private Contract contract;      // 수주

    @Column(name = "STANDART_ITEM_NO", columnDefinition = "varchar(255) COMMENT '규격화 품번'")
    private String standardItemNo;      // 규격화 품번

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;        // 비고

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;  // 삭제여부


}
