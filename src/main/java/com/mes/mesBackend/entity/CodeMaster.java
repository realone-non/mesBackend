package com.mes.mesBackend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

// 코드마스터 - 주코드
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "CODE_MASTERS")
@Data
public class CodeMaster extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '코드마스터 주코드 고유아이디'")
    private Long id;    // 주코드 id

    @Column(name = "MAIN_CODE", nullable = false, columnDefinition = "varchar(255) COMMENT '주코드'")
    private String mainCode;    // 주코드

    @Column(name = "CODE_NAME", nullable = false, columnDefinition = "varchar(255) COMMENT '코드명'")
    private String codeName;    // 코드명

    @Column(name = "DEFAULT_NAME", nullable = false, columnDefinition = "varchar(255) COMMENT '기본어명'")
    private String defaultName; // 기본어명

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private Boolean useYn = true;       // 사용여부

    @Column(name = "REF_INFO_1", columnDefinition = "varchar(255) COMMENT '참조정보1'")
    private String refInfo1;   // 참조정보1

    @Column(name = "REF_INFO_2", columnDefinition = "varchar(255) COMMENT '참조정보2'")
    private String refInfo2;   // 참조정보2

    @Column(name = "REF_INFO_3", columnDefinition = "varchar(255) COMMENT '참조정보3'")
    private String refInfo3;   // 참조정보3

    @Column(name = "REF_INFO_4", columnDefinition = "varchar(255) COMMENT '참조정보4'")
    private String refInfo4;   // 참조정보4

    @Column(name = "REF_INFO_5", columnDefinition = "varchar(255) COMMENT '참조정보5'")
    private String refInfo5;   // 참조정보5

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    // 수정매핑
    public void put(CodeMaster newCodeMaster) {
        setMainCode(newCodeMaster.mainCode);
        setCodeName(newCodeMaster.codeName);
        setDefaultName(newCodeMaster.defaultName);
        setRefInfo1(newCodeMaster.refInfo1);
        setRefInfo2(newCodeMaster.refInfo2);
        setRefInfo3(newCodeMaster.refInfo3);
        setRefInfo4(newCodeMaster.refInfo4);
        setRefInfo5(newCodeMaster.refInfo5);
    }
}

