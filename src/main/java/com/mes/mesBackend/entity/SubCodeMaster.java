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

    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;  // 부코드 id

    @Column(name = "SUB_CODE", nullable = false)
    private String subCode;  // 부코드

    @Column(name = "CODE_NAME", nullable = false)
    private String codeName;  // 부코드명

    @Column(name = "SUB_CODE_DEFAULT_NAME", nullable = false)
    private String defaultName;   // 기본어명

    @Column(name = "REF_INFO_1")
    private String refInfo1;   // 참조정보1

    @Column(name = "REF_INFO_2")
    private String refInfo2;   // 참조정보2

    @Column(name = "REF_INFO_3")
    private String refInfo3;   // 참조정보3

    @Column(name = "REF_INFO_4")
    private String refInfo4;   // 참조정보4

    @Column(name = "REF_INFO_5")
    private String refInfo5;   // 참조정보5

    @Column(name = "REF_INFO_DESC")
    private String refInfoDesc;    // 참조정보 설명

    @Column(name = "OUTPUT_ORDER", nullable = false)
    private int outputOrder;     // 출력순번

    @Column(name = "USE_YN", nullable = false)
    private Boolean useYn = true;      //  사용여부

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) @JoinColumn(name = "CODE_MASTER_ID", nullable = false)
    private CodeMaster codeMaster;

    @Column(name = "DELETE_YN")
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
