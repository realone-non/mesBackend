package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.Equipment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.helper.Constants.ASIA_SEOUL;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD;

@Getter
@Setter
@Schema(description = "설비")
@JsonInclude(NON_NULL)
public class EquipmentResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "설비코드")
    String equipmentCode;

    @Schema(description = "설비명")
    String equipmentName;

    @Schema(description = "설비유형")
    String equipmentType;

    @Schema(description = "규격&모델")
    String model;

    @Schema(description = "구매처")
    ClientResponse.idAndName client;

    @Schema(description = "구매일자")
    @JsonFormat(pattern = YYYY_MM_DD, timezone = ASIA_SEOUL)
    LocalDateTime purchaseDate;

    @Schema(description = "구입금액")
    int purchaseAmount;

    @Schema(description = "생산업체명")
    String maker;

    @Schema(description = "시리얼번호")
    String serialNo;

    @Schema(description = "생산개시일자")
    @JsonFormat(pattern = YYYY_MM_DD, timezone = ASIA_SEOUL)
    LocalDateTime startDate;

    @Schema(description = "작업라인, 작업장")
    WorkLineResponse.workLineAndWorkCenterAndWorkProcess workLine;

    @Schema(description = "작업공정")
    WorkProcessResponse.idAndName workProcess;

    @Schema(description = "점검주기")
    int checkCycle;

    @Schema(description = "사용")
    boolean useYn = true;

    @Schema(description = "수명(월)정보")
    String life;

    @Schema(description = "최종점검일자")
    @JsonFormat(pattern = YYYY_MM_DD, timezone = ASIA_SEOUL)
    LocalDate lastTestDate;

    @Schema(description = "로트 생성 용 코드")
    String lotCode;

    public EquipmentResponse setResponse(Equipment equipment) {
        setId(equipment.getId());
        setEquipmentCode(equipment.getEquipmentCode());
        setEquipmentName(equipment.getEquipmentName());
        if (equipment.getWorkLine() != null) {
            WorkLineResponse.workLineAndWorkCenterAndWorkProcess workLine = new WorkLineResponse.workLineAndWorkCenterAndWorkProcess();
            setEquipmentType(equipment.getWorkLine().getWorkLineName());
            workLine.setId(equipment.getWorkLine().getId());
            workLine.setWorkLineName(equipment.getWorkLine().getWorkLineName());
            workLine.setWorkCenterName(equipment.getWorkLine().getWorkCenter().getWorkCenterName());
            setWorkLine(workLine);
        }
        setModel(equipment.getModel());
        if (equipment.getClient() != null) {
            ClientResponse.idAndName client = new ClientResponse.idAndName();
            client.setId(equipment.getClient().getId());
            client.setClientName(equipment.getClient().getClientName());
            setClient(client);
        }
        setPurchaseDate(equipment.getPurchaseDate());
        setPurchaseAmount(equipment.getPurchaseAmount());
        setMaker(equipment.getMaker());
        setSerialNo(equipment.getSerialNo());
        setStartDate(equipment.getStartDate());
        if (equipment.getWorkProcess() != null) {
            WorkProcessResponse.idAndName workProcess = new WorkProcessResponse.idAndName();
            workProcess.setId(equipment.getWorkProcess().getId());
            workProcess.setWorkProcessName(equipment.getWorkProcess().getWorkProcessName());
            setWorkProcess(workProcess);
        }
        setCheckCycle(equipment.getCheckCycle());
        setUseYn(equipment.isUseYn());
        setLife(equipment.getLife());
        setLastTestDate(equipment.getLastTestDate());
        setLotCode(equipment.getLotCode());
        return this;
    }
}
