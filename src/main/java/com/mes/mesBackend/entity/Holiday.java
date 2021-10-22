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
public class Holiday extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '휴일 고유아이디'")
    private Long id;

    @Column(name = "DATE", nullable = false, columnDefinition = "date COMMENT '휴일일자'")
    private LocalDate date; // 휴일일자

    @Column(name = "DAY", columnDefinition = "varchar(255) COMMENT '요일'")
    private String day; // 요일

    @Column(name = "TYPE", nullable = false, columnDefinition = "varchar(255) COMMENT '휴일유형'")
    private String type;    // 휴일유형

    @Column(name = "DESCRIPTION", nullable = false, columnDefinition = "varchar(255) COMMENT '비고'")
    private String description;    // 비고

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn = true;  // 사용여부

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부
}
