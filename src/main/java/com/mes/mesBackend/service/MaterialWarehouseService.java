package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.MaterialStockInspectRequestRequest;
import com.mes.mesBackend.dto.response.MaterialStockInspectRequestResponse;
import com.mes.mesBackend.dto.response.ReceiptAndPaymentResponse;
import com.mes.mesBackend.entity.MaterialStockInspectRequest;
import com.mes.mesBackend.exception.NotFoundException;
import jdk.vm.ci.meta.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

public interface MaterialWarehouseService {
    //수불부 조회
    List<ReceiptAndPaymentResponse> getReceiptAndPaymentList(Long warehouseId, Long itemAccountId, LocalDate fromDate, LocalDate toDate);
    //재고실사의뢰 등록
    MaterialStockInspectRequestResponse createMaterialStockInspect(MaterialStockInspectRequestRequest request) throws NotFoundException;
    //재고실사의뢰 조회
    List<MaterialStockInspectRequestResponse> getMaterialStockInspectList(LocalDate fromDate, LocalDate toDate);
    //재고실사의뢰 단건조회
    MaterialStockInspectRequestResponse getMaterialStockInspect(Long id) throws NotFoundException;
    //재고실사의뢰 수정
    MaterialStockInspectRequestResponse modifyMaterialStockInspect(Long id, MaterialStockInspectRequestRequest request) throws NotFoundException;
    //재고실사의뢰 삭제
    void deleteMaterialStockInspect (Long id) throws NotFoundException;
}
