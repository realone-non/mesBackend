package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.entity.enumeration.EnrollmentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

// 7-2. LOT Tracking
@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "LOT Tracking")
public class LotTrackingResponse {
    @Schema(description = "메인로트 고유아이디")
    Long mainLotMasterId;

    @Schema(description = "메인 LOT NO")
    String mainLotNo;

    @Schema(description = "메인 품번")
    String mainItemNo;

    @Schema(description = "메인 품목")
    String mainItemName;

    @Schema(description = "메인 등록유형")
    EnrollmentType mainEnrollmentType;

    @Schema(description = "메인 생성일자")
    LocalDateTime createdDate;

    @Schema(description = "투입수량")
    int inputAmount;

    @Schema(description = "서브 LOT")
    List<LotTrackingResponse> subLots;

    public LotTrackingResponse setMainLotMaster(LotMaster lotMaster) {
        setMainLotMasterId(lotMaster.getId());
        setMainLotNo(lotMaster.getLotNo());
        setMainItemNo(lotMaster.getItem().getItemNo());
        setMainItemName(lotMaster.getItem().getItemName());
        setMainEnrollmentType(lotMaster.getEnrollmentType());
        setCreatedDate(lotMaster.getCreatedDate());
        return this;
    }

    public LotTrackingResponse setSubLots(List<LotTrackingResponse> lotTrackingResponses) {
        setSubLots(lotTrackingResponses);
        return this;
    }


//    @Schema(description = "고유아이디")
//    Long lotMasterId;
//
//    @Schema(description = "LOT NO")
//    String lotNo;
//
//    @Schema(description = "품번")
//    String itemNo;
//
//    @Schema(description = "품명")
//    String itemName;
//
//    @Schema(description = "등록유형")
//    EnrollmentType enrollmentType;
//
//    @Schema(description = "발생일자")
//    @JsonFormat(pattern = YYYY_MM_DD_HH_MM, timezone = ASIA_SEOUL)
//    LocalDateTime createdDate;
//
//    @Schema(description = "투입수량")
//    int inputAmount;
}
