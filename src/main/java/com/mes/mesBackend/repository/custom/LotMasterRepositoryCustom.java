package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.*;
import com.mes.mesBackend.entity.ItemAccountCode;
import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.entity.OutSourcingInput;
import com.mes.mesBackend.entity.enumeration.EnrollmentType;
import com.mes.mesBackend.entity.enumeration.GoodsType;
import com.mes.mesBackend.entity.enumeration.LotMasterDivision;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LotMasterRepositoryCustom {
    // id 로 itemAccountCode 의 code 조회
    ItemAccountCode findCodeByItemId(Long itemId);
    // 원부자재 일련번호 시퀀스 찾는법
    // 일련번호의 1~6이 현재날짜의 format 과 동일 And 일련번호가 9자
    // 끝에서 두번째 자리 수 중 제일 큰 애를 찾아서  +1
    Optional<String> findLotNoByLotNoLengthAndLotNoDateAndCode(int length, String date, String code);
    // LOT 마스터 조회, 검색조건: 품목그룹 id, LOT 번호, 품번|품명, 창고 id, 등록유형, 재고유무, LOT 유형, 검사중여부, 유효여부
    List<LotMasterResponse> findLotMastersByCondition(
            Long itemGroupId,
            String lotNo,
            String itemNoAndItemName,
            Long wareHouseId,
            EnrollmentType enrollmentType,
            Boolean stockYn,
            Long lotTypeId,
            Boolean testingYn,
            WorkProcessDivision workProcessDivision
    );
    //제품분류에 따른 가장 마지막에 생성된 LOT NO 반환
    Optional<String> findLotNoByGoodsType(GoodsType goodsType, LocalDate startDate, LocalDate endDate);

    //제품분류에 따른 가장 마지막에 생성된 LOT NO 반환(수정버전)
    Optional<String> findLotNoByAccountCode(Long itemAccountCodeId, LocalDate startDate);
    //외주입고정보로 LOT정보 가져오기
    List<OutsourcingInputLOTResponse> findLotMastersByOutsourcing(Long inputId);
    //LOT정보 조회
    OutsourcingInputLOTResponse findLotMasterByInputAndId(OutSourcingInput input, Long id);
    //재고현황 조회
    List<MaterialStockReponse> findStockByItemAccountAndItemAndItemAccountCode(
            Long itemAccountId, Long itemId, Long itemGroupId, Long warehouseId);
    // 출하 LOT 정보 생성 시 LOT 정보 조회 API
    List<LotMasterResponse.idAndLotNo> findLotMastersByShipmentLotCondition(Long itemId, int notShippedAmount);

    //ITEM으로 현재 재고현황 조회
    List<MaterialStockReponse> findStockAmountByItemId(Long itemId, Long warehouseId);

    //공정별 불량 갯수 조회
    List<PopRecycleResponse> findBadAmountByWorkProcess(Long workProcessId);

    //공정별 불량 개수 단일 조회
    PopRecycleResponse findBadAmountByWorkProcess(Long workProcessId, Long itemId);

    //재사용 생성 가능 LOT검색
    List<LotMaster> findBadLotByItemIdAndWorkProcess(Long itemId, Long workProcessId);

    // 품목 id 에 해당되는 lotMaster 조회
    List<PopBomDetailLotMasterResponse> findAllByItemIdAndLotNo(Long itemId, String lotNo);

    // 공정, 설비로 라벨프린트 대상 조회
    List<LabelPrintResponse> findPrintsByWorkProcessAndEquipment(Long workProcessId, Long equipmentId);
    // 생성날짜가 오늘이고, lotDivision 이 dummny 인 걸 찾아옴
    Optional<String> findDummyNoByDivision(LotMasterDivision lotMasterDivision, LocalDate startDate);
    Optional<BadItemWorkOrderResponse.subDto> findLotMaterByDummyLotIdAndWorkProcessId(Long dummyLotId, Long workProcessId);
    // 생성날짜가 오늘이고, 공정구분, inputEquipment 가 같은거
    Optional<LotMaster> findByTodayAndWorkProcessDivisionEqAndInputEquipmentEq(
            LocalDate now,
            WorkProcessDivision workProcessDivision,
            Long inputEquipmentId
    );
}
