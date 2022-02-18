package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 용도 유형
// TODO 삭제
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "USE_TYPES")
@Data
public class UseType extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '용도유형 고유아이디'")
    private Long id;

    @Column(name = "USE_TYPE", nullable = false, columnDefinition = "varchar(255) COMMENT '용도유형'")
    private String useType;

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn;

    public void put(UseType newUseType) {
        setUseType(newUseType.useType);
    }
    public void delete() {
        setDeleteYn(true );
    }
}
