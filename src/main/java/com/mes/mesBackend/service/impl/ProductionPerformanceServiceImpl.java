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
            return responses.stream().filter(f -> f.getItemNo().contains(itemNoOrItemName) || f.getItemName().contains(itemNoOrItemName)).collect(Collectors.toList());
        } else
            return responses;

//        List<ProductionPerformanceResponse> responses = produceOrderRepository.findProductionPerformanceResponseByCondition(fromDate, toDate, itemGroupId, itemNoOrItemName);
//
//        for (ProductionPerformanceResponse r : responses) {
//            r.setStartMaterialMixing(workOrderDetailRepository.findWorkOrderStartDateByProduceOrderIdAndWorkProcessDivision(r.getId(), MATERIAL_MIXING));
//            r.setMaterialMixing(workOrderDetailRepository.findWorkOrderEndDateByProduceOrderIdAndWorkProcessDivision(r.getId(), MATERIAL_MIXING));
//            r.setFilling(workOrderDetailRepository.findWorkOrderEndDateByProduceOrderIdAndWorkProcessDivision(r.getId(), FILLING));
//            r.setCapAssembly(workOrderDetailRepository.findWorkOrderEndDateByProduceOrderIdAndWorkProcessDivision(r.getId(), CAP_ASSEMBLY));
//            r.setLabeling(workOrderDetailRepository.findWorkOrderEndDateByProduceOrderIdAndWorkProcessDivision(r.getId(), LABELING));
//            r.setPackaging(workOrderDetailRepository.findWorkOrderEndDateByProduceOrderIdAndWorkProcessDivision(r.getId(), PACKAGING));
//            Integer packagingProductAmount = workOrderDetailRepository.findPackagingProductAmountByProduceOrderId(r.getId()).orElse(0);
//            r.setProductionAmount(packagingProductAmount);
//        }
//
//        // 조회기간 기준: 원료혼합 공정 완료날짜와 충진공정 완료날짜의 사이에 있는거
//        if (fromDate != null && toDate == null) {
//            LocalDateTime fromDateTime = fromDate.atStartOfDay();
//            return responses.stream().filter(f -> f.getStartMaterialMixing() != null && f.getStartMaterialMixing().isAfter(fromDateTime)).collect(Collectors.toList());
//        } else if (fromDate != null && toDate != null) {
//            LocalDateTime fromDateTime = fromDate.atStartOfDay();
//            LocalDateTime toDateTime = LocalDateTime.of(toDate, LocalTime.MAX).withNano(0);
//            return responses.stream().filter(
//                    f -> (f.getStartMaterialMixing() != null && f.getStartMaterialMixing().isAfter(fromDateTime)) && (f.getPackaging() != null && f.getPackaging().isBefore(toDateTime))
//            ).collect(Collectors.toList());
//        } else if (fromDate == null && toDate != null) {
//            LocalDateTime toDateTime = LocalDateTime.of(toDate, LocalTime.MAX).withNano(0);
//            return responses.stream().filter(f -> f.getPackaging() != null && f.getPackaging().isBefore(toDateTime)).collect(Collectors.toList());
//        } else {
//            return responses;
//        }
//        return productionPerformanceRepository.findProductionPerformanceResponsesByCondition(fromDate, toDate, itemGroupId, itemNoOrItemName);
    }

}
