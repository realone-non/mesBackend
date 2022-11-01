package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.BadItemWorkOrderResponse;
import com.mes.mesBackend.dto.response.ProductionPerformanceResponse;
import com.mes.mesBackend.entity.LotLog;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.repository.LotLogRepository;
import com.mes.mesBackend.repository.LotMasterRepository;
import com.mes.mesBackend.repository.ProduceOrderRepository;
import com.mes.mesBackend.repository.WorkOrderDetailRepository;
import com.mes.mesBackend.service.ProductionPerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.mes.mesBackend.entity.enumeration.WorkProcessDivision.*;

// 8-6. 생산실적 관리
@Service
@RequiredArgsConstructor
public class ProductionPerformanceServiceImpl implements ProductionPerformanceService {
    private final ProduceOrderRepository produceOrderRepository;
    private final WorkOrderDetailRepository workOrderDetailRepository;
    private final LotLogRepository lotLogRepository;
    private final LotMasterRepository lotMasterRepository;
    // 생산실적 리스트 조회, 검색조건: 조회기간 fromDate~toDate, 작업공정 id
    @Override
    public List<ProductionPerformanceResponse> getProductionPerformances(
            LocalDate fromDate,
            LocalDate toDate,
            Long inputWorkProcessId,
            String itemNoOrItemName
    ) throws NotFoundException {
        List<ProductionPerformanceResponse> responses = workOrderDetailRepository.findProductionPerformanceResponseByCondition(fromDate, toDate, inputWorkProcessId);

        for (ProductionPerformanceResponse r : responses) {
            Long workOrderId = r.getWorkOrderId();
            Long workProcessId = r.getWorkProcessId();
            LotLog lotLog = lotLogRepository.findLotLogByWorkOrderIdAndWorkProcessId(workOrderId, workProcessId)
                    .orElseThrow(() -> new NotFoundException("[데이터오류] 공정 완료된 작업지시가 LotLog 에 등록되지 않았습니다."));

            Long dummyLotId = lotLog.getLotMaster().getId();
            BadItemWorkOrderResponse.subDto subDto = lotMasterRepository.findLotMaterByDummyLotIdAndWorkProcessId(dummyLotId, workProcessId)
                    .orElseThrow(() -> new NotFoundException("[데이터오류] lotLog 에 등록된 lotMaster(id: " + dummyLotId + ") 가 lotEquipmentConnect parentLot 로 등록되지 않았습니다."));
            r.set(subDto);
        }

        if (itemNoOrItemName != null) {
            return responses.stream().filter(f -> f.getItemNo().contains(itemNoOrItemName) || f.getItemName().contains(itemNoOrItemName))
                    .map(m -> m.setList(m))
                    .collect(Collectors.toList());
        } else
            return responses.stream().filter(f -> f.getCostTime() != null)
                    .map(m -> m.setList(m))
                    .collect(Collectors.toList());        // 221101 costTime null 인건 제외
    }
}
