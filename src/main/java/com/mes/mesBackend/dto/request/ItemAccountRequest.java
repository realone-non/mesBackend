package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.mes.mesBackend.exception.Message.NOT_NULL;

@Getter
@Setter
@Schema(description = "품목계정")
public class ItemAccountRequest {

    @Schema(description = "품목계정")
    @NotBlank(message = NOT_NULL)
    String account;
}
