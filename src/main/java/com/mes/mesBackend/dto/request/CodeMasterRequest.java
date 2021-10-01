package com.mes.mesBackend.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CodeMasterRequest {
    String mainCode;    // 주코드
    String codeName;    // 코드명
    String defaultName; // 기본어명
    String refInfo1;   // 참조정보1
    String refInfo2;   // 참조정보2
    String refInfo3;   // 참조정보3
    String refInfo4;   // 참조정보4
    String refInfo5;   // 참조정보5
    List<SubCodeMasterRequest> subCodeMasterRequest;  // 서브코드
}
