package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.PopEquipmentResponse;
import com.mes.mesBackend.dto.response.PopWorkOrderResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;

import java.util.List;

// pop
public interface PopService {
    // 작업지시 정보 리스트 api, 조건: 작업공정
    List<PopWorkOrderResponse> getPopWorkOrders(Long workProcessId) throws NotFoundException;
    // 작업지시 상태 변경
    Long createCreateWorkOrder(Long workOrderId, Long itemId, String userCode, int productAmount, Long equipmentId) throws NotFoundException, BadRequestException;
    // 공정으로 공정에 해당하는 설비정보 가져오기 GET
    List<PopEquipmentResponse> getPopEquipments(Long workProcessId) throws NotFoundException;
}
