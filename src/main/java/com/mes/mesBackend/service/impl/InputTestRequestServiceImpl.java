package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.InputTestRequestCreateRequest;
import com.mes.mesBackend.dto.request.InputTestRequestUpdateRequest;
import com.mes.mesBackend.dto.response.InputTestRequestResponse;
import com.mes.mesBackend.entity.InputTestRequest;
import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.entity.enumeration.TestType;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.InputTestDetailRepository;
import com.mes.mesBackend.repository.InputTestRequestRepository;
import com.mes.mesBackend.repository.LotMasterRepository;
import com.mes.mesBackend.service.InputTestRequestService;
import com.mes.mesBackend.service.LotMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// 14-1. 검사의뢰 등록
@Service
@RequiredArgsConstructor
public class InputTestRequestServiceImpl implements InputTestRequestService {
    private final LotMasterService lotMasterService;
    private final InputTestRequestRepository inputTestRequestRepo;
    private final ModelMapper modelMapper;
    private final LotMasterRepository lotMasterRepo;
    private final InputTestDetailRepository inputTestDetailRepo;

    /*
    * 검사의뢰 생성
    * lotMaster.checkRequestAmount 검사요청수량 변경
    * 검사유형: 추후 non 측과 협의 후 변경되어야 함
    * 예외: 입고된 갯수만큼만 요청수량을 등록 할 수 있음
    * */
    @Override
    public InputTestRequestResponse createInputTestRequest(InputTestRequestCreateRequest inputTestRequestRequest) throws NotFoundException, BadRequestException {
        LotMaster lotMaster = lotMasterService.getLotMasterOrThrow(inputTestRequestRequest.getLotId());
        throwIfRequestAmountGreaterThanInputAmount(inputTestRequestRequest.getLotId(), lotMaster.getCheckRequestAmount() + inputTestRequestRequest.getRequestAmount());       // 요청수량 재고수량 비교
        InputTestRequest inputTest = modelMapper.toEntity(inputTestRequestRequest, InputTestRequest.class);
        inputTest.createItemInputRequest(lotMaster);                // 상태값: SCHEDULE
        inputTestRequestRepo.save(inputTest);       // lotMaster, 요청유형, 요청수량, 검사유형, 상태값 생성
        lotMaster.setCheckRequestAmount(lotMaster.getCheckRequestAmount() + inputTestRequestRequest.getRequestAmount());    // lotMaster 검사요청수량 변경 = 기존 검사요청수량 + 새로들어온 검사요청수량
        lotMasterRepo.save(lotMaster);
        return getInputTestRequestResponse(inputTest.getId());
    }


    // 검사의뢰 리스트 검색 조회,
    // 검색조건: 창고 id, LOT 유형 id, 품명|품목, 검사유형, 품목그룹, 요청유형, 의뢰기간
    @Override
    public List<InputTestRequestResponse> getInputTestRequests(
            Long warehouseId,
            Long lotTypeId,
            String itemNoAndName,
            TestType testType,
            Long itemGroupId,
            TestType requestType,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        List<InputTestRequestResponse> inputTestRequestResponses = inputTestRequestRepo.findAllByCondition(warehouseId, lotTypeId, itemNoAndName, testType, itemGroupId, requestType, fromDate, toDate, true);
        for (InputTestRequestResponse inputTestRequestResponse : inputTestRequestResponses) {
            List<Integer> testAmount = inputTestDetailRepo.findTestAmountByInputTestRequestId(inputTestRequestResponse.getId());
            int testAmountSum = testAmount.stream().mapToInt(Integer::intValue).sum();
            inputTestRequestResponse.setTestAmount(testAmountSum);
        }
        return inputTestRequestResponses;
    }

    // 검사의뢰 단일 조회
    @Override
    public InputTestRequestResponse getInputTestRequestResponse(Long id) throws NotFoundException {
        InputTestRequestResponse inputTestRequestResponse = inputTestRequestRepo.findResponseByIdAndDeleteYnFalse(id, true)
                .orElseThrow(() -> new NotFoundException("inputTestRequest does not exist. input id: " + id));
        List<Integer> testAmount = inputTestDetailRepo.findTestAmountByInputTestRequestId(inputTestRequestResponse.getId());
        int testAmountSum = testAmount.stream().mapToInt(Integer::intValue).sum();
        inputTestRequestResponse.setTestAmount(testAmountSum);
        return inputTestRequestResponse;
    }

    // 검사의뢰 수정
    @Override
    public InputTestRequestResponse updateInputTestRequest(Long id, InputTestRequestUpdateRequest inputTestRequestUpdateRequest) throws NotFoundException, BadRequestException {
        InputTestRequest findInputTestRequest = getInputTestRequestOrThrow(id);
        LotMaster findLotMaster = findInputTestRequest.getLotMaster();
        throwIfRequestAmountGreaterThanInputAmount(findInputTestRequest.getLotMaster().getId(), inputTestRequestUpdateRequest.getRequestAmount());     // 요청수량 재고수량 비교
        InputTestRequest newInputTestRequest = modelMapper.toEntity(inputTestRequestUpdateRequest, InputTestRequest.class);
        int beforeCheckAmount = findLotMaster.getCheckRequestAmount() - findInputTestRequest.getRequestAmount();

        findInputTestRequest.update(newInputTestRequest);
        inputTestRequestRepo.save(findInputTestRequest);
        findLotMaster.setCheckRequestAmount(beforeCheckAmount + inputTestRequestUpdateRequest.getRequestAmount());     // lotMaster 검사요청수량 변경
        lotMasterRepo.save(findLotMaster);
        return getInputTestRequestResponse(findInputTestRequest.getId());
    }

    // 검사의뢰 삭제
    @Override
    public void deleteInputTestRequest(Long id) throws NotFoundException {
        InputTestRequest inputTestRequest = getInputTestRequestOrThrow(id);
        inputTestRequest.delete();
        LotMaster lotMaster = inputTestRequest.getLotMaster();
        lotMaster.setCheckRequestAmount(lotMaster.getCheckRequestAmount() - inputTestRequest.getRequestAmount());
        inputTestRequestRepo.save(inputTestRequest);
    }

    // 검사의뢰 단일 조회 및 예외
    @Override
    public InputTestRequest getInputTestRequestOrThrow(Long id) throws NotFoundException {
        return inputTestRequestRepo.findByIdAndInputTestDivisionTrueAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("inputTestRequest does not exist. input id: " + id));
    }

    // 입고된 갯수만큼만 요청수량을 등록 할 수 있음.
    // 요청수량 재고수량 비교
    private void throwIfRequestAmountGreaterThanInputAmount(Long lotId, int requestAmount) throws BadRequestException {
        Integer stockAmountFromLotMaster = inputTestRequestRepo.findLotMasterInputAmountByLotMasterId(lotId);
        if (requestAmount > stockAmountFromLotMaster)
            throw new BadRequestException("input requestAmount must not be greater than inputAmount. " +
                    "input requestAmount: " + requestAmount + ", inputAmount: " + stockAmountFromLotMaster);
    }
}
