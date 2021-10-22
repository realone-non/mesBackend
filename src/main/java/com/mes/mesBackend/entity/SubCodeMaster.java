package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 코드마스터 -부코드
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "SUB_CODE_MASTERS")
@Data
public class SubCodeMaster extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '부코드 고유아이디'")
    private Long id;  // 부코드 id

    @Column(name = "SUB_CODE", nullable = false, columnDefinition = "bigint COMMENT '부코드'")
    private String subCode;  // 부코드

    @Column(name = "CODE_NAME", nullable = false, columnDefinition = "bigint COMMENT '부코드명'")
    private String codeName;  // 부코드명

    @Column(name = "SUB_CODE_DEFAULT_NAME", nullable = false, columnDefinition = "bigint COMMENT '기본어명'")
    private String defaultName;   // 기본어명

    @Column(name = "REF_INFO_1", columnDefinition = "bigint COMMENT '참조정보1'")
    private String refInfo1;   // 참조정보1

    @Column(name = "REF_INFO_2", columnDefinition = "bigint COMMENT '참조정보2'")
    private String refInfo2;   // 참조정보2

    @Column(name = "REF_INFO_3", columnDefinition = "bigint COMMENT '참조정보3'")
    private String refInfo3;   // 참조정보3

    @Column(name = "REF_INFO_4", columnDefinition = "bigint COMMENT '참조정보4'")
    private String refInfo4;   // 참조정보4

    @Column(name = "REF_INFO_5", columnDefinition = "bigint COMMENT '참조정보5'")
    private String refInfo5;   // 참조정보5

    @Column(name = "REF_INFO_DESC", columnDefinition = "bigint COMMENT '참조정보 설명'")
    private String refInfoDesc;    // 참조정보 설명

    @Column(name = "OUTPUT_ORDER", nullable = false, columnDefinition = "bigint COMMENT '출력순번'")
    private int outputOrder;     // 출력순번

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bigint COMMENT '사용여부'")
    private Boolean useYn = true;      //  사용여부

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "CODE_MASTER", nullable = false, columnDefinition = "bigint COMMENT '코드마스터'")
    private CodeMaster codeMaster;

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bigint COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    // 수정매핑
    public void put(SubCodeMaster newSubCodeMaster) {
        setSubCode(newSubCodeMaster.subCode);
        setCodeName(newSubCodeMaster.codeName);
        setDefaultName(newSubCodeMaster.defaultName);
        setRefInfo1(newSubCodeMaster.refInfo1);
        setRefInfo2(newSubCodeMaster.refInfo2);
        setRefInfo3(newSubCodeMaster.refInfo3);
        setRefInfo4(newSubCodeMaster.refInfo4);
        setRefInfo5(newSubCodeMaster.refInfo5);
        setRefInfoDesc(newSubCodeMaster.refInfoDesc);
        setOutputOrder(newSubCodeMaster.outputOrder);
    }
}
