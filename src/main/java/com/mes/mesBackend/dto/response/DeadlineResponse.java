package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "마감일자")
public class DeadlineResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "마감일자")
    LocalDate deadline;
}
