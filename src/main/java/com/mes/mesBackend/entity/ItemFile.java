package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 품목등록 파일
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "ITEM_FILES")
@Data
public class ItemFile extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '품목 파일 고유아이디'")
    private Long id;

    @Column(name = "FILE_TYPE", columnDefinition = "varchar(255) COMMENT '파일유형'")
    private String fileType;        // 파일유형

    @Column(name = "VER", columnDefinition = "int COMMENT 'VER'")
    private int ver;            // VER

    @Column(name = "FILE_URL",length = 400, columnDefinition = "varchar(255) COMMENT '첨부파일 url'")
    private String fileUrl;     // 첨부파일유알엘

    @Column(name = "REGISTERED", columnDefinition = "varchar(255) COMMENT '등록자'")
    private String registered;      // 등록자
}
