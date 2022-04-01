package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.WorkOrderStateDetailResponse;
import com.mes.mesBackend.dto.response.WorkOrderStateResponse;
import com.mes.mesBackend.entity.Item;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.repository.ItemRepository;
import com.mes.mesBackend.repository.WorkOrderDetailRepository;
import com.mes.mesBackend.service.WorkOrderStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.mes.mesBackend.entity.enumeration.WorkProcessDivision.PACKAGING;

// 8-1. 작지상태 확인
@Service
@RequiredArgsConstructor
public class WorkOrderStateServiceImpl implements WorkOrderStateService {
    private final WorkOrderDetailRepository workOrderDetailRepo;
    private final ItemRepository itemRepository;

    // 쟉업지시 정보 조회 , 검색조건: 작업장 id, 작업라인 id, 제조오더번호, 품목계정 id, 지시상태, 작업기간 fromDate~toDate, 수주번호
    @Override
    public List<WorkOrderStateResponse> getWorkOrderStates(
            Long workProcessId,
            Long workLineId,
            String produceOrderNo,
            Long itemAccountId,
            OrderState orderState,
            LocalDate fromDate,
            LocalDate toDate,
            String contractNo
    ) {
        List<WorkOrderStateResponse> responses = workOrderDetailRepo.findWorkOrderStateResponsesByCondition(workProcessId, workLineId, produceOrderNo, itemAccountId, orderState, fromDate, toDate, contractNo);
        for (WorkOrderStateResponse response : responses) {
            Item item = response.getWorkProcessDivision().equals(PACKAGING) ? getItemOrNull(response.getItemId())
                    : workOrderDetailRepo.findBomDetailHalfProductByBomMasterItemIdAndWorkProcessId(response.getItemId(), response.getWorkProcessId(), null)
                    .orElse(null);
            if (item != null) response.setItems(item);
        }

        if (itemAccountId != null) {
            return responses.stream().filter(f -> f.getItemAccountId().equals(itemAccountId)).collect(Collectors.toList());
        } else {
            return responses;
        }
    }

    // 품목 조회 및 없으면 null
    private Item getItemOrNull(Long id) {
        return itemRepository.findByIdAndDeleteYnFalse(id).orElse(null);
    }

    // 작업지시 단일 조회
    @Override
    public WorkOrderStateResponse getWorkOrderState(Long workOrderId) throws NotFoundException {
        return workOrderDetailRepo.findWorkOrderStateResponseById(workOrderId)
                .orElseThrow(() -> new NotFoundException("workOrder does not exist. input workOrderId: " + workOrderId));
    }

    // 작업지시 상태 이력 정보 리스트 조회
    @Override
    public WorkOrderStateDetailResponse getWorkOrderStateDetail(Long workOrderId) {
        return workOrderDetailRepo.findWorkOrderStateDetailById(workOrderId);
    }
}
