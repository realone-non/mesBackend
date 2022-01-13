package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

// pop-작업지시정보
@Getter
@Setter
@Schema(description = "pop-작업지시정보")
@JsonInclude(NON_NULL)
public class PopWorkOrderResponse {
    @Schema(description = "작업지시 고유아이디")
    Long id;

    @Schema(description = "작업자 고유아이디")
    Long userId;

    @Schema(description = "작업자 명")
    String userName;

    @Schema(description = "지시번호")
    String orderNo;

    @Schema(description = "지시수량")
    int orderAmount;

    @Schema(description = "작업예정일")
    LocalDate expectedWorkDate;
}
