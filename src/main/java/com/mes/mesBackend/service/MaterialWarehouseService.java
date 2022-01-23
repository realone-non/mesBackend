package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.MaterialStockInspectRequestRequest;
import com.mes.mesBackend.dto.request.RequestMaterialStockInspect;
import com.mes.mesBackend.dto.response.*;
import com.mes.mesBackend.entity.MaterialStockInspect;
import com.mes.mesBackend.entity.MaterialStockInspectRequest;
import com.mes.mesBackend.entity.enumeration.InspectionType;
import com.mes.mesBackend.exception.NotFoundException;
import jdk.vm.ci.meta.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

public interface MaterialWarehouseService {
    //수불부 조회
    List<ReceiptAndPaymentResponse> getReceiptAndPaymentList(Long warehouseId, Long itemAccountId, LocalDate fromDate, LocalDate toDate);
    //재고실사의뢰 등록
    MaterialStockInspectRequestResponse createMaterialStockInspectRequest(MaterialStockInspectRequestRequest request) throws NotFoundException;
    //재고실사의뢰 조회
    List<MaterialStockInspectRequestResponse> getMaterialStockInspectRequestList(LocalDate fromDate, LocalDate toDate);
    //재고실사의뢰 단건조회
    MaterialStockInspectRequestResponse getMaterialStockInspectRequest(Long id) throws NotFoundException;
    //재고실사의뢰 수정
    MaterialStockInspectRequestResponse modifyMaterialStockInspect(Long id, MaterialStockInspectRequestRequest request) throws NotFoundException;
    //재고실사의뢰 삭제
    void deleteMaterialStockInspectRequest (Long id) throws NotFoundException;
    //재고실사 조회
    List<MaterialStockInspectResponse> getMaterialStockInspects(Long requestId, LocalDate fromDate, LocalDate toDate, String itemAccount);
    //재고조사 단일 조회
    MaterialStockInspectResponse getMaterialStockInspect(Long requestId, Long id) throws NotFoundException;
    //DB재고실사 데이터 등록
    void createMaterialStockInspect (Long requestId, Long itemAccountId) throws NotFoundException;
    //재고조사 수정
    List<MaterialStockInspectResponse> modifyMaterialStockInspect(Long requestId, List<RequestMaterialStockInspect> requestList)
            throws NotFoundException;
    //재고실사 삭제
    void deleteMaterialStockInspect(Long id) throws NotFoundException;
    //재고실사 승인 등록
    List<MaterialStockInspectResponse> createStockInspectApproval(Long requestId, Long userId) throws NotFoundException;
    //재고현황 조회
    List<MaterialStockReponse> getMaterialStock(Long itemAccountId, Long itemId, Long itemAccoutCodeId, Long warehouseId, List<Long> warehouseList);
    //헤더용 창고 목록 조회
    List<HeaderWarehouseResponse> getHeaderWarehouse();
}
