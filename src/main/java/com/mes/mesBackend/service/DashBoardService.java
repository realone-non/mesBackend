package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.ItemInventoryStatusResponse;
import com.mes.mesBackend.dto.response.OperationStatusResponse;
import com.mes.mesBackend.dto.response.SalesRelatedStatusResponse;
import com.mes.mesBackend.dto.response.WorkProcessStatusResponse;
import com.mes.mesBackend.entity.enumeration.GoodsType;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;

import java.util.List;

// 대시보드
public interface DashBoardService {
    // 생산현황, 수주현황, 출하현황, 출하완료 갯수 조회
    OperationStatusResponse getOperationStatus();
    // 작업 공정 별 정보
    WorkProcessStatusResponse getWorkProcessStatus(WorkProcessDivision workProcessDivision);
    // 품목계정 별 재고현황 정보
    List<ItemInventoryStatusResponse> getItemInventoryStatusResponse(GoodsType goodsType);
    // 매출관련현황 - 수주
    List<SalesRelatedStatusResponse> getContractSaleRelatedStatus();
    // 매출관련현황 - 제품 생산
    List<SalesRelatedStatusResponse> getProductSaleRelatedStatus();
    // 매출관련현황 - 제품출고
    List<SalesRelatedStatusResponse> getShipmentSaleRelatedStatus();
}
