package com.mes.mesBackend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CodeMasterResponse {
    Long id;
    String mainCode;    // 주코드
    String codeName;    // 코드명
    String defaultName; // 기본어명
    String refInfo1;   // 참조정보1
    String refInfo2;   // 참조정보2
    String refInfo3;   // 참조정보3
    String refInfo4;   // 참조정보4
    String refInfo5;   // 참조정보5
    List<SubCodeMasterResponse> subCodeMasterResponse;  // 서브코드
}
