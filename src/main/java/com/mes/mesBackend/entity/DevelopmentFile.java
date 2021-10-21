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
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @OneToOne @JoinColumn(name = "DEVELOPMENT_FILE_TYPE", nullable = false)
    private DevelopmentFileType developmentFileType;        // 개발등록 파일 타입

    @Column(name = "FILE_URL", nullable = false)
    private String fileUrl;         // 파일url

    @Column(name = "REGISTERED")
    private String registered;      // 등록자

    @Column(name = "APPROVAL_DATE")
    private LocalDate approvalDate;     // 승인일시

    @Column(name = "UPDATE_CONTENT")
    private String updateContent;       // 변경내용

    @Column(name = "VER")
    private int ver;                    // VER

    @ManyToOne @JoinColumn(name = "DEVELOPMENT", nullable = false)
    private Development development;                // 개발등록
}
