package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.PopWorkOrderDetailResponse;
import com.mes.mesBackend.dto.response.PopWorkOrderResponse;
import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.repository.WorkOrderDetailRepository;
import com.mes.mesBackend.service.LotMasterService;
import com.mes.mesBackend.service.PopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PopServiceImpl implements PopService {
    private final WorkOrderDetailRepository workOrderDetailRepository;
    private final LotMasterService lotMasterService;

    // 작업지시 정보 리스트 api, 조건: 작업자, 작업공정
    @Override
    public List<PopWorkOrderResponse> getPopWorkOrders(Long workProcessId, Long userId) {
        return workOrderDetailRepository.findPopWorkOrderResponsesByCondition(workProcessId, userId, fromDate);
    }

    // 작업지시 상세 정보
    // 위에 해당 작업지시로 bomItemDetail 항목들 가져오기(품번, 품명, 계정, bom 수량, 예약수량)
    @Override
    public List<PopWorkOrderDetailResponse> getPopWorkOrderDetails(Long lotMasterId, Long workOrderId) throws NotFoundException {
        Integer contractItemAmount = workOrderDetailRepository.findContractItemAmountByWorkOrderId(workOrderId);
        LotMaster lotMaster = lotMasterService.getLotMasterOrThrow(lotMasterId);
        List<PopWorkOrderDetailResponse> list = workOrderDetailRepository.findPopWorkOrderDetailResponsesByItemId(lotMaster.getItem().getId());
        for (PopWorkOrderDetailResponse popWorkOrderDetailResponse : list) {
            popWorkOrderDetailResponse.setReservationAmount(popWorkOrderDetailResponse.getBomAmount() * contractItemAmount);
        }
        return list;
    }
}
