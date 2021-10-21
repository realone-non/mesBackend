package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
/*
* 개발등록 파일타입
* 파일타입 (개발의뢰서,요구사양서,타당성검토 .. 등)
* */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "DEVELOPMENT_FILE_TYPES")
@Data
public class DevelopmentFileType extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    private String fileType;        // 개발등록 파일타입
}
