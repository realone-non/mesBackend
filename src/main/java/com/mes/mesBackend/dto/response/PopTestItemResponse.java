package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.LotMaster;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "중간검사 품목 정보 조회")
public class PopTestItemResponse {
    @Schema(description = "품목 고유아이디")
    Long itemId;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "lot 고유아이디")
    Long lotMasterId;

    @Schema(description = "Lot No")
    String lotNo;

    @Schema(description = "전체수량")
    int createdAmount;

    @Schema(description = "양품수량")
    int stockAmount;

    @Schema(description = "불량수량")
    int badItemAmount;

    @Schema(description = "등록 가능한 불량수량")
    int possibleBadItemAmount;

    @Schema(description = "등록된 불량 수량")
    int completeBadItemAmount;

    public void put(LotMaster lotMaster, int equipmentAllBadItemAmount) {
        setItemId(lotMaster.getItem().getId());
        setItemNo(lotMaster.getItem().getItemNo());
        setItemName(lotMaster.getItem().getItemName());
        setLotMasterId(lotMaster.getId());
        setLotNo(lotMaster.getLotNo());
        setCreatedAmount(lotMaster.getCreatedAmount());
        setStockAmount(lotMaster.getStockAmount());
        setBadItemAmount(lotMaster.getBadItemAmount());
        setPossibleBadItemAmount(lotMaster.getBadItemAmount() - equipmentAllBadItemAmount);
        setCompleteBadItemAmount(equipmentAllBadItemAmount);
    }
}
