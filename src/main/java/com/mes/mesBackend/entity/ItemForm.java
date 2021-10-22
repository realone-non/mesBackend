package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 품목형태
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "ITEM_FORMS")
@Data
public class ItemForm extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '품목형태 고유아이디'")
    private Long id;

    @Column(name = "FORM", nullable = false, columnDefinition = "bigint COMMENT '품목형태'")
    private String form;

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bigint COMMENT '사용여부'")
    private boolean useYn;
}