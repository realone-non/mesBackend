package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.ShipmentCreateRequest;
import com.mes.mesBackend.dto.request.ShipmentUpdateRequest;
import com.mes.mesBackend.dto.response.*;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.AmountHelper;
import com.mes.mesBackend.helper.LotLogHelper;
import com.mes.mesBackend.helper.NumberAutomatic;
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
    private final AmountHelper amountHelper;
    private final WorkProcessRepository workProcessRepository;

    // ====================================================== 출하 ======================================================
    // orderState 변경 api


    // 출하 생성
    // TODO: 바코드 생성 BARYYMMDD001
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

            // 화폐 id, 담당자 id 검색
            if (res.getCurrencyId() != null && res.getUserId() != null) {
                if (currencyId != null && userId != null) {
                    if (!res.getCurrencyId().equals(currencyId) && !res.getUserId().equals(userId)) res = null;
                    else if (res.getCurrencyId().equals(currencyId) && !res.getUserId().equals(userId)) res = null;
                } else if (!res.getCurrencyId().equals(currencyId) && res.getUserId().equals(userId)) {
                    res = null;
                } else if (currencyId != null) {
                    if (!res.getCurrencyId().equals(currencyId)) res = null;
                } else if (userId != null) {
                    if (!res.getUserId().equals(userId)) res = null;
                }
            } else {
                if (currencyId != null && userId != null) res = null;
                else if (currencyId != null) res = null;
                else if (userId != null) res = null;
            }
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

    // 출하 삭제
    // 다른 출하 부분 구현 후 삭제 기능 다시 구현
    @Override
    public void deleteShipment(Long shipmentId) throws NotFoundException, BadRequestException {
        Shipment shipment = getShipmentOrThrow(shipmentId);
        // shipmentState 가 COMPLETION 인지 체크
        throwIfShipmentStateCompletion(shipment.getOrderState());
        // 해당 출하에 등록된 출하 품목 정보가 있는지 체크
        throwShipmentItemInShipment(shipmentId);
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
    /*
    * 등록조건
    * - 수주품목 중복 등록 불가능(입력한 contractItem 이 이미 있는지 체크)
    * - 생성된 출하의 거래처가 같은것만 등록 가능
    * - 출하의 상태가 COMPLETION 이면 추가 불가능
    * */
    @Override
    public ShipmentItemResponse createShipmentItem(Long shipmentId, Long contractItemId, String note) throws NotFoundException, BadRequestException {
        Shipment shipment = getShipmentOrThrow(shipmentId);
        ContractItem contractItem = getContractItemOrThrow(contractItemId);

        // 입력한 contractItem 이 이미 있는지 체크
        throwIfContractItemInShipment(shipmentId, contractItemId);
        // 출하의 거래처와 수주품목의 수주 고객사가 같은지 체크
        throwIfShipmentClientEqualContractClient(shipment.getClient().getId(), contractItem.getContract().getClient().getId());
        // 출하의 상태가 COMPLETION 인지 체크
        throwIfShipmentStateCompletion(shipment.getOrderState());

        ShipmentItem shipmentItem = new ShipmentItem();
        shipmentItem.create(shipment, contractItem, note);
        shipmentItemRepo.save(shipmentItem);
        return getShipmentItemResponse(shipmentId, shipmentItem.getId());
    }

    // 출하 품목정보 단일 조회
    @Override
    public ShipmentItemResponse getShipmentItemResponse(Long shipmentId, Long shipmentItemId) throws NotFoundException {
        int shipmentAmount = shipmentLotRepo.findShipmentLotShipmentAmountByShipmentItemId(shipmentItemId).stream().mapToInt(Integer::intValue).sum();
        return shipmentItemRepo.findShipmentItemResponseByShipmentItemId(shipmentId, shipmentItemId)
                .orElseThrow(() -> new NotFoundException("shipmentItem does not exist. input id: " + shipmentItemId)).converter(shipmentAmount);
    }

    // 출하 품목 정보 전체조회
    @Override
    public List<ShipmentItemResponse> getShipmentItem(Long shipmentId) {
        List<ShipmentItemResponse> responses = shipmentItemRepo.findShipmentResponsesByShipmentId(shipmentId);
        for (ShipmentItemResponse res : responses) {
            int shipmentAmount = shipmentLotRepo.findShipmentLotShipmentAmountByShipmentItemId(res.getId()).stream().mapToInt(Integer::intValue).sum();
            res.converter(shipmentAmount);
        }
        return responses;
    }

    // 출하 품목정보 수정
    /*
    * 수주품목 변경 시 LOT 정보가 있으면 해당 LOT 정보 삭제 후 수정 가능
    * */
    @Override
    public ShipmentItemResponse updateShipmentItem(Long shipmentId, Long shipmentItemId, Long contractItemId, String note) throws NotFoundException, BadRequestException {
        ShipmentItem findShipmentItem = getShipmentItemOrThrow(shipmentId, shipmentItemId);

        // 출하의 상태가 COMPLETION 인지 체크
        throwIfShipmentStateCompletion(findShipmentItem.getShipment().getOrderState());

        // 기존 contractItem 과 입력받은 contractItem 이 다르면
        if (!findShipmentItem.getContractItem().getId().equals(contractItemId)) {
            throwIfShipmentLotInShipmentItem(findShipmentItem.getId()); // 수주품목 변경 시 해당 출하품목 정보에 해당하는 LOT 정보가 있는지 체크
        }

        ContractItem newContractItem = getContractItemOrThrow(contractItemId);
        findShipmentItem.update(newContractItem, note);
        shipmentItemRepo.save(findShipmentItem);
        return getShipmentItemResponse(shipmentId, shipmentItemId);
    }

    // 출하 품목정보 삭제
    /*
    * LOT 정보가 있으면 해당 LOT 정보 삭제 후 삭제 가능
    * */
    @Override
    public void deleteShipmentItem(Long shipmentId, Long shipmentItemId) throws NotFoundException, BadRequestException {
        ShipmentItem shipmentItem = getShipmentItemOrThrow(shipmentId, shipmentItemId);
        // 수주품목 수정, 삭제 시 해당 출하품목 정보에 해당하는 LOT 정보가 있는지 체크
        throwIfShipmentLotInShipmentItem(shipmentItemId);
        // 출하의 상태가 COMPLETION 인지 체크
        throwIfShipmentStateCompletion(shipmentItem.getShipment().getOrderState());

        shipmentItem.delete();
        shipmentItemRepo.save(shipmentItem);
    }

// =================================================== 출하 LOT 정보 ====================================================
    // LOT 정보 생성
    @Override
    public ShipmentLotInfoResponse createShipmentLot(Long shipmentId, Long shipmentItemId, Long lotMasterId) throws NotFoundException, BadRequestException {
        ShipmentItem shipmentItem = getShipmentItemOrThrow(shipmentId, shipmentItemId);
        LotMaster lotMaster = getLotMasterOrThrow(lotMasterId);

        // 출하의 상태가 COMPLETION 인지 체크
        throwIfShipmentStateCompletion(shipmentItem.getShipment().getOrderState());

        // 해당 LOT 가 다른쪽에 출하 품목정보에 등록 되어있는지 체크


        // lotMaster: shipmentItem 의 item 에 해당되는 lotMaster 가져옴, 조건? 공정이 포장까지 완료된, stockAmount 가 1 이상
        List<Long> lotMasterIds = shipmentLotRepo.findLotMasterIdByItemIdAndWorkProcessShipment(shipmentItem.getContractItem().getItem().getId(), PACKAGING);
        boolean lotMasterIdAnyMatch = lotMasterIds.stream().anyMatch(id -> id.equals(lotMasterId));
        if (!lotMasterIdAnyMatch) {
            throw new BadRequestException("입력한 lotMaster id 는 shipmentItem 의 item 에 해당하고, 포장공정까지 완료하고, stockAmount 가 1 이상인 lotMaster 가 아닙니다. " +
                    "입력한 lotMasterId: " + lotMasterId + ", " +
                    "조건에 맞는 lotMasterId: " + lotMasterIds
            );
        }


        ShipmentLot shipmentLot = new ShipmentLot();
        shipmentLot.create(shipmentItem, lotMaster);

        // stockAmount  0, stockAmount 만큼 ShipmentAmount 가 바뀜
        int stockAmountLotMaster = lotMaster.getStockAmount();       // lotMaster 재고수량
        lotMaster.setShipmentAmount(stockAmountLotMaster);        // 출하수량 변경(재고수량)
        lotMaster.setStockAmount(0);                                    // 재고수량 0으로 변경
        Long workProcessShipmentId = lotLogHelper.getWorkProcessByDivisionOrThrow(SHIPMENT);
        WorkProcess workProcessShipment = workProcessRepository.findByIdAndDeleteYnFalse(workProcessShipmentId)
                .orElseThrow(() -> new NotFoundException("[ShipmentLot]workProcess does not exist. workProcessId: " + workProcessShipmentId));
        lotMaster.setWorkProcess(workProcessShipment);

        // lotLog insert
        Long workProcessId = lotLogHelper.getWorkProcessByDivisionOrThrow(PACKAGING);
        Long workOrderDetailId = lotLogHelper.getWorkOrderDetailByContractItemAndWorkProcess(shipmentItem.getContractItem().getContract().getId(), workProcessId);
        lotLogHelper.createLotLog(lotMasterId, workOrderDetailId, workProcessId);

        // amountHelper insert
        amountHelper.amountUpdate(shipmentItem.getContractItem().getItem().getId(), lotMaster.getWareHouse().getId(), null, SHIPMENT_AMOUNT, stockAmountLotMaster, false);

        shipmentLotRepo.save(shipmentLot);
        return getShipmentLotInfoResponse(shipmentLot.getId());
    }

    // LOT 정보 전체 조회
    @Override
    public List<ShipmentLotInfoResponse> getShipmentLots(Long shipmentId, Long shipmentItemId) {
        List<ShipmentLotInfoResponse> responses = shipmentLotRepo.findShipmentLotResponsesByShipmentItemId(shipmentItemId);
        for (ShipmentLotInfoResponse res : responses) {
            boolean inputTestYn = getInputTestYnInLotMasterId(res.getLotId());
            res.setInputTestYn(inputTestYn);   // lotMaster 에 해당하는 검사 완료 여부
        }
        return responses;
    }

    // 출하 LOT 정보 삭제
    @Override
    public void deleteShipmentLot(Long shipmentId, Long shipmentItemId, Long shipmentLotId) throws NotFoundException, BadRequestException {
        ShipmentItem findShipmentItem = getShipmentItemOrThrow(shipmentId, shipmentItemId);
        ShipmentLot findShipmentLot = getShipmentLotOrThrow(findShipmentItem, shipmentLotId);

        // 출하의 상태가 COMPLETION 인지 체크
        throwIfShipmentStateCompletion(findShipmentItem.getShipment().getOrderState());

        LotMaster lotMaster = findShipmentLot.getLotMaster();
        int shipmentAmount = lotMaster.getShipmentAmount();

        // shipmentLot 삭제
        findShipmentLot.delete();
        // lotMaster 재고수량, 출하수량 변경
        lotMaster.setStockAmount(shipmentAmount);       // 재고수량 -> lotMaster 의 shipmentAmount
        lotMaster.setShipmentAmount(0);                 // 출하수량 -> 0

        // lotLog insert
        Long workProcessId = lotLogHelper.getWorkProcessByDivisionOrThrow(PACKAGING);          // 작업공정 division 으로 공정 id 찾음
        Long workOrderDetailId = lotLogHelper.getWorkOrderDetailByContractItemAndWorkProcess(findShipmentItem.getContractItem().getId(), workProcessId);// 수주품목, 작업공정으로 해당 작업지시 정보 가져옴
        lotLogHelper.createLotLog(findShipmentLot.getLotMaster().getId(), workOrderDetailId, workProcessId);
        // amountHelper insert
        amountHelper.amountUpdate(findShipmentItem.getContractItem().getItem().getId(), lotMaster.getWareHouse().getId(), null, STOCK_AMOUNT, shipmentAmount, false);

        shipmentLotRepo.save(findShipmentLot);
        lotMasterRepository.save(lotMaster);
    }

    // 출하 LOT 정보 생성 시 LOT 정보 조회 API
    // 출하 품목정보의 품목과 lotMaster 의 품목이랑 같으며 포장공정 끝나고 현재 재고가 0 이 아닌 lotMaster id
    // 출하품목정보에 수주수량보다 lot 정보의 재고수량이 더 크면 X
    @Override
    public List<LotMasterResponse.idAndLotNo> getShipmentLotMasters(Long contractItemId, int notShippedAmount) throws NotFoundException {
        ContractItem contractItem = getContractItemOrThrow(contractItemId);
        return lotMasterRepository.findLotMastersByShipmentLotCondition(contractItem.getItem().getId(), notShippedAmount);
    }


    // shipmentLot 단일 조회 및 예외
    private ShipmentLot getShipmentLotOrThrow(ShipmentItem shipmentItem, Long shipmentLotId) throws NotFoundException {
        return shipmentLotRepo.findByIdAndShipmentItemAndDeleteYnFalse(shipmentLotId, shipmentItem)
                .orElseThrow(() -> new NotFoundException("shipmentLot does not exist. input shipmentLogId: " + shipmentLotId));
    }

    // lotMaster 에 해당하는 검사번호 찾아서 추가
    private boolean getInputTestYnInLotMasterId(Long lotMasterId) {
        return inputTestRequestRepo.findInputTestYnByLotMasterId(lotMasterId);
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
        boolean inputTestYn = getInputTestYnInLotMasterId(shipmentLotInfoResponse.getLotId());
        shipmentLotInfoResponse.setInputTestYn(inputTestYn);      // lotMaster 에 해당하는 검사 완료 여부
        return shipmentLotInfoResponse;
    }

    // shipmentState 가 COMPLETION 이 아닌것만 수정, 삭제 가능
    private void throwIfShipmentStateCompletion(OrderState shipmentOrderState) throws BadRequestException {
        if (shipmentOrderState.equals(COMPLETION)) {
            throw new BadRequestException("출하가 완료된 정보이므로 삭제나 수정이 불가능 합니다.");
        }
    }

    // 입력한 contractItem 이 이미 있는지 체크
    private void throwIfContractItemInShipment(Long shipmentId, Long contractItemId) throws BadRequestException {
        boolean existsByContractItemInShipment = shipmentItemRepo.existsByContractItemInShipment(shipmentId, contractItemId);
        if (existsByContractItemInShipment) {
            throw new BadRequestException("입력한 수주품목은 출하정보에 이미 등록되어 있습니다.");
        }
    }

    // 출하의 거래처와 수주품목의 수주 고객사가 같은지 체크
    private void throwIfShipmentClientEqualContractClient(Long shipmentClientId, Long contractClientId) throws BadRequestException {
        if (!shipmentClientId.equals(contractClientId)) {
            throw new BadRequestException("출하의 거래처와 수주품목이 해당 된 수주의 고객사가 같은 수주품목만 등록 할 수 있습니다. " +
                    "해당 출하의 거래처: " + shipmentClientId + ", " +
                    "수주품목이 해당된 수주의 고객사: " + contractClientId
            );
        }
    }

    // 수주품목 수정, 삭제 시 해당 출하품목 정보에 해당하는 LOT 정보가 있는지 체크
    private void throwIfShipmentLotInShipmentItem(Long shipmentItemId) throws BadRequestException {
        boolean existsByShipmentItemInShipmentLot = shipmentLotRepo.existsByShipmentItemInShipmentLot(shipmentItemId);
        if (existsByShipmentItemInShipmentLot) {
            throw new BadRequestException("출하 품목에 대한 수정이나 삭제는 해당하는 LOT 정보 삭제 후에 가능합니다.");
        }
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

    // 해당 출하에 등록된 출하 품목 정보가 있는지 체크
    private void throwShipmentItemInShipment(Long shipmentId) throws BadRequestException {
        boolean existsByShipmentItemInShipment = shipmentRepo.existsByShipmentItemInShipment(shipmentId);
        if (existsByShipmentItemInShipment) {
            throw new BadRequestException("등록된 출하 품목정보가 있으면 삭제할 수 없습니다. 품목정보 삭제 후 다시 시도 바랍니다.");
        }
    }

// ==================================================== 4-7. 출하 현황 ====================================================
    // 출하현황 검색 리스트 조회, 검색조건: 거래처 id, 출하기간 fromDate~toDate, 화폐 id, 담당자 id, 품번|품명
    @Override
    public List<ShipmentStatusResponse> getShipmentStatuses(
            Long clientId,
            LocalDate fromDate,
            LocalDate toDate,
            Long currencyId,
            Long userId,
            String itemNoAndItemName
    ) {
        return shipmentLotRepo.findShipmentStatusesResponsesByCondition(clientId, fromDate, toDate, currencyId, userId, itemNoAndItemName);
    }
}
