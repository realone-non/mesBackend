package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.*;
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
    // 작업지시 상태 변경
    Long createCreateWorkOrder(Long workOrderId, Long itemId, String userCode, int productAmount, Long equipmentId) throws NotFoundException, BadRequestException;
    // 공정으로 공정에 해당하는 설비정보 가져오기 GET
    List<PopEquipmentResponse> getPopEquipments(WorkProcessDivision workProcessDivision) throws NotFoundException;
    // 해당 품목(반제품)에 대한 원자재, 부자재 정보 가져와야함
    List<PopBomDetailItemResponse> getPopBomDetailItems(Long lotMasterId) throws NotFoundException;
    // 원자재, 부자재에 해당되는 lotMaster 조회, stockAmount 1 이상
    List<PopBomDetailLotMasterResponse> getPopBomDetailLotMasters(Long lotMasterId, Long itemId, String lotNo) throws NotFoundException;
    // 원부자재 lot 사용정보 등록
    PopBomDetailLotMasterResponse createLotMasterExhaust(Long lotMasterId, Long itemId, Long exhaustLotMasterId, int exhaustAmount) throws NotFoundException, BadRequestException;
    // 원부자재 lot 사용정보 수정
    PopBomDetailLotMasterResponse putLotMasterExhaust(Long lotMasterId, Long itemId, Long exhaustLotMasterId, int exhaustAmount) throws NotFoundException, BadRequestException;
    // 원부자재 lot 사용정보 삭제
    void deleteLotMasterExhaust(Long lotMasterId, Long itemId, Long exhaustLotMasterId) throws NotFoundException;
    // 원부자재 lot 사용정보 조회
    List<PopBomDetailLotMasterResponse> getLotMasterExhaust(Long lotMasterId, Long itemId);
}
