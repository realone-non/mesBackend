package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.ShipmentItemRequest;
import com.mes.mesBackend.dto.request.ShipmentRequest;
import com.mes.mesBackend.dto.response.ShipmentItemResponse;
import com.mes.mesBackend.dto.response.ShipmentResponse;
import com.mes.mesBackend.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

// 4-5. 출하 등록
public interface ShipmentService {
    // ====================================================== 출하 ======================================================
    // 출하 생성
    ShipmentResponse createShipment(ShipmentRequest shipmentRequest) throws NotFoundException;
    // 출하 단일 조회
    ShipmentResponse getShipment(Long shipmentId) throws NotFoundException;
    // 출하 리스트 조회 검색조건 : 거래처 명, 출하기간, 화폐 id, 담당자 명
    List<ShipmentResponse> getShipments(String clientName, LocalDate fromDate, LocalDate toDate, Long currencyId, String userName);
    // 출하 수정
    ShipmentResponse updateShipment(Long shipmentId, ShipmentRequest shipmentRequest) throws NotFoundException;
    // 출하 삭제
    void deleteShipment(Long shipmentId) throws NotFoundException;

    // =================================================== 출하 수주 품목 ====================================================
    // 출하 품목정보 생성
    ShipmentItemResponse createShipmentItem(Long shipmentId, ShipmentItemRequest shipmentItemRequest);
    // 출하 품목정보 단일 조회
    ShipmentItemResponse getShipmentItem(Long shipmentId, Long shipmentItemId);
    // 출하 품목 정보 전체조회
    List<ShipmentItemResponse> getShipmentItems(Long shipmentId);
    // 출하 품목정보 수정
    ShipmentItemResponse updateShipmentItem(Long shipmentId, Long shipmentItemId, ShipmentItemRequest shipmentItemRequest);
    // 출하 품목정보 삭제
    void deleteShipmentItem(Long shipmentId, Long shipmentItemId);
}
