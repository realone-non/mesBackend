package com.mes.mesBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "ITEM_ACCOUNT_CODES")
@Data
public class ItemAccountCode extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '품목계정코드 고유아이디'")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ITEM_ACCOUNT", columnDefinition = "bigint COMMENT '품목계정'", nullable = false)
    private ItemAccount itemAccount;

    @Column(name = "DETAIL", columnDefinition = "varchar(255) COMMENT '분류'", nullable = false)
    private String detail;

    @Column(name = "CODE", columnDefinition = "varchar(15) COMMENT '기호'", nullable = false)
    private String code;

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;
}
