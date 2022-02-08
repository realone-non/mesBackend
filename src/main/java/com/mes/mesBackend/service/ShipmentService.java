package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.ShipmentCreateRequest;
import com.mes.mesBackend.dto.request.ShipmentUpdateRequest;
import com.mes.mesBackend.dto.response.*;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

// 4-5. 출하 등록
public interface ShipmentService {
    // ====================================================== 출하 ======================================================
    // 출하 생성
    ShipmentResponse createShipment(ShipmentCreateRequest shipmentRequest) throws NotFoundException;
    // 출하 단일 조회
    ShipmentResponse getShipmentResponse(Long shipmentId) throws NotFoundException;
    // 출하 리스트 조회 검색조건 : 거래처 id, 출하기간, 화폐 id, 담당자 id
    List<ShipmentResponse> getShipments(Long clientId, LocalDate fromDate, LocalDate toDate, Long currencyId, Long userId);
    // 출하 수정
    ShipmentResponse updateShipment(Long shipmentId, ShipmentUpdateRequest shipmentUpdateRequest) throws NotFoundException, BadRequestException;
    // 출하 삭제
    void deleteShipment(Long shipmentId) throws NotFoundException, BadRequestException;

    // =================================================== 출하 수주 품목 ====================================================
    // 출하 품목정보 생성
    ShipmentItemResponse createShipmentItem(Long shipmentId, Long contractItemId, String note) throws NotFoundException, BadRequestException;
    // 출하 품목정보 단일 조회
    ShipmentItemResponse getShipmentItemResponse(Long shipmentId, Long shipmentItemId) throws NotFoundException;
    // 출하 품목 정보 전체조회
    List<ShipmentItemResponse> getShipmentItem(Long shipmentId);
    // 출하 품목정보 수정
    ShipmentItemResponse updateShipmentItem(Long shipmentId, Long shipmentItemId, Long contractItemId, String note) throws NotFoundException, BadRequestException;
    // 출하 품목정보 삭제
    void deleteShipmentItem(Long shipmentId, Long shipmentItemId) throws NotFoundException, BadRequestException;

    // ==================================================== 출하 LOT 정보 ====================================================
    // LOT 정보 생성
    ShipmentLotInfoResponse createShipmentLot(Long shipmentId, Long shipmentItemId, Long lotMasterId) throws NotFoundException, BadRequestException;
    // LOT 정보 전체 조회
    List<ShipmentLotInfoResponse> getShipmentLots(Long shipmentId, Long shipmentItemId) throws NotFoundException;
    // 출하 LOT 정보 삭제
    void deleteShipmentLot(Long shipmentId, Long shipmentItemId, Long shipmentLotId) throws NotFoundException, BadRequestException;
    // 출하 LOT 정보 생성 시 LOT 정보 조회 API
    List<LotMasterResponse.idAndLotNo> getShipmentLotMasters(Long contractItemId, int notShippedAmount) throws NotFoundException;
    // ==================================================== 4-7. 출하 현황 ====================================================
    // 출하현황 검색 리스트 조회, 검색조건: 거래처 id, 출하기간 fromDate~toDate, 화폐 id, 담당자 id, 품번|품명
    List<ShipmentStatusResponse> getShipmentStatuses(Long clientId, LocalDate fromDate, LocalDate toDate, Long currencyId, Long userId, String itemNoAndItemName);
}
