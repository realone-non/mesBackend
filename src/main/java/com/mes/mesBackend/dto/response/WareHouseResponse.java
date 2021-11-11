package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
public class WareHouseResponse {
    Long id;
    String wareHouseCode;  // 창고코드
    String wareHouseName;   // 창고명
    WareHouseTypeResponse wareHouseType;
    String wareHouseGroup;   // 창고그룹
    boolean useYn;      // 사용여부

    @Getter
    @Setter
    public static class idAndName {
        Long id;
        String wareHouseName;   // 창고명
    }
}
