package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "LOT 제조일자")
public class LotCreatedResponse {
    @Schema(description = "id")
    Long id;

    @Schema(description = "lot no")
    String lotNo;

    @Schema(description = "제조일자(생성일시)")
    LocalDateTime createdDate;

    public LotCreatedResponse toResponse(Long id, String lotNo, LocalDateTime createdDate) {
        setId(id);
        setLotNo(lotNo);
        setCreatedDate(createdDate);
        return this;
    }
}
