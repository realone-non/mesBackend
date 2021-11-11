package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
public class ItemGroupResponse {
    CodeResponse.idAndCode itemGroupCode;  // 그룹코드
    String topGroupCode;        // 상위그룹코드
    String topGroupName;        // 상위그룹명
    String groupName;           // 그룹명
    String defaultName;         // 기본어명
    int orders;              // 순번
    boolean useYn;

    @Getter
    @Setter
    public static class idAndGroupName {
        Long id;
        String groupName;
    }
}
