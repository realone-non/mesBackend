package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "불량 유형")
public class PopBadItemTypeResponse {
    @Schema(description = "불량 유형 고유아이디")
    Long badItemTypeId;

    @Schema(description = "불량 유형 명")
    String badItemTypeName;
}
