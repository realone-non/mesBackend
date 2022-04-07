package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.Item;
import com.mes.mesBackend.entity.enumeration.GoodsType;
import com.mes.mesBackend.entity.enumeration.TestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Schema
@JsonInclude(NON_NULL)
public class ProduceRequestBomDetail {
    @Schema(description = "품목 id")
    Long itemId;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "규격")
    String standard;    // 규격

    @Schema(description = "제조사품번")
    String manufacturerPartNo;

    @Schema(description = "구매단위")
    String unit;

    @Schema(description = "검사유형")
    TestType testType;

    @Schema(description = "제조사")
    String manufacturer;

    @Schema(description = "품목계정구분")
    GoodsType goodsType;

    public ProduceRequestBomDetail converter(Item item) {
        setItemId(item.getId());
        setItemNo(item.getItemNo());
        setItemName(item.getItemName());
        setStandard(item.getStandard());
        setManufacturerPartNo(item.getManufacturerPartNo());
        setUnit(item.getUnit().getUnitCodeName());
        setTestType(item.getTestType());
        if (item.getManufacturer() != null) {
            setManufacturer(item.getManufacturer().getClientName());
        }
        setGoodsType(item.getItemAccount().getGoodsType());
        return this;
    }
}
