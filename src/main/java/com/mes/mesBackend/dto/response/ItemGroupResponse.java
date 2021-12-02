package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "품목그룹")
public class ItemGroupResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "그룹코드")
    CodeResponse.idAndCode itemGroupCode;

    @Schema(description = "상위그룹코드")
    String topGroupCode;

    @Schema(description = "상위그룹명")
    String topGroupName;

    @Schema(description = "그룹명")
    String groupName;

    @Schema(description = "기본어명")
    String defaultName;

    @Schema(description = "순번")
    int orders;

    @Schema(description = "사용여부")
    boolean useYn;

    @Getter
    @Setter
    public static class idAndGroupName {
        Long id;
        String groupName;
    }
}
