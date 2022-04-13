package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

//11-5. Shortage
@Getter
@Setter
@Schema(description = "11-5. Shortage")
@JsonInclude(NON_NULL)
public class ShortageReponse {
    @Schema(description = "원재료 품번")
    String materialNo;

    @Schema(description = "원재료 품명")
    String materialName;

    @Schema(description = "전일재고수량")
    float beforeDayAmount;

    @Schema(description = "입고예정수량")
    float scheduleInputAmount;

    @Schema(description = "생산소요량")
    float productionCapacity;

    @Schema(description = "과부족량")
    float overLackAmount;
}
