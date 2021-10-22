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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '국가코드 고유아이디'")
    private Long id;

    @Column(name = "NAME", nullable = false, columnDefinition = "bigint COMMENT '국가코드'")
    private String name;

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bigint COMMENT '사용여부'")
    private boolean useYn = true;  // 사용여부

    @Column(name = "DELETE_YN", columnDefinition = "bigint COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부
}
