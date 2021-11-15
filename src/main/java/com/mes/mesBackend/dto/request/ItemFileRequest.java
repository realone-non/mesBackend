package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "품목파일")
public class ItemFileRequest {
    @Schema(description = "VER")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    int version;

    @Schema(description = "등록자")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long user;
}
