package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 거래처 - 업태
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "BUSINESS_TYPES")
@Data
public class BusinessType extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "USE_YN", nullable = false)
    private boolean useYn = true;  // 사용여부

    @Column(name = "DELETE_YN")
    private boolean deleteYn = false;  // 삭제여부

    // 수정 매핑
    public void update(BusinessType newBusinessType) {
        setName(newBusinessType.name);
        setUseYn(newBusinessType.useYn);
        setDeleteYn(newBusinessType.deleteYn);
    }
}
