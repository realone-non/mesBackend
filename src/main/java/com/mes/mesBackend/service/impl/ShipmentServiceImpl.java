package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.ShipmentItemRequest;
import com.mes.mesBackend.dto.request.ShipmentRequest;
import com.mes.mesBackend.dto.response.ShipmentItemResponse;
import com.mes.mesBackend.dto.response.ShipmentResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.ShipmentRepository;
import com.mes.mesBackend.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.mes.mesBackend.helper.Constants.YYMMDDHHMMSSSS;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {
    private final ShipmentRepository shipmentRepo;
    private final ModelMapper mapper;
    private final ClientService clientService;
    private final UserService userService;
    private final WareHouseService wareHouseService;
    private final CurrencyService currencyService;

    // ====================================================== 출하 ======================================================
    // 출하 생성
    @Override
    public ShipmentResponse createShipment(ShipmentRequest shipmentRequest) throws NotFoundException {
        Client client = clientService.getClientOrThrow(shipmentRequest.getClient());
        User user = shipmentRequest.getUser() != null ? userService.getUserOrThrow(shipmentRequest.getUser()) : null;
        WareHouse wareHouse = shipmentRequest.getWareHouse() != null ? wareHouseService.getWareHouseOrThrow(shipmentRequest.getWareHouse()) : null;
        Currency currency = shipmentRequest.getCurrency() != null ? currencyService.getCurrencyOrThrow(shipmentRequest.getCurrency()) : null;
        Shipment shipment = mapper.toEntity(shipmentRequest, Shipment.class);
        shipment.add(client, user, wareHouse, currency);
        String shipmentNo = createShipmentNo();
        shipment.setShipmentNo(shipmentNo);
        shipmentRepo.save(shipment);
        return mapper.toResponse(shipment, ShipmentResponse.class);
    }

    // 출하 단일 조회
    @Override
    public ShipmentResponse getShipment(Long shipmentId) throws NotFoundException {
        Shipment shipment = getShipmentOrThrow(shipmentId);
        return mapper.toResponse(shipment, ShipmentResponse.class);
    }
    // 출하 리스트 조회 검색조건 : 거래처 명, 출하기간, 화폐 id, 담당자 명
    @Override
    public List<ShipmentResponse> getShipments(String clientName, LocalDate fromDate, LocalDate toDate, Long currencyId, String userName) {
        List<Shipment> shipments = shipmentRepo.findAllCondition(clientName, fromDate, toDate, currencyId, userName);
        return mapper.toListResponses(shipments, ShipmentResponse.class);
    }
    // 출하 수정
    @Override
    public ShipmentResponse updateShipment(Long shipmentId, ShipmentRequest newShipmentRequest) throws NotFoundException {
        Shipment findShipment = getShipmentOrThrow(shipmentId);
        Client newClient = clientService.getClientOrThrow(newShipmentRequest.getClient());
        User newUser = newShipmentRequest.getUser() != null ? userService.getUserOrThrow(newShipmentRequest.getUser()) : null;
        WareHouse newWareHouse = newShipmentRequest.getWareHouse() != null ? wareHouseService.getWareHouseOrThrow(newShipmentRequest.getWareHouse()) : null;
        Currency newCurrency =  newShipmentRequest.getCurrency() != null ? currencyService.getCurrencyOrThrow(newShipmentRequest.getCurrency()) : null;
        Shipment newShipment = mapper.toEntity(newShipmentRequest, Shipment.class);
        findShipment.update(newShipment, newClient, newUser, newWareHouse, newCurrency);
        shipmentRepo.save(findShipment);
        return mapper.toResponse(findShipment, ShipmentResponse.class);
    }

    // 출하 삭제
    // 다른 출하 부분 구현 후 삭제 기능 다시 구현
    @Override
    public void deleteShipment(Long shipmentId) throws NotFoundException {
        Shipment shipment = getShipmentOrThrow(shipmentId);
        shipment.delete();
        shipmentRepo.save(shipment);
    }

    // 출하 단일 조회 및 예외
    private Shipment getShipmentOrThrow(Long id) throws NotFoundException {
        return shipmentRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("shipment does not exist. input id: " + id));
    }

    // 출하 번호 생성
    private String createShipmentNo() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(YYMMDDHHMMSSSS));
    }

    // =================================================== 출하 품목 ====================================================
    // 출하 품목정보 생성
    @Override
    public ShipmentItemResponse createShipmentItem(Long shipmentId, ShipmentItemRequest shipmentItemRequest) {
        return null;
    }
    // 출하 품목정보 단일 조회
    @Override
    public ShipmentItemResponse getShipmentItem(Long shipmentId, Long shipmentItemId) {
        return null;
    }
    // 출하 품목 정보 전체조회
    @Override
    public List<ShipmentItemResponse> getShipmentItems(Long shipmentId) {
        return null;
    }
    // 출하 품목정보 수정
    @Override
    public ShipmentItemResponse updateShipmentItem(Long shipmentId, Long shipmentItemId, ShipmentItemRequest shipmentItemRequest) {
        return null;
    }
    // 출하 품목정보 삭제
    @Override
    public void deleteShipmentItem(Long shipmentId, Long shipmentItemId) {

    }
}
