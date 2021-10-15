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
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(name = "FILE_TYPE")
    private String fileType;        // 파일유형

    @Column(name = "VER")
    private int ver;            // VER

    @Column(name = "FILE_URL")
    private String fileUrl;     // 첨부파일유알엘

    @Column(name = "REGISTERED")
    private String registered;      // 등록자
}
