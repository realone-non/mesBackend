package com.mes.mesBackend.service;

import com.google.gson.JsonArray;
import com.mes.mesBackend.dto.response.ItemInventoryStatusResponse;
import com.mes.mesBackend.dto.response.OperationStatusResponse;
import com.mes.mesBackend.dto.response.WorkProcessStatusResponse;
import com.mes.mesBackend.entity.enumeration.GoodsType;

import java.util.List;

// 대시보드
public interface DashBoardService {
    // 생산현황, 수주현황, 출하현황, 출하완료 갯수 조회
    OperationStatusResponse getOperationStatus();
    // 작업 공정 별 정보
    WorkProcessStatusResponse getWorkProcessStatus();
    // 품목계정 별 재고현황 정보
    List<ItemInventoryStatusResponse> getItemInventoryStatusResponse(GoodsType goodsType);
    // 매출관련현황 - 수주
    JsonArray getContractSaleRelatedStatus();
    // 매출관련현황 - 제품 생산
    JsonArray getProductSaleRelatedStatus();
    // 매출관련현황 - 제품출고
    JsonArray getShipmentSaleRelatedStatus();
}
