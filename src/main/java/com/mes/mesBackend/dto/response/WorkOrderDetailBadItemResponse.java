package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.WorkOrderBadItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "작업지시 상세 불량")
public class WorkOrderDetailBadItemResponse {
    @Schema(description = "불량 고유아이디")
    Long enrollmentBadItemId;

    @Schema(description = "불량 항목 고유아이디")
    Long badItemTypeId;

    @Schema(description = "불량 항목 명")
    String badItemTypeName;

    @Schema(description = "불량 수량")
    int badItemAmount;

    public WorkOrderDetailBadItemResponse put(WorkOrderBadItem workOrderBadItem) {
        setEnrollmentBadItemId(workOrderBadItem.getId());
        setBadItemTypeId(workOrderBadItem.getBadItem().getId());
        setBadItemTypeName(workOrderBadItem.getBadItem().getBadItemName());
        setBadItemAmount(workOrderBadItem.getBadItemAmount());
        return this;
    }
}