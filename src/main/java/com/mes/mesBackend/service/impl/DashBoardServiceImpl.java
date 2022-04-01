package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.ItemInventoryStatusResponse;
import com.mes.mesBackend.dto.response.OperationStatusResponse;
import com.mes.mesBackend.dto.response.SalesRelatedStatusResponse;
import com.mes.mesBackend.dto.response.WorkProcessStatusResponse;
import com.mes.mesBackend.entity.enumeration.GoodsType;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import com.mes.mesBackend.repository.ContractRepository;
import com.mes.mesBackend.repository.ProduceOrderRepository;
import com.mes.mesBackend.repository.ShipmentRepository;
import com.mes.mesBackend.repository.WorkOrderDetailRepository;
import com.mes.mesBackend.service.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mes.mesBackend.entity.enumeration.OrderState.COMPLETION;
import static com.mes.mesBackend.entity.enumeration.OrderState.ONGOING;

// 대시보드
@Service
@RequiredArgsConstructor
public class DashBoardServiceImpl implements DashBoardService {
    private final WorkOrderDetailRepository workOrderDetailRepo;
    private final ContractRepository contractRepo;
    private final ShipmentRepository shipmentRepo;

    // 생산현황, 수주현황, 출하현황, 출하완료 갯수 조회
    @Override
    public OperationStatusResponse getOperationStatus() {
        OperationStatusResponse response = new OperationStatusResponse();

        // 생산현황(작업오더 수)
        Long ongoingProduceOrderAmount = workOrderDetailRepo.findProduceOrderStateOngoingProductionAmountSum().orElse(0L);
        response.setOngoingProduceOrderAmount(ongoingProduceOrderAmount.intValue());

        // 납기일자 오늘 이후로 남은거 갯수
        Long contractAmount = contractRepo.findContractPeriodDateByTodayAmountSum().orElse(0L);
        response.setContractAmount(contractAmount.intValue());

        // 출하일자가 오늘인거
        Long shipmentTodayAmount = shipmentRepo.findShipmentCountByToday(null).orElse(0L);
        response.setShipmentSchedule(shipmentTodayAmount.intValue());

        // 출하 일자가 오늘이고 출하 상태 값이 완료인 경우
        Long shipmentCompletionAmount = shipmentRepo.findShipmentCountByToday(COMPLETION).orElse(0L);
        response.setShipmentCompletionAmount(shipmentCompletionAmount.intValue());
        return response;
    }

    // 작업 공정 별 정보
    @Override
    public WorkProcessStatusResponse getWorkProcessStatus(WorkProcessDivision workProcessDivision) {
        WorkProcessStatusResponse response = new WorkProcessStatusResponse();

        // 작업진행
        Long ongoingAmount = workOrderDetailRepo.findOrderStateCountByWorkProcessDivisionAndOrderState(workProcessDivision, ONGOING).orElse(0L);
        response.setOngoingAmount(ongoingAmount);

        // 작업완료
        Long completionAmount = workOrderDetailRepo.findOrderStateCountByWorkProcessDivisionAndOrderState(workProcessDivision, COMPLETION).orElse(0L);
        response.setCompletionAmount(completionAmount);

        // 수량
        Integer productionAmount = workOrderDetailRepo.findProductionAmountByWorkProcessDivision(workProcessDivision);
        response.setProductionAmount(productionAmount);
        return response;
    }

    // 품목계정 별 재고현황 정보
    @Override
    public List<ItemInventoryStatusResponse> getItemInventoryStatusResponse(GoodsType goodsType) {
        return null;
    }

    // 매출관련현황 - 수주
    @Override
    public List<SalesRelatedStatusResponse> getContractSaleRelatedStatus() {
        return null;
    }

    // 매출관련현황 - 제품 생산
    @Override
    public List<SalesRelatedStatusResponse> getProductSaleRelatedStatus() {
        return null;
    }

    // 매출관련현황 - 제품출고
    @Override
    public List<SalesRelatedStatusResponse> getShipmentSaleRelatedStatus() {
        return null;
    }
}
