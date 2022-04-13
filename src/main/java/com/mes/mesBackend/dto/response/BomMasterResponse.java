package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.BomMaster;
import com.mes.mesBackend.entity.enumeration.DevelopStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.helper.Constants.ASIA_SEOUL;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD;


@Getter
@Setter
@Schema(description = "BOM 마스터")
@JsonInclude(NON_NULL)
public class BomMasterResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "품목 정보")
    ItemResponse.itemToBomResponse item;

    @Schema(description = "BOM 번호")
    int bomNo;

    @Schema(description = "유효시작일")
    @JsonFormat(pattern = YYYY_MM_DD, timezone = ASIA_SEOUL)
    LocalDate startDate;

    @Schema(description = "유효종료일")
    @JsonFormat(pattern = YYYY_MM_DD, timezone = ASIA_SEOUL)
    LocalDate endDate;

    @Schema(description = "개발상태")
    DevelopStatus developStatus;

    @Schema(description = "비고")
    String note;

    @Schema(description = "승인일시")
    @JsonFormat(pattern = YYYY_MM_DD, timezone = ASIA_SEOUL)
    LocalDate approvalDate;

    @Schema(description = "사용")
    Boolean useYn;

    @Schema(description = "작업공정")
    WorkProcessResponse.idAndName workProcess;

    public BomMasterResponse setResponse(BomMaster bomMaster) {
        setId(bomMaster.getId());
        if (bomMaster.getItem() != null) {
            ItemResponse.itemToBomResponse item = new ItemResponse.itemToBomResponse();
            item.setId(bomMaster.getItem().getId());
            item.setItemNo(bomMaster.getItem().getItemNo());
            item.setItemName(bomMaster.getItem().getItemName());
            item.setItemAccount(bomMaster.getItem().getItemAccount().getAccount());
            item.setManufacturerPartNo(bomMaster.getItem().getManufacturerPartNo());
            item.setInputUnitPrice(bomMaster.getItem().getInputUnitPrice());
            setItem(item);
        }
        setBomNo(bomMaster.getBomNo());
        setStartDate(bomMaster.getStartDate());
        setEndDate(bomMaster.getEndDate());
        setDevelopStatus(bomMaster.getDevelopStatus());
        setNote(bomMaster.getNote());
        setApprovalDate(bomMaster.getApprovalDate());
        setUseYn(bomMaster.isUseYn());
        if (bomMaster.getWorkProcess() != null) {
            WorkProcessResponse.idAndName workProcess = new WorkProcessResponse.idAndName();
            workProcess.setId(bomMaster.getWorkProcess().getId());
            workProcess.setWorkProcessName(bomMaster.getWorkProcess().getWorkProcessName());
            setWorkProcess(workProcess);
        }
        return this;
    }
}
