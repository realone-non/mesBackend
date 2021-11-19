package com.mes.mesBackend.dto.response;

import com.mes.mesBackend.entity.enumeration.TestCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "품목별 검사항목")
public class ItemCheckResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "품목")
    ItemResponse.noAndName item;

    @Schema(description = "검사유형")
    TestCategory checkCategory = TestCategory.INPUT_TEST;
}
