package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.ShipmentCreateRequest;
import com.mes.mesBackend.dto.request.ShipmentUpdateRequest;
import com.mes.mesBackend.dto.response.ShipmentItemResponse;
import com.mes.mesBackend.dto.response.ShipmentLotInfoResponse;
import com.mes.mesBackend.dto.response.ShipmentResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.AmountHelper;
import com.mes.mesBackend.helper.LotLogHelper;
import com.mes.mesBackend.helper.NumberAutomatic;
import com.mes.mesBackend.helper.ProductionPerformanceHelper;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.*;
import com.mes.mesBackend.service.ClientService;
import com.mes.mesBackend.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mes.mesBackend.entity.enumeration.ItemLogType.SHIPMENT_AMOUNT;
import static com.mes.mesBackend.entity.enumeration.ItemLogType.STOCK_AMOUNT;
import static com.mes.mesBackend.entity.enumeration.OrderState.COMPLETION;
import static com.mes.mesBackend.entity.enumeration.WorkProcessDivision.PACKAGING;
import static com.mes.mesBackend.entity.enumeration.WorkProcessDivision.SHIPMENT;

// 4-5. 출하등록
@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {
    private final ShipmentRepository shipmentRepo;
    private final ShipmentItemRepository shipmentItemRepo;
    private final ModelMapper mapper;
    private final ClientService clientService;
    private final NumberAutomatic numberAutomatic;
    private final ContractItemRepository contractItemRepo;
    private final ShipmentLotRepository shipmentLotRepo;
    private final LotMasterRepository lotMasterRepository;
    private final InputTestRequestRepository inputTestRequestRepo;
    private final LotLogHelper lotLogHelper;
    private final LotLogRepository lotLogRepo;
    private final WorkProcessRepository workProcessRepo;
    private final ProductionPerformanceHelper productionPerformanceHelper;
    private final AmountHelper amountHelper;

    // ====================================================== 출하 ======================================================
    // 출하 생성
    @Override
    public ShipmentResponse createShipment(ShipmentCreateRequest shipmentRequest) throws NotFoundException {
        Client client = clientService.getClientOrThrow(shipmentRequest.getClient());
        Shipment shipment = mapper.toEntity(shipmentRequest, Shipment.class);

        String shipmentNo = numberAutomatic.createDateTimeNo();     // 출하번호 생성
        shipment.create(client, shipmentNo);       // 지시상태 SCHEDULE

        shipmentRepo.save(shipment);
        return getShipmentResponse(shipment.getId());
    }

    // 출하 단일 조회
    @Override
    public ShipmentResponse getShipmentResponse(Long shipmentId) throws NotFoundException {
        ShipmentResponse shipmentResponse = shipmentRepo.findShipmentResponseById(shipmentId)
                .orElseThrow(() -> new NotFoundException("shipment does not exist. input id: " + shipmentId));

        // shipmentItem 에 제일 첨에 등록된 contractItem 의  contract 조회
        Contract contract = shipmentItemRepo.findContractsByShipmentId(shipmentId).orElse(null);
        return shipmentResponse.addContractInfo(contract);
    }

    // 출하 리스트 조회 검색조건 : 거래처 명, 출하기간, 화폐 id, 담당자 명
    @Override
    public List<ShipmentResponse> getShipments(
            Long clientId,
            LocalDate fromDate,
            LocalDate toDate,
            Long currencyId,
            Long userId
    ) {
        List<ShipmentResponse> shipmentResponses = shipmentRepo.findShipmentResponsesByCondition(clientId, fromDate, toDate, currencyId, userId);
        List<ShipmentResponse> response = new ArrayList<>();
        for (ShipmentResponse res : shipmentResponses) {
            // shipmentItem 에 제일 첨에 등록된 contractItem 의  contract 조회
            Contract contract = shipmentItemRepo.findContractsByShipmentId(res.getId()).orElse(null);
            res.addContractInfo(contract);
            if (currencyId != null) response.add(res.currencyIdEq(currencyId));
            if (userId != null) response.add(res.userIdEq(userId));                   // 담당자 조회
            response.add(res);
        }

        response.remove(null);
        return response;
    }

    // 출하 수정
    @Override
    public ShipmentResponse updateShipment(Long shipmentId, ShipmentUpdateRequest shipmentUpdateRequest) throws NotFoundException, BadRequestException {
        Shipment findShipment = getShipmentOrThrow(shipmentId);

        // shipmentState 가 COMPLETION 이면 수정, 삭제 불가능
        throwIfShipmentStateCompletion(findShipment.getOrderState());

        Shipment newShipment = mapper.toEntity(shipmentUpdateRequest, Shipment.class);
        findShipment.update(newShipment);       // client 수정불가
        shipmentRepo.save(findShipment);
        return getShipmentResponse(findShipment.getId());
    }

    // shipmentState 가 COMPLETION 인것만 수정, 삭제 가능
    private void throwIfShipmentStateCompletion(OrderState orderState) throws BadRequestException {
        if (orderState.equals(COMPLETION)) {
            throw new BadRequestException("출하가 완료된 정보이므로 삭제나 수정이 불가능 합니다.");
        }
    }

    // 출하 삭제
    // 다른 출하 부분 구현 후 삭제 기능 다시 구현
    @Override
    public void deleteShipment(Long shipmentId) throws NotFoundException, BadRequestException {
        Shipment shipment = getShipmentOrThrow(shipmentId);
        throwIfShipmentStateCompletion(shipment.getOrderState());   // shipmentState 가 COMPLETION 이면 수정, 삭제 불가능
        boolean existsByShipmentItemInShipment = shipmentRepo.existsByShipmentItemInShipment(shipmentId);
        if (!existsByShipmentItemInShipment) {
            throw new BadRequestException("등록된 품목정보가 있으면 삭제할 수 없습니다.");
        }

        shipment.delete();
        shipmentRepo.save(shipment);
    }

    // 출하 단일 조회 및 예외
    private Shipment getShipmentOrThrow(Long id) throws NotFoundException {
        return shipmentRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("shipment does not exist. input id: " + id));
    }


    // =================================================== 출하 품목 ====================================================
    // 출하 품목정보 생성
    @Override
    public ShipmentItemResponse createShipmentItem(Long shipmentId, Long contractItemId, String note) throws NotFoundException {
        Shipment shipment = getShipmentOrThrow(shipmentId);
        ContractItem contractItem = getContractItemOrThrow(contractItemId);

        // 여까지 봤음
        ShipmentItem shipmentItem = new ShipmentItem();
        shipmentItem.create(shipment, contractItem, note);
        shipmentItemRepo.save(shipmentItem);
        return getShipmentItemResponse(shipmentId, shipmentItem.getId());
    }

    // 출하 품목정보 단일 조회
    @Override
    public ShipmentItemResponse getShipmentItemResponse(Long shipmentId, Long shipmentItemId) throws NotFoundException {
        return shipmentItemRepo.findShipmentItemResponseByShipmentItemId(shipmentId, shipmentItemId)
                .orElseThrow(() -> new NotFoundException("shipmentItem does not exist. input id: " + shipmentItemId));
    }

    // 출하 품목 정보 전체조회
    @Override
    public List<ShipmentItemResponse> getshipmentItem(Long shipmentId) {
        return shipmentItemRepo.findShipmentResponsesByShipmentId(shipmentId);
    }

    // 출하 품목정보 수정
    /*
    * 수주품목 변경 시 LOT 정보가 있으면 해당 LOT 정보 삭제
    * */
    @Override
    public ShipmentItemResponse updateShipmentItem(Long shipmentId, Long shipmentItemId, Long contractItemId, String note) throws NotFoundException {
        ShipmentItem findShipmentItem = getShipmentItemOrThrow(shipmentId, shipmentItemId);
        if (!findShipmentItem.getContractItem().getId().equals(contractItemId)) {
            // 해당하는 LOT 정보 삭제
        }
        ContractItem newContractItem = getContractItemOrThrow(contractItemId);
        findShipmentItem.update(newContractItem, note);
        shipmentItemRepo.save(findShipmentItem);
        return getShipmentItemResponse(shipmentId, shipmentItemId);
    }

    // 출하 품목정보 삭제
    /*
    * LOT 정보가 있으면 해당 LOT 정보 삭제
    * */
    @Override
    public void deleteShipmentItem(Long shipmentId, Long shipmentItemId) throws NotFoundException {
        ShipmentItem shipmentItem = getShipmentItemOrThrow(shipmentId, shipmentItemId);
        shipmentItem.delete();
        shipmentItemRepo.save(shipmentItem);
    }

    // shipmentItem 단일 조회 및 예외
    private ShipmentItem getShipmentItemOrThrow(Long shipmentId, Long shipmentItemId) throws NotFoundException {
        Shipment shipment = getShipmentOrThrow(shipmentId);
        return shipmentItemRepo.findByIdAndShipmentAndDeleteYnFalse(shipmentItemId, shipment)
                .orElseThrow(() -> new NotFoundException("shipmentItem does not exist. input id: " + shipmentItemId));
    }

    // contractItem 단일 조회 및 예외
    private ContractItem getContractItemOrThrow(Long contractItemId) throws NotFoundException {
        return contractItemRepo.findByIdAndDeleteYnFalse(contractItemId)
                .orElseThrow(() -> new NotFoundException("contractItem does not exist. input id: " + contractItemId));
    }

// =================================================== 출하 LOT 정보 ====================================================
    // LOT 정보 생성
    @Override
    public ShipmentLotInfoResponse createShipmentLot(Long shipmentId, Long shipmentItemId, Long lotMasterId) throws NotFoundException, BadRequestException {
        ShipmentItem shipmentItem = getShipmentItemOrThrow(shipmentId, shipmentItemId);

        // lotMaster: shipmentItem 의 item 에 해당되는 lotMaster 가져옴, 조건? 공정이 포장까지 완료된, stockAmount 가 1 이상
        List<Long> lotMasterIds = shipmentLotRepo.findLotMasterIdByItemIdAndWorkProcessShipment(shipmentItem.getContractItem().getItem().getId(), SHIPMENT);
        boolean lotMasterIdAnyMatch = lotMasterIds.stream().anyMatch(id -> id.equals(lotMasterId));
        if (!lotMasterIdAnyMatch) {
            throw new BadRequestException("입력한 lotMaster id 는 shipmentItem 의 item 에 해당하고, 포장공정까지 완료하고, stockAmount 가 1 이상인 lotMaster 가 아닙니다. " +
                    "입력한 lotMasterId: " + lotMasterId + ", " +
                    "조건에 맞는 lotMasterId: " + lotMasterIds
            );
        }

        LotMaster lotMaster = getLotMasterOrThrow(lotMasterId);

        ShipmentLot shipmentLot = new ShipmentLot();
        shipmentLot.create(shipmentItem, lotMaster);

        // stockAmount  0, stockAmount 만큼 ShipmentAmount 가 바뀜
        int stockAmountLotMaster = lotMaster.getStockAmount();       // lotMaster 재고수량
        lotMaster.setShipmentAmount(stockAmountLotMaster);        // 출하수량 변경(재고수량)
        lotMaster.setStockAmount(0);                                    // 재고수량 0으로 변경

        shipmentLotRepo.save(shipmentLot);

        // lotLog insert
        Long workProcessId = lotLogHelper.getWorkProcessByDivisionOrThrow(SHIPMENT);
        Long workOrderDetailId = lotLogHelper.getWorkOrderDetailByContractItemAndWorkProcess(shipmentItem.getContractItem().getId(), workProcessId);
        lotLogHelper.createLotLog(lotMasterId, workOrderDetailId, workProcessId);

        // amountHelper insert
        amountHelper.amountUpdate(shipmentItem.getContractItem().getItem().getId(), lotMaster.getWareHouse().getId(), null, SHIPMENT_AMOUNT, stockAmountLotMaster, false);
        return getShipmentLotInfoResponse(shipmentLot.getId());
    }

    // LOT 정보 전체 조회
    @Override
    public List<ShipmentLotInfoResponse> getShipmentLots(Long shipmentId, Long shipmentItemId) throws NotFoundException {
        List<ShipmentLotInfoResponse> responses = shipmentLotRepo.findShipmentLotResponsesByShipmentItemId(shipmentItemId);
        for (ShipmentLotInfoResponse res : responses) {
            Long inputTestDetailId = getInputTestIdInLotMasterId(res.getLotId(), res.getShipmentLotId());
            res.setInputTestId(inputTestDetailId);   // lotMaster 에 해당하는 검사번호 찾아서 추가
        }
        return responses;
    }

    // 출하 LOT 정보 삭제
    @Override
    public void deleteShipmentLot(Long shipmentId, Long shipmentItemId, Long shipmentLotId) throws NotFoundException {
        ShipmentItem findShipmentItem = getShipmentItemOrThrow(shipmentId, shipmentItemId);
        ShipmentLot findShipmentLot = getShipmentLotOrThrow(findShipmentItem, shipmentLotId);
        LotMaster lotMaster = findShipmentLot.getLotMaster();
        int shipmentAmount = lotMaster.getShipmentAmount();

        // shipmentLot 삭제
        findShipmentLot.delete();
        // lotMaster 재고수량, 출하수량 변경
        lotMaster.setStockAmount(shipmentAmount);       // 재고수량 -> lotMaster 의 shipmentAmount
        lotMaster.setShipmentAmount(0);                 // 출하수량 -> 0

        shipmentLotRepo.save(findShipmentLot);
        lotMasterRepository.save(lotMaster);

        // lotLog insert
        Long workProcessId = lotLogHelper.getWorkProcessByDivisionOrThrow(PACKAGING);          // 작업공정 division 으로 공정 id 찾음
        Long workOrderDetailId = lotLogHelper.getWorkOrderDetailByContractItemAndWorkProcess(findShipmentItem.getContractItem().getId(), workProcessId);// 수주품목, 작업공정으로 해당 작업지시 정보 가져옴
        lotLogHelper.createLotLog(findShipmentLot.getLotMaster().getId(), workOrderDetailId, workProcessId);
        // amountHelper insert
        amountHelper.amountUpdate(findShipmentItem.getContractItem().getItem().getId(), lotMaster.getWareHouse().getId(), null, STOCK_AMOUNT, shipmentAmount, false);
    }

    // shipmentLot 단일 조회 및 예외
    private ShipmentLot getShipmentLotOrThrow(ShipmentItem shipmentItem, Long shipmentLotId) throws NotFoundException {
        return shipmentLotRepo.findByIdAndShipmentItemAndDeleteYnFalse(shipmentLotId, shipmentItem)
                .orElseThrow(() -> new NotFoundException("shipmentLot does not exist. input shipmentLogId: " + shipmentLotId));
    }

    // lotMaster 에 해당하는 검사번호 찾아서 추가
    private Long getInputTestIdInLotMasterId(Long lotMasterId, Long shipmentLotId) throws NotFoundException {
        return inputTestRequestRepo.findInputTestDetailIdByLotMasterId(lotMasterId)
                .orElseThrow(() -> new NotFoundException("해당 검사번호를 찾을 수 없음. " +
                        "경우1. 검사요청에 대한 검사가 완료되지 않음, " +
                        "경우2. 해당 Lot 에 대한 검사등록이 되지 않음. " +
                        "lotMaster id: " + lotMasterId + ", " +
                        "shipmentLotId: " + shipmentLotId)
                );
    }

    // lotMaster 단일 조회 및 예외
    private LotMaster getLotMasterOrThrow(Long lotMasterId) throws NotFoundException {
        return lotMasterRepository.findByIdAndDeleteYnFalse(lotMasterId)
                .orElseThrow(() -> new NotFoundException("lotMaster does not exist. input id:" + lotMasterId));
    }

    // shipmentLotResponse 단일 조회 및 예외
    private ShipmentLotInfoResponse getShipmentLotInfoResponse(Long shipmentLotId) throws NotFoundException {
        ShipmentLotInfoResponse shipmentLotInfoResponse = shipmentLotRepo.findShipmentLotResponseById(shipmentLotId)
                .orElseThrow(() -> new NotFoundException("shipmentLot does not exist. input id: " + shipmentLotId));
        Long inputTestDetailId = getInputTestIdInLotMasterId(shipmentLotInfoResponse.getLotId(), shipmentLotId);
        shipmentLotInfoResponse.setInputTestId(inputTestDetailId);      // lotMaster 에 해당하는 검사번호 찾아서 추가
        return shipmentLotInfoResponse;
    }
}
