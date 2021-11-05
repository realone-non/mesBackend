package com.mes.mesBackend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemGroupRequest {
    Long itemGroupCode;
    String topGroupCode;        // 상위그룹코드
    String topGroupName;        // 상위그룹명
    String groupName;           // 그룹명
    String defaultName;         // 기본어명
    int orders;              // 순번
    boolean useYn;
}
