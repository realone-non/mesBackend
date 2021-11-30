package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.NOT_NULL;

@Getter
@Setter
@Schema(description = "국가코드")
public class CountryCodeRequest {

    @Schema(description = "국가코드")
    @NotBlank(message = NOT_NULL)
    String name;

    @Schema(description = "사용여부")
    @NotNull(message = NOT_NULL)
    boolean useYn;  // 사용여부
}
