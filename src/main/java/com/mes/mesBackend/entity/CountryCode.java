package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 거래처 - 국가코드
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "COUNTRY_CODES")
@Data
public class CountryCode extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '국가코드 고유아이디'")
    private Long id;

    @Column(name = "NAME", nullable = false, columnDefinition = "varchar(255) COMMENT '국가코드'")
    private String name;

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn = true;  // 사용여부

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    public void put(CountryCode newCountryCode) {
        setName(newCountryCode.name);
        setUseYn(newCountryCode.useYn);
    }

    public void delete() {
        setDeleteYn(true);
    }
}
