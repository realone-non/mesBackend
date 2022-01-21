//package com.mes.mesBackend.service.impl;
//
//import com.mes.mesBackend.dto.request.ShipmentItemRequest;
//import com.mes.mesBackend.dto.request.ShipmentRequest;
//import com.mes.mesBackend.dto.response.ShipmentItemResponse;
//import com.mes.mesBackend.dto.response.ShipmentResponse;
//import com.mes.mesBackend.entity.*;
//import com.mes.mesBackend.exception.NotFoundException;
//import com.mes.mesBackend.helper.NumberAutomatic;
//import com.mes.mesBackend.mapper.ModelMapper;
//import com.mes.mesBackend.repository.ShipmentRepository;
//import com.mes.mesBackend.service.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//
//// 4-5. 출하등록
//@Service
//@RequiredArgsConstructor
//public class ShipmentServiceImpl implements ShipmentService {
//    private final ShipmentRepository shipmentRepo;
//    private final ModelMapper mapper;
//    private final ClientService clientService;
//    private final NumberAutomatic numberAutomatic;
//
//    // ====================================================== 출하 ======================================================
//    // 출하 생성
//    @Override
//    public ShipmentResponse createShipment(ShipmentRequest shipmentRequest) throws NotFoundException {
//        Client client = clientService.getClientOrThrow(shipmentRequest.getClient());
//
//        Shipment shipment = mapper.toEntity(shipmentRequest, Shipment.class);
//        shipment.add(client);
//        String shipmentNo = numberAutomatic.createDateTimeNo();     // 수주번호 생성
//        shipment.setShipmentNo(shipmentNo);
//
//        shipmentRepo.save(shipment);
//        return mapper.toResponse(shipment, ShipmentResponse.class);
//    }
//
//    // 출하 단일 조회
//    @Override
//    public ShipmentResponse getShipment(Long shipmentId) throws NotFoundException {
//        Shipment shipment = getShipmentOrThrow(shipmentId);
//        return mapper.toResponse(shipment, ShipmentResponse.class);
//    }
//
//    // 출하 리스트 조회 검색조건 : 거래처 명, 출하기간, 화폐 id, 담당자 명
//    @Override
//    public List<ShipmentResponse> getShipments(
//            String clientName,
//            LocalDate fromDate,
//            LocalDate toDate,
//            Long currencyId,
//            String userName
//    ) {
//        List<Shipment> shipments = shipmentRepo.findShipmentResponsesByCondition(clientName, fromDate, toDate, currencyId, userName);
//        return mapper.toListResponses(shipments, ShipmentResponse.class);
//    }
//    // 출하 수정
//    @Override
//    public ShipmentResponse updateShipment(Long shipmentId, ShipmentRequest newShipmentRequest) throws NotFoundException {
//        Shipment findShipment = getShipmentOrThrow(shipmentId);
//        Client newClient = clientService.getClientOrThrow(newShipmentRequest.getClient());
//
//        Shipment newShipment = mapper.toEntity(newShipmentRequest, Shipment.class);
//        findShipment.update(newShipment, newClient);
//        shipmentRepo.save(findShipment);
//        return mapper.toResponse(findShipment, ShipmentResponse.class);
//    }
//
//    // 출하 삭제
//    // 다른 출하 부분 구현 후 삭제 기능 다시 구현
//    @Override
//    public void deleteShipment(Long shipmentId) throws NotFoundException {
//        Shipment shipment = getShipmentOrThrow(shipmentId);
//        shipment.delete();
//        shipmentRepo.save(shipment);
//    }
//
//    // 출하 단일 조회 및 예외
//    private Shipment getShipmentOrThrow(Long id) throws NotFoundException {
//        return shipmentRepo.findByIdAndDeleteYnFalse(id)
//                .orElseThrow(() -> new NotFoundException("shipment does not exist. input id: " + id));
//    }
//
//
//    // =================================================== 출하 품목 ====================================================
//    // 출하 품목정보 생성
//    @Override
//    public ShipmentItemResponse createShipmentItem(Long shipmentId, ShipmentItemRequest shipmentItemRequest) {
//        return null;
//    }
//    // 출하 품목정보 단일 조회
//    @Override
//    public ShipmentItemResponse getShipmentItem(Long shipmentId, Long shipmentItemId) {
//        return null;
//    }
//    // 출하 품목 정보 전체조회
//    @Override
//    public List<ShipmentItemResponse> getShipmentItems(Long shipmentId) {
//        return null;
//    }
//    // 출하 품목정보 수정
//    @Override
//    public ShipmentItemResponse updateShipmentItem(Long shipmentId, Long shipmentItemId, ShipmentItemRequest shipmentItemRequest) {
//        return null;
//    }
//    // 출하 품목정보 삭제
//    @Override
//    public void deleteShipmentItem(Long shipmentId, Long shipmentItemId) {
//
//    }
//
//    // =================================================== 출하 품목 ====================================================
//}
