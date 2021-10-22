package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/*
 * 개발등록 파일
 * 파일타입 필수       -> DevelopmentFileType
 * 파일 url 필수
 * 등록일자 필수       -> CreatedDate
 * 등록자 필수
 * 승인일시
 * 변경내용
 * VER
 * CheckList파일 -> 일단 제외
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "DEVELOPMENT_FILES")
@Data
public class DevelopmentFile extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '개발등록 파일 고유아이디'")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEVELOPMENT_FILE_TYPE", nullable = false, columnDefinition = "bigint COMMENT '개발등록 파일 타입'")
    private DevelopmentFileType developmentFileType;        // 개발등록 파일 타입

    @Column(name = "FILE_URL", nullable = false, columnDefinition = "varchar(255) COMMENT '파일 url'")
    private String fileUrl;         // 파일url

    @Column(name = "REGISTERED", columnDefinition = "varchar(255) COMMENT '등록자'")
    private String registered;      // 등록자

    @Column(name = "APPROVAL_DATE", columnDefinition = "date COMMENT '승인일시'")
    private LocalDate approvalDate;     // 승인일시

    @Column(name = "UPDATE_CONTENT", columnDefinition = "varchar(255) COMMENT '변경내용'")
    private String updateContent;       // 변경내용

    @Column(name = "VER", columnDefinition = "int COMMENT 'VER'")
    private int ver;                    // VER

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEVELOPMENT", nullable = false, columnDefinition = "bigint COMMENT '개발등록'")
    private Development development;                // 개발등록

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn = true;      //  사용여부

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부
}
