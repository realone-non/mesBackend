package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "불량")
public class PopTestBadItemResponse {
    @Schema(description = "불량 고유아이디")
    Long badItemId;

    @Schema(description = "불량 항목 고유아이디")
    Long badItemTypeId;

    @Schema(description = "불량 항목 명")
    String badItemTypeName;

    @Schema(description = "불량 수량")
    int badItemAmount;
}
