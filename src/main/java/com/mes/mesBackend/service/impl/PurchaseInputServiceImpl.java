package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.LotMasterRequest;
import com.mes.mesBackend.dto.request.PurchaseInputRequest;
import com.mes.mesBackend.dto.response.PurchaseInputDetailResponse;
import com.mes.mesBackend.dto.response.PurchaseInputResponse;
import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.entity.PurchaseInput;
import com.mes.mesBackend.entity.PurchaseRequest;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.LotMasterRepository;
import com.mes.mesBackend.repository.PurchaseInputRepository;
import com.mes.mesBackend.repository.PurchaseRequestRepository;
import com.mes.mesBackend.service.LotMasterService;
import com.mes.mesBackend.service.PurchaseInputService;
import com.mes.mesBackend.service.PurchaseRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// 9-5. 구매입고 등록
@Service
@RequiredArgsConstructor
public class PurchaseInputServiceImpl implements PurchaseInputService {
    private final PurchaseInputRepository purchaseInputRepo;
    private final ModelMapper mapper;
    private final PurchaseRequestService purchaseRequestService;
    private final LotMasterService lotMasterService;
    private final LotMasterRepository lotMasterRepo;
    private final PurchaseRequestRepository purchaseRequestRepos;

    // 구매입고 리스트 조회, 검색조건: 입고기간 fromDate~toDate, 입고창고, 거래처, 품명|품번
    // 미구현: 입고기간 조회, 입고수량쪽
    @Override
    public List<PurchaseInputResponse> getPurchaseInputs(
            LocalDate fromDate,
            LocalDate toDate,
            Long wareHouseId,
            Long clientId,
            String itemNoOrItemName
    ) {
        List<PurchaseInputResponse> purchaseRequests = purchaseInputRepo.findPurchaseRequestsByCondition(fromDate, toDate, wareHouseId, clientId, itemNoOrItemName);
        for (PurchaseInputResponse purchaseRequest : purchaseRequests) {
            // 입고일시
            LocalDateTime findInputDate = purchaseInputRepo.findCreatedDateByPurchaseRequestId(purchaseRequest.getId()).orElse(null);
            LocalDate inputDate = findInputDate != null ? LocalDate.from(findInputDate) : null;
            purchaseRequest.setInputDate(inputDate);

            // 입고수량
            List<Integer> purchaserInputAmounts = purchaseInputRepo.findInputAmountByPurchaseRequestId(purchaseRequest.getId());    // 현재 입고된 수량
            int inputAmountSum = purchaserInputAmounts.stream().mapToInt(Integer::intValue).sum();  // 현재 입고된 수량
            purchaseRequest.setInputAmount(inputAmountSum);

            // 입고금액
            int inputPrice = purchaseRequest.getUnitPrice() * inputAmountSum;
            purchaseRequest.setInputPrice(inputPrice);

            // 부가세
            double vat = inputPrice * 0.1;
            purchaseRequest.setVat(vat);

            // 미입고수량
            int orderAmount = purchaseRequest.getOrderAmount();     // 발주수량 - 입고수량
            int alreadyInput = orderAmount - inputAmountSum;
            purchaseRequest.setAlreadyInput(alreadyInput);
        }
        return purchaseRequests;
    }

    // ================================ 구매입고 LOT 정보 ================================
    // 구매입고 LOT 생성
    @Override
    public PurchaseInputDetailResponse createPurchaseInputDetail(
            Long purchaseRequestId,
            PurchaseInputRequest purchaseInputRequest
    ) throws NotFoundException, BadRequestException {
        PurchaseRequest purchaseRequest = purchaseRequestService.getPurchaseRequestOrThrow(purchaseRequestId);
        PurchaseInput purchaseInput = mapper.toEntity(purchaseInputRequest, PurchaseInput.class);

        // 구매요청의 발주수량 만큼 모두 입고 완료 햇을때 구매요청의 지시상태값을 완료(COMPLETION) 으로 변경한다.
        int orderAmount = purchaseRequest.getOrderAmount();     // 발주수량
        List<Integer> purchaserInputAmounts = purchaseInputRepo.findInputAmountByPurchaseRequestId(purchaseRequest.getId());    // 현재 입고된 수량
        int inputAmountSum = purchaserInputAmounts.stream().mapToInt(Integer::intValue).sum() + purchaseInputRequest.getInputAmount();  // 현재 입고된 수량 + 입력된 입고수량
        if (inputAmountSum == orderAmount) {
            purchaseRequest.putOrderStateChangedCompletion();       // 지시상태 값 변경
            purchaseRequestRepos.save(purchaseRequest);
        } else if (inputAmountSum > orderAmount) {
            throw new BadRequestException("입고수량이 등록되어있는 발주수량을 초과했습니다. " +
                    " input 입고수량: " + purchaseInput.getInputAmount()
                    + ", 기존 입고수량 + input 입고수량: " + inputAmountSum
                    + " , 발주수량 : " + orderAmount);
        }

        purchaseInput.setPurchaseRequest(purchaseRequest);
        purchaseInputRepo.save(purchaseInput);  // lot 생성이 안되면 다시 삭제

        // lot 생성
        LotMasterRequest lotMasterRequest = new LotMasterRequest();
        lotMasterRequest.putPurchaseInput(
                purchaseInput.getPurchaseRequest().getItem(),
                purchaseInput.getPurchaseRequest().getPurchaseOrder().getWareHouse(),
                purchaseInput.getId(),
                purchaseInput.getInputAmount(),
                purchaseInput.getInputAmount(),
                purchaseInputRequest.getLotType(),
                purchaseInputRequest.isProcessYn()
        );
        String lotMaster = lotMasterService.createLotMaster(lotMasterRequest);      // lotMaster 생성

        if (lotMaster == null) {
            purchaseInputRepo.deleteById(purchaseInput.getId());
            throw new BadRequestException("lot 번호가 생성되지 않았습니다.");
        }

        return getPurchaseInputDetailResponse(purchaseInput.getId(), purchaseRequest.getId());
    }

    // 구매입고 LOT 전체 조회
    @Override
    public List<PurchaseInputDetailResponse> getPurchaseInputDetails(Long purchaseRequestId) {
        return purchaseInputRepo.findPurchaseInputDetailByPurchaseRequestId(purchaseRequestId);
    }

    // 구메입고 LOT 단일 조회
    @Override
    public PurchaseInputDetailResponse getPurchaseInputDetailResponse(
            Long purchaseRequestId,
            Long purchaseInputId
    ) throws NotFoundException {
        return purchaseInputRepo.findPurchaseInputDetailByIdAndPurchaseInputId(purchaseInputId, purchaseRequestId)
                .orElseThrow(() -> new NotFoundException("purchaseInputDetail does not exist"));
    }

    // 구매입고 LOT 수정
    @Override
    public PurchaseInputDetailResponse updatePurchaseInputDetail(
            Long purchaseRequestId,   // 구매입고
            Long purchaseInputId,     // 구매입고 상세
            PurchaseInputRequest.updateRequest purchaseInputUpdateRequest
    ) throws NotFoundException {
        PurchaseInput findPurchaseInput = getPurchaseInputOrThrow(purchaseInputId, purchaseRequestId);
        PurchaseInput newPurchaseInput = mapper.toEntity(purchaseInputUpdateRequest, PurchaseInput.class);
        findPurchaseInput.put(newPurchaseInput);

        // 해당하는 lot 의 재고수량, 생성수량 변경
        LotMaster lotMaster = getLotMasterOrThrow(findPurchaseInput);
        lotMaster.updatePurchaseInput(findPurchaseInput.getInputAmount());
        lotMasterRepo.save(lotMaster);

        return getPurchaseInputDetailResponse(purchaseRequestId, purchaseInputId);
    }

    // 구매입고 LOT 삭제
    @Override
    public void deletePurchaseInputDetail(Long purchaseRequestId, Long purchaseInputId) throws NotFoundException {
        PurchaseInput purchaseInput = getPurchaseInputOrThrow(purchaseInputId, purchaseRequestId);
        purchaseInput.delete();
        LotMaster lotMaster = getLotMasterOrThrow(purchaseInput);
        lotMaster.delete();
        purchaseInputRepo.save(purchaseInput);
        lotMasterRepo.save(lotMaster);
    }

    // 구매입고 LOT 단일 조회 및 예외
    private PurchaseInput getPurchaseInputOrThrow(Long purchaseInputId, Long purchaseRequestId) throws NotFoundException {      // 구매입고, 구매요청
        PurchaseRequest purchaseRequest = purchaseRequestService.getPurchaseRequestOrThrow(purchaseRequestId);    // 구매요청 조회
        return purchaseInputRepo.findByIdAndPurchaseRequestAndDeleteYnFalse(purchaseInputId, purchaseRequest)
                .orElseThrow(() -> new NotFoundException("purchaseInput does not exist. input purchaseInput id: " + purchaseInputId));
    }

    private LotMaster getLotMasterOrThrow(PurchaseInput purchaseInput) throws NotFoundException {
        return lotMasterRepo.findByPurchaseInputAndDeleteYnFalse(purchaseInput)
                .orElseThrow(() -> new NotFoundException("lotMaster does not exist. input purchaseInputId: " + purchaseInput.getId()));
    }
}
