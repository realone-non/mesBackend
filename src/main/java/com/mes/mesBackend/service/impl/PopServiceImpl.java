package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.PopWorkOrderDetailResponse;
import com.mes.mesBackend.dto.response.PopWorkOrderResponse;
import com.mes.mesBackend.entity.BomItemDetail;
import com.mes.mesBackend.entity.Item;
import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.entity.WorkProcess;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.repository.WorkOrderDetailRepository;
import com.mes.mesBackend.repository.WorkProcessRepository;
import com.mes.mesBackend.service.LotMasterService;
import com.mes.mesBackend.service.PopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PopServiceImpl implements PopService {
    private final WorkOrderDetailRepository workOrderDetailRepository;
    private final WorkProcessRepository workProcessRepository;
    private final LotMasterService lotMasterService;


    // 작업지시 정보 리스트 api, 조건: 작업자, 작업공정
    @Override
    public List<PopWorkOrderResponse> getPopWorkOrders(Long workProcessId) throws NotFoundException {
        WorkProcess workProcess = getWorkProcessIdOrThrow(workProcessId);
        LocalDate now = LocalDate.now();
        // 조건: 작업예정일이 오늘, 작업공정, 해당유저
        List<PopWorkOrderResponse> todayWorkOrders = workOrderDetailRepository.findPopWorkOrderResponsesByCondition(workProcessId, now);

        for (PopWorkOrderResponse todayWorkOrder : todayWorkOrders) {
            // 제조오더의 품목정보에 해당하고, 검색공정과 같고, 반제품인 bomItemDetail 을 가져옴
            Item item = workOrderDetailRepository.findBomDetailByBomMasterItemIdAndWorkProcessId(todayWorkOrder.getItemId(), workProcess.getId())
                    .orElseThrow(() -> new NotFoundException("공정에 맞는 반제품을 찾을 수 없습니다."));

            todayWorkOrder.setItemId(item.getId());
            todayWorkOrder.setItemNo(item.getItemNo());
            todayWorkOrder.setItemName(item.getItemName());
            todayWorkOrder.setUnitCode(item.getUnit().getUnitCode());
        }
        return todayWorkOrders;
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

    private WorkProcess getWorkProcessIdOrThrow(Long id) throws NotFoundException {
        return workProcessRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("workProcess does not exist. input id: " + id));
    }
}
