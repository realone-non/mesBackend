package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

// 휴일
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "HOLIDAYS")
@Data
public class Holiday extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(name = "DATE", nullable = false)
    private LocalDate date; // 휴일일자

    @Column(name = "DAY")
    private String day; // 요일

    @Column(name = "TYPE", nullable = false)
    private String type;    // 휴일유형

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;    // 비고

    @Column(name = "USE_YN", nullable = false)
    private boolean useYn = true;  // 사용여부
}
