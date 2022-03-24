package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.PurchaseInputReturnCreateRequest;
import com.mes.mesBackend.dto.request.PurchaseInputReturnUpdateRequest;
import com.mes.mesBackend.dto.response.PurchaseInputReturnResponse;
import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.entity.PurchaseInputReturn;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.LotMasterRepository;
import com.mes.mesBackend.repository.PurchaseInputReturnRepository;
import com.mes.mesBackend.service.LotMasterService;
import com.mes.mesBackend.service.PurchaseInputReturnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// 9-6. 구매입고 반품 등록
@Service
@RequiredArgsConstructor
public class PurchaseInputReturnServiceImpl implements PurchaseInputReturnService {
    private final PurchaseInputReturnRepository purchaseInputReturnRepo;
    private final ModelMapper mapper;
    private final LotMasterService lotMasterService;
    private final LotMasterRepository lotMasterRepo;

    // 구매입고반품 생성
    @Override
    public PurchaseInputReturnResponse createPurchaseInputReturn(PurchaseInputReturnCreateRequest purchaseInputReturnRequest) throws NotFoundException, BadRequestException {
        PurchaseInputReturn purchaseInputReturn = mapper.toEntity(purchaseInputReturnRequest, PurchaseInputReturn.class);
        LotMaster lotMaster = lotMasterService.getLotMasterOrThrow(purchaseInputReturnRequest.getLotMasterId());
        boolean division = purchaseInputReturnRequest.isReturnDivision();
        int inputReturnAmount = purchaseInputReturnRequest.getReturnAmount();
        PurchaseInputReturnResponse dbInputReturn = purchaseInputReturnRepo.findPurchaseInputReturnByCondition(lotMaster.getId(), division);
        if(dbInputReturn != null){
            throw new BadRequestException("동일한 LOT로 입고반품을 중복 등록 할 수 없습니다.");
        }

//        int lotMasterPossibleAmount = division ? lotMaster.getStockAmount() : lotMaster.getBadItemAmount();
//
//        int inputPossibleAmount = purchaseInputReturnRequest.getReturnPossibleAmount() + purchaseInputReturnRequest.getReturnAmount();
//
//        if (inputPossibleAmount > lotMasterPossibleAmount) {
//            throw new BadRequestException("입력한 반품수량 + 반품가능수량이 lotMaster 의 반품가능수량을 초과한다.");
//        }

        if(division == true && purchaseInputReturnRequest.getReturnAmount() > lotMaster.getStockAmount()){
            throw new BadRequestException("반품수량은 현재 정상 재고 수량을 초과할 수 없습니다.");
        }
        else if(division == false && purchaseInputReturnRequest.getReturnAmount() > lotMaster.getBadItemAmount()){
            throw new BadRequestException("반품수량은 현재 불량 재고 수량을 초과할 수 없습니다.");
        }

        if (division) {
            lotMaster.setStockAmount(lotMaster.getStockAmount() - inputReturnAmount);
            lotMaster.setStockReturnAmount(lotMaster.getStockReturnAmount() + inputReturnAmount);
        } else {
            lotMaster.setBadItemAmount(lotMaster.getBadItemAmount() - inputReturnAmount);
            lotMaster.setBadItemReturnAmount(lotMaster.getBadItemReturnAmount() + inputReturnAmount);
        }

        lotMasterRepo.save(lotMaster);
        purchaseInputReturn.setLotMaster(lotMaster);
        purchaseInputReturnRepo.save(purchaseInputReturn);

        return getPurchaseInputReturnResponse(purchaseInputReturn.getId());
    }

    // 구매입고반품 리스트 검색 조회, 검색조건: 거래처 id, 품명|품목, 반품기간 fromDate~toDate
    @Override
    public List<PurchaseInputReturnResponse> getPurchaseInputReturns(Long clientId, String itemNoOrItemName, LocalDate fromDate, LocalDate toDate) {
        List<PurchaseInputReturnResponse> purchaseInputReturnResponses = purchaseInputReturnRepo.findPurchaseInputReturnResponsesByCondition(clientId, itemNoOrItemName, fromDate, toDate);
        purchaseInputReturnResponses.forEach(PurchaseInputReturnResponse::setPossibleAmount);
        return purchaseInputReturnResponses;
    }

    // 구매입고반품 단일조회
    @Override
    public PurchaseInputReturnResponse getPurchaseInputReturnResponse(Long purchaseInputReturnId) throws NotFoundException {
        PurchaseInputReturnResponse inputReturnResponse = purchaseInputReturnRepo.findPurchaseInputReturnResponseById(purchaseInputReturnId)
                .orElseThrow(() -> new NotFoundException("purchaseInputReturn does not exist. input id: " + purchaseInputReturnId));
        inputReturnResponse.setPossibleAmount();
        return inputReturnResponse;
    }

    // 구매입고반품 수정
    @Override
    public PurchaseInputReturnResponse updatePurchaseInputReturn(
            Long purchaseInputReturnId,
            PurchaseInputReturnUpdateRequest purchaseInputReturnUpdateRequest
    ) throws NotFoundException, BadRequestException {
        PurchaseInputReturn newPurchaseInputReturn = mapper.toEntity(purchaseInputReturnUpdateRequest, PurchaseInputReturn.class);
        PurchaseInputReturn findPurchaseInputReturn = getPurchaseInputReturnOrThrow(purchaseInputReturnId);
        LotMaster findLotMaster = lotMasterService.getLotMasterOrThrow(findPurchaseInputReturn.getLotMaster().getId());

        boolean division = findPurchaseInputReturn.isReturnDivision();
        int updateAmount = 0;

//        PurchaseInputReturnResponse response = getPurchaseInputReturnResponse(purchaseInputReturnId);
//        int possibleAmount = response.getPossibleAmount() + response.getReturnAmount();
//        int inputPossibleAmount = purchaseInputReturnUpdateRequest.getReturnPossibleAmount() + purchaseInputReturnUpdateRequest.getReturnAmount();

        //입력받은 반품 수량이 현재 정상품 재고 수량 + 정상품 반품 수량보다 클 경우 Exception
        if(division == true && purchaseInputReturnUpdateRequest.getReturnAmount() > findLotMaster.getStockAmount() + findLotMaster.getStockReturnAmount()){
            throw new BadRequestException("반품수량은 전체 재고 수량을 초과할 수 없습니다.");
        }
        //입력받은 반품 수량이 현재 불량품 재고 수량 + 불량품 반품 수량보다 클 경우 Exception
        else if(division == false && purchaseInputReturnUpdateRequest.getReturnAmount() > findLotMaster.getBadItemReturnAmount() + findLotMaster.getBadItemAmount()){
            throw new BadRequestException("반품수량은 전체 불량 재고 수량을 초과할 수 없습니다.");
        }
//        if (possibleAmount != inputPossibleAmount) {
//            throw new BadRequestException("입력받은 반품가능수량과 lotMaster 의 반품가능수량이 다르다.");
//        }

        //입력받은 반품 수량으로 현재 재고, 반품 수량 업데이트
        if (division == true) {
            updateAmount = findLotMaster.getStockReturnAmount() - purchaseInputReturnUpdateRequest.getReturnAmount();
            findLotMaster.setStockAmount(findLotMaster.getStockAmount() + updateAmount);
            findLotMaster.setStockReturnAmount(purchaseInputReturnUpdateRequest.getReturnAmount());
        } else {
            updateAmount = findLotMaster.getBadItemAmount() - purchaseInputReturnUpdateRequest.getReturnAmount();
            findLotMaster.setBadItemAmount(findLotMaster.getBadItemAmount() + updateAmount);
            findLotMaster.setBadItemReturnAmount(purchaseInputReturnUpdateRequest.getReturnAmount());
        }
        findPurchaseInputReturn.update(newPurchaseInputReturn);
        lotMasterRepo.save(findLotMaster);
        purchaseInputReturnRepo.save(findPurchaseInputReturn);

        return getPurchaseInputReturnResponse(purchaseInputReturnId);
    }

    // 구매입고반품 삭제
    /*
    * 1. returnDivision 이 true ? 정상품
    *       lotMaster 의 stockAmount 를 (lotMaster stockAmount + 삭제될 purchaseInputReturn 의 returnStockItemAmount) 로 변경
    *       lotMaster 의 returnAmount 를 (lotMaster returnAmount - 삭제될 purchaseInputReturn 의 returnStockItemAmount) 로 변경
    * 2. returnDivision 이 false ? 불량품
    *       lotMaster 의 badItemAmount 를 (lotMaster badItemAmount + 삭제될 purchaseInputReturn 의 returnBadItemAmount) 로 변경
    *       lotMaster 의 returnAmount 를 lotMaster returnAmount - 삭제될 purchaseInputReturn 의 returnBadItemAmount) 로 변경
    * */
    @Override
    public void deletePurchaseInputReturn(Long purchaseInputReturnId) throws NotFoundException {
        PurchaseInputReturn purchaseInputReturn = getPurchaseInputReturnOrThrow(purchaseInputReturnId);
        purchaseInputReturn.delete();
        boolean returnDivision = purchaseInputReturn.isReturnDivision();
        LotMaster lotMaster = purchaseInputReturn.getLotMaster();

        if (returnDivision) {
            lotMaster.setStockAmount(lotMaster.getStockAmount() + purchaseInputReturn.getReturnAmount());
            lotMaster.setStockReturnAmount(lotMaster.getStockReturnAmount() - purchaseInputReturn.getReturnAmount());
        } else {
            lotMaster.setBadItemAmount(lotMaster.getBadItemAmount() + purchaseInputReturn.getReturnAmount());
            lotMaster.setBadItemReturnAmount(lotMaster.getBadItemReturnAmount() - purchaseInputReturn.getReturnAmount());
        }
        purchaseInputReturnRepo.save(purchaseInputReturn);
        lotMasterRepo.save(lotMaster);
    }

    private PurchaseInputReturn getPurchaseInputReturnOrThrow(Long id) throws NotFoundException {
        return purchaseInputReturnRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("purchaseInputReturn does not exist. input id: " + id));
    }
}
