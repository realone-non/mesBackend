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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '품목형태 고유아이디'")
    private Long id;

    @Column(name = "FORM", nullable = false, columnDefinition = "varchar(255) COMMENT '품목형태'")
    private String form;

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn;

    public void delete() {
        setDeleteYn(true);
    }

    public void put(ItemForm newItemForm) {
        setForm(newItemForm.form);
    }
}