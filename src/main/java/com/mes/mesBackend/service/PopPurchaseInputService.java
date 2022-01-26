package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.PopPurchaseOrderResponse;
import com.mes.mesBackend.dto.response.PopPurchaseRequestResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;

import java.util.List;

public interface PopPurchaseInputService {
    // 구매발주 등록이 완료 된 구매발주 리스트 GET
    List<PopPurchaseOrderResponse> getPurchaseOrders();
    // 구매발주에 등록 된 구매요청 리스트 GET
    List<PopPurchaseRequestResponse> getPurchaseRequests(Long id);
    // 구매요청에 대한 구매입고(request: 수량) POST
    Long createPurchaseInput(Long purchaseRequestId, int inputAmount) throws NotFoundException, BadRequestException;
}
