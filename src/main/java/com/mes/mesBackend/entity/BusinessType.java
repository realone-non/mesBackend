package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 업태
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "BUSINESS_TYPES")
@Data
public class BusinessType extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "USE_YN", nullable = false)
    private boolean useYn = true;  // 사용여부
}
