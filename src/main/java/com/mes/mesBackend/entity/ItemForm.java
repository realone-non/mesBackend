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

// 품목형태
@AllArgsConstructor
@NoArgsConstructor(access = PUBLIC)
@Entity(name = "ITEM_FORMS")
@Data
public class ItemForm extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '품목형태 고유아이디'")
    private Long id;

    @Column(name = "FORM", nullable = false, columnDefinition = "varchar(255) COMMENT '품목형태'")
    private String form;

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;

    public void delete() {
        setDeleteYn(true);
    }

    public void put(ItemForm newItemForm) {
        setForm(newItemForm.form);
    }
}