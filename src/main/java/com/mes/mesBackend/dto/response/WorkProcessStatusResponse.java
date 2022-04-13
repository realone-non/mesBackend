package com.mes.mesBackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

// 작업공정별 정보
@Getter
@Setter
@Schema(description = "작업공정별 정보")
public class WorkProcessStatusResponse {
    @Schema(description = "원료혼합 작업진행")
    Long materialMicingOngoingAmount;

    @Schema(description = "원료혼합 작업완료")
    Long materialMicingCompletionAmount;

    @Schema(description = "원료혼합 수량")
    int materialMicingProductionAmount;

    @Schema(description = "충진 작업진행")
    Long fillingOngoingAmount;

    @Schema(description = "충진 작업완료")
    Long fillingCompletionAmount;

    @Schema(description = "충진 수량")
    int fillingProductionAmount;

    @Schema(description = "캡조립 작업진행")
    Long capAssemblyOngoingAmount;

    @Schema(description = "캡조립 작업완료")
    Long capAssemblyCompletionAmount;

    @Schema(description = "캡조립 수량")
    int capAssemblyProductionAmount;

    @Schema(description = "라벨링 작업진행")
    Long labelingOngoingAmount;

    @Schema(description = "라벨링 작업완료")
    Long labelingCompletionAmount;

    @Schema(description = "라벨링 수량")
    int labelingProductionAmount;

    @Schema(description = "포장 작업진행")
    Long packagingOngoingAmount;

    @Schema(description = "포장 작업완료")
    Long packagingCompletionAmount;

    @Schema(description = "포장 수량")
    int packagingProductionAmount;
}
