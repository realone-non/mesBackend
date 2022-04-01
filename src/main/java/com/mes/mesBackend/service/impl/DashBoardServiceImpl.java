package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.ItemInventoryStatusResponse;
import com.mes.mesBackend.dto.response.OperationStatusResponse;
import com.mes.mesBackend.dto.response.SalesRelatedStatusResponse;
import com.mes.mesBackend.dto.response.WorkProcessStatusResponse;
import com.mes.mesBackend.entity.enumeration.GoodsType;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import com.mes.mesBackend.repository.ProduceOrderRepository;
import com.mes.mesBackend.repository.WorkOrderDetailRepository;
import com.mes.mesBackend.service.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// 대시보드
@Service
@RequiredArgsConstructor
public class DashBoardServiceImpl implements DashBoardService {
    private final ProduceOrderRepository produceOrderRepo;
    private final WorkOrderDetailRepository workOrderDetailRepo;


    // 생산현황, 수주현황, 출하현황, 출하완료 갯수 조회
    @Override
    public OperationStatusResponse getOperationStatus() {
        OperationStatusResponse response = new OperationStatusResponse();

//        response.setProductAmount();
        return null;
    }

    // 작업 공정 별 정보
    @Override
    public WorkProcessStatusResponse getWorkProcessStatus(WorkProcessDivision workProcessDivision) {
        return null;
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
