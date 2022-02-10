package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Schema(description = "결제조건")
@JsonInclude(NON_NULL)
public class PayTypeResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "결제조건")
    String payType;
}
