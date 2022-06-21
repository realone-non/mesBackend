package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.OutsourcingInputLOTResponse;
import com.mes.mesBackend.dto.response.OutsourcingInputResponse;
import com.mes.mesBackend.dto.response.OutsourcingStatusResponse;
import com.mes.mesBackend.entity.OutSourcingInput;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OutsourcingInputRepositoryCustom {
    //외주 입고 정보 검색 조건:외주처, 품목, 입고기간
    List<OutsourcingInputResponse> findAllByCondition(Long clientId, String itemNoAndItemName, LocalDate startDate, LocalDate endDate);

    //외주생산의뢰 단일 조회
    Optional<OutsourcingInputResponse> findInputByIdAndDeleteYnAndUseYn(Long id);

    //외주생산의로 ID로 외주입고 조회
    List<OutSourcingInput> findAllByRequestId(Long id);

    //외주생산의뢰 ID로 생산수량 조회
    Integer findAmountByRequestId(Long requestId);

    //외주 현황 조회
    List<OutsourcingStatusResponse> findStatusByCondition(Long clientId, String itemNoAndItemName);

    //아이템 ID조회
    Long findItemIdByInputId(Long inputId);
    // 외주입고 lot 정보 리스트 조회
    List<OutsourcingInputLOTResponse> findOutsourcingInputLotResponsesByRequestId(Long requestId);
    // 외주입고 lot 정보 단일 조회
    Optional<OutsourcingInputLOTResponse> findOutsourcingInputLotResponseByRequestId(Long requestId, Long inputId);
    // 외주생산의뢰로 등록된 외주입고가 존재하는지여부
    boolean existsByOutsourcingProductionRequestId(Long requestId);
    // 외주생산의뢰 id 로 외주입고 list 조회
    List<OutSourcingInput> findOutsourcingInputByRequestId(Long requestId);

}
