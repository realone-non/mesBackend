package com.mes.mesBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PUBLIC;

// 결제조건
// 현금, 귀사 정기 결제, 발주전 100% 현금, 납품전 100% 현긍 등..
@AllArgsConstructor
@NoArgsConstructor(access = PUBLIC)
@Entity(name = "PAY_TYPES")
@Data
public class PayType extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '결제조건 고유아이디'")
    private Long id;

    @Column(name = "PAY_TYPE", columnDefinition = "varchar(255) COMMENT '결제조건'", nullable = false)
    private String payType;

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;

    public void delete() {
        setDeleteYn(true);
    }
}
