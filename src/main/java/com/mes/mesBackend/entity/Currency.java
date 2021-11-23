package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

/*
 * 화폐
 * 화폐 (KRW￦)
 * */
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "CURRENCIES")
@Data
public class Currency extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '화폐 고유아이디'")
    private Long id;

    @Column(name = "CURRENCY", nullable = false, columnDefinition = "varchar(255) COMMENT '화페'")
    private String currency;        // 화폐

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;  // 삭제여부

    public void update(Currency newCurrency) {
        setCurrency(newCurrency.currency);
    }

    public void delete() {
        setDeleteYn(true);
    }
}
