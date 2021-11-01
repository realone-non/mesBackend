package com.mes.mesBackend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WareHouseRequest {
    String wareHouseCode;  // 창고코드
    String wareHouseName;   // 창고명
    Long wareHouseType;           // 창고유형 id
    String wareHouseGroup;   // 창고그룹
    boolean useYn;      // 사용여부
}
