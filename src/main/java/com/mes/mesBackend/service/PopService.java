package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.*;
import com.mes.mesBackend.entity.enumeration.BreakReason;
import com.mes.mesBackend.entity.enumeration.ProcessStatus;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;

import java.util.List;

// pop
public interface PopService {
    // pop 작업공정 조회
    List<WorkProcessResponse> getPopWorkProcesses(Boolean recycleYn);
    // 작업지시 정보 리스트 api, 조건: 작업공정
    List<PopWorkOrderResponse> getPopWorkOrders(WorkProcessDivision workProcessDivision) throws NotFoundException;
    // 작업지시 진행상태 정보 조회
    List<PopWorkOrderStates> getPopWorkOrderStates(Long workOrderId) throws NotFoundException;
    // 작업지시 진행상태 변경
    void updatePopWorkOrderState(Long lotMasterId, ProcessStatus processStatus) throws NotFoundException, BadRequestException;
    // 작업지시 상태 변경 (작업완료)
    Long createWorkOrder(
            Long workOrderId,
            Long itemId,
            String userCode,
            int productAmount,
            int stockAmount,
            int badItemAmount,
            Long equipmentId
    ) throws NotFoundException, BadRequestException;
    // 공정으로 공정에 해당하는 설비정보 가져오기 GET
    List<PopEquipmentResponse> getPopEquipments(WorkProcessDivision workProcessDivision, Boolean produceYn) throws NotFoundException;
    // 해당 품목(반제품)에 대한 원자재, 부자재 정보 가져와야함
    List<PopBomDetailItemResponse> getPopBomDetailItems(Long lotMasterId) throws NotFoundException;
    // 원자재, 부자재에 해당되는 lotMaster 조회, stockAmount 1 이상
    List<PopBomDetailLotMasterResponse> getPopBomDetailLotMasters(Long lotMasterId, Long itemId, String lotNo) throws NotFoundException;
    // 원부자재 lot 사용정보 조회
    List<PopBomDetailLotMasterResponse> getLotMasterExhaust(Long lotMasterId, Long itemId);
    // 원부자재 lot 사용정보 등록
    PopBomDetailLotMasterResponse createLotMasterExhaust(Long lotMasterId, Long itemId, Long exhaustLotMasterId, int exhaustAmount) throws NotFoundException, BadRequestException;
    // 원부자재 lot 사용정보 수정
    PopBomDetailLotMasterResponse putLotMasterExhaust(Long lotMasterId, Long itemId, Long exhaustLotMasterId, int exhaustAmount) throws NotFoundException, BadRequestException;
    // 원부자재 lot 사용정보 삭제
    void deleteLotMasterExhaust(Long lotMasterId, Long itemId, Long exhaustLotMasterId) throws NotFoundException;
    // 중간검사 품목 정보 조회
    PopTestItemResponse getPopTestItem(Long lotMasterId) throws NotFoundException;
    // 공정에 해당하는 불량유형 조회
    List<PopBadItemTypeResponse> getPopTestBadItemTypes(Long lotMasterId) throws NotFoundException;
    // 중간검사 등록된 불량 조회
    List<PopTestBadItemResponse> getPopBadItemEnrollments(Long lotMasterId) throws NotFoundException;
    // 불량 등록
    PopTestBadItemResponse createPopBadItemEnrollment(Long lotMasterId, Long badItemTypeId, int badItemAmount) throws NotFoundException, BadRequestException;
    // 불량 수량 수정
    PopTestBadItemResponse putPopBadItemEnrollment(Long enrollmentBadItemId, int badItemAmount) throws NotFoundException, BadRequestException;
    // 불량 삭제
    void deletePopBadItemEnrollment(Long enrollmentBadItemId) throws NotFoundException;
    // 분할 lot 조회
    List<PopLotMasterResponse> getPopLotMasters(Long lotMasterId) throws NotFoundException;
    // 분할 lot 생성
    PopLotMasterResponse createPopLotMasters(Long lotMasterId, int amount) throws NotFoundException, BadRequestException;
    // 분할 lot 수정
    PopLotMasterResponse putPopLotMasters(Long lotMasterId, int amount) throws NotFoundException, BadRequestException;
    // 분할 lot 삭제
    void deletePopLotMasters(Long lotMasterId) throws NotFoundException;
    // 충진공정 설비 선택
    void putFillingEquipmentOfRealLot(Long lotMasterId, Long equipmentId) throws NotFoundException, BadRequestException;
    // 충진공정 설비 고장등록 api
    void createFillingEquipmentError(Long workOrderId, Long lotMasterId, BreakReason breakReason) throws NotFoundException, BadRequestException;
}
