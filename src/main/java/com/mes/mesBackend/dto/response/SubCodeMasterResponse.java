package com.mes.mesBackend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubCodeMasterResponse {
    Long id;
    String subCode;  // 부코드
    String codeName;  // 부코드명
    String defaultName;   // 기본어명
    String refInfo1;   // 참조정보1
    String refInfo2;   // 참조정보2
    String refInfo3;   // 참조정보3
    String refInfo4;   // 참조정보4
    String refInfo5;   // 참조정보5
    String refInfoDesc;    // 참조정보 설명
    int outputOrder;     // 출력순번
    boolean useYn;  // 사용여부
}
