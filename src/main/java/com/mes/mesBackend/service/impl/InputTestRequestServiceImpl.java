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

// 15-1. 외주수입검사의뢰 등록
@Service
@RequiredArgsConstructor
public class InputTestRequestServiceImpl implements InputTestRequestService {
    private final LotMasterService lotMasterService;
    private final InputTestRequestRepository inputTestRequestRepo;
    private final ModelMapper modelMapper;
    private final LotMasterRepository lotMasterRepo;
    private final InputTestDetailRepository inputTestDetailRepo;

    // 외주수입검사의뢰 생성
    /*
    * lotMaster.checkRequestAmount 검사요청수량 변경
    * 검사유형: 추후 non 상의
    * 예외: 입고된 갯수만큼만 요청수량을 등록 할 수 있음.
    * */
    @Override
    public InputTestRequestResponse createInputTestRequest(InputTestRequestCreateRequest inputTestRequestRequest, boolean inputTestDivision) throws BadRequestException, NotFoundException {
        int requestAmount = inputTestRequestRequest.getRequestAmount();
        LotMaster lotMaster = lotMasterService.getLotMasterOrThrow(inputTestRequestRequest.getLotId());
        // 입력받은 요청수량이 lot 의 입고수량보다 많은지 체크
        throwIfRequestAmountGreaterThanInputAmount(inputTestRequestRequest.getLotId(), lotMaster.getCheckRequestAmount() + requestAmount);

        int beforeCheckRequestAmount = lotMaster.getCheckRequestAmount();
        InputTestRequest inputTest = modelMapper.toEntity(inputTestRequestRequest, InputTestRequest.class);
        inputTest.createOutsourcingInputRequest(lotMaster);
        inputTestRequestRepo.save(inputTest);       // lotMaster, 요청유형, 요청수량, 검사유형, 상태값 생성

        lotMaster.setCheckRequestAmount(beforeCheckRequestAmount + requestAmount);
        lotMasterRepo.save(lotMaster);
        return getInputTestRequestResponse(inputTest.getId(), inputTestDivision);
    }

    // 검사의뢰 단일 조회
    @Override
    public InputTestRequestResponse getInputTestRequestResponse(Long inputTestId, boolean inputTestDivision) throws NotFoundException {
        return inputTestRequestRepo.findResponseByIdAndDeleteYnFalse(inputTestId, inputTestDivision)
                .orElseThrow(() -> new NotFoundException("inputTestRequest does not exist. input id: " + inputTestId));
    }

    // 외주수입검사의뢰 리스트 검색 조회
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
            LocalDate toDate,
            boolean inputTestDivision
    ) {
        List<InputTestRequestResponse> responses = inputTestRequestRepo.findAllByCondition(warehouseId, lotTypeId, itemNoAndName, testType, itemGroupId, requestType, fromDate, toDate, inputTestDivision);
        for (InputTestRequestResponse response : responses) {
            List<Integer> testAmountList = inputTestDetailRepo.findTestAmountByInputTestRequestId(response.getId());
            int testAmountSum = testAmountList.stream().mapToInt(Integer::intValue).sum();
            response.setTestAmount(testAmountSum);
        }
        return responses;
    }

    // 외주수입검사의뢰 수정
    @Override
    public InputTestRequestResponse updateInputTestRequest(
            Long id,
            InputTestRequestUpdateRequest inputTestRequestUpdateRequest,
            boolean inputTestDivision
    ) throws BadRequestException, NotFoundException {
        InputTestRequest findInputTestRequest = getInputTestRequestOrThrow(id, inputTestDivision);
        LotMaster findLotMaster = findInputTestRequest.getLotMaster();
        int beforeRequestAmount = findInputTestRequest.getRequestAmount();

        int newRequestAmount = inputTestRequestUpdateRequest.getRequestAmount();
        // 입력받은 요청수량이 lot 의 입고수량보다 많은지 체크
        throwIfRequestAmountGreaterThanInputAmount(findLotMaster.getId(), (findLotMaster.getCheckRequestAmount() - beforeRequestAmount) + newRequestAmount);

        InputTestRequest newInputTestRequest = modelMapper.toEntity(inputTestRequestUpdateRequest, InputTestRequest.class);
        findInputTestRequest.update(newInputTestRequest);
        findLotMaster.setCheckRequestAmount((findLotMaster.getCheckRequestAmount() - beforeRequestAmount) + inputTestRequestUpdateRequest.getRequestAmount());
        inputTestRequestRepo.save(findInputTestRequest);
        lotMasterRepo.save(findLotMaster);
        return getInputTestRequestResponse(id, inputTestDivision);
    }

    @Override
    public InputTestRequest getInputTestRequestOrThrow(Long id, boolean inputTestDivision) throws NotFoundException {
        return inputTestRequestRepo.findByIdAndInputTestDivisionAndDeleteYnFalse(id, inputTestDivision)
                .orElseThrow(() -> new NotFoundException("inputTestRequest does not exist. input id: " + id));
    }

    // 외주수입검사의뢰 삭제
    @Override
    public void deleteInputTestRequest(Long id, boolean inputTestDivision) throws NotFoundException {
        InputTestRequest findInputTestRequest = getInputTestRequestOrThrow(id, inputTestDivision);
        LotMaster findLotMaster = findInputTestRequest.getLotMaster();
        findInputTestRequest.delete();
        findLotMaster.setCheckRequestAmount(findLotMaster.getCheckRequestAmount() - findInputTestRequest.getRequestAmount());
        lotMasterRepo.save(findLotMaster);
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
