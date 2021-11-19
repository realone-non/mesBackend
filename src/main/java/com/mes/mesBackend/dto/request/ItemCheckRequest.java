package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.enumeration.TestCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "품목별 검사항목")
public class ItemCheckRequest {
    @Schema(description = "품목 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long item;

    @Schema(description = "검사유형")
    @NotNull(message = NOT_NULL)
    TestCategory checkCategory = TestCategory.INPUT_TEST;
}
