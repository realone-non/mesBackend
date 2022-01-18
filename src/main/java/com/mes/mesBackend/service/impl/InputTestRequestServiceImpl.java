package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.InputTestRequestCreateRequest;
import com.mes.mesBackend.dto.request.InputTestRequestUpdateRequest;
import com.mes.mesBackend.dto.response.InputTestRequestResponse;
import com.mes.mesBackend.entity.InputTestRequest;
import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.entity.enumeration.InputTestDivision;
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
import java.util.stream.Collectors;

import static com.mes.mesBackend.entity.enumeration.InputTestDivision.*;
import static com.mes.mesBackend.helper.Constants.PRODUCT_ITEM_ACCOUNT;

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
    public InputTestRequestResponse createInputTestRequest(
            InputTestRequestCreateRequest inputTestRequestRequest,
            InputTestDivision inputTestDivision
    ) throws BadRequestException, NotFoundException {
        int requestAmount = inputTestRequestRequest.getRequestAmount();
        LotMaster lotMaster = lotMasterService.getLotMasterOrThrow(inputTestRequestRequest.getLotId());

        if (inputTestDivision.equals(PART)) {
            if (lotMaster.getPurchaseInput() == null) throw new BadRequestException("구매입고만 등록할 수 있음.");
        } else if (inputTestDivision.equals(OUT_SOURCING)) {
            if (lotMaster.getOutSourcingInput() == null) throw new BadRequestException("외주입고만 등록할 수 있음");
        } else if (inputTestDivision.equals(PRODUCT)) {
            if (!lotMaster.getItem().getItemAccount().getAccount().equals(PRODUCT_ITEM_ACCOUNT)) {
                throw new BadRequestException("완제품만 등록할 수 있음.");
            }
        }

        // 입력받은 요청수량이 lot 의 재고수량보다 많은지 체크
        throwIfRequestAmountGreaterThanInputAmount(inputTestRequestRequest.getLotId(), lotMaster.getCheckRequestAmount() + requestAmount);

        int beforeCheckRequestAmount = lotMaster.getCheckRequestAmount();
        InputTestRequest inputTest = modelMapper.toEntity(inputTestRequestRequest, InputTestRequest.class);
        inputTest.createInputTestRequest(lotMaster, inputTestDivision, inputTestRequestRequest.getTestCompletionRequestDate());
        inputTestRequestRepo.save(inputTest);       // lotMaster, 요청유형, 요청수량, 검사유형, 상태값 생성

        lotMaster.setCheckRequestAmount(beforeCheckRequestAmount + requestAmount);
        lotMasterRepo.save(lotMaster);
        return getInputTestRequestResponse(inputTest.getId(), inputTestDivision);
    }

    // 검사의뢰 단일 조회
    @Override
    public InputTestRequestResponse getInputTestRequestResponse(Long inputTestId, InputTestDivision inputTestDivision) throws NotFoundException {
        InputTestRequestResponse response = inputTestRequestRepo.findResponseByIdAndDeleteYnFalse(inputTestId, inputTestDivision)
                .orElseThrow(() -> new NotFoundException("inputTestRequest does not exist. input id: " + inputTestId));
        if (inputTestDivision.equals(PRODUCT)) {
            String workOrderNo = inputTestRequestRepo.findWorkOrderNoByLotId(response.getLotId())
                    .orElseThrow(() -> new NotFoundException("해당하는 lot 로 등록된 작업지시 정보가 없음."));
            response.setWorkOrderNo(workOrderNo);
        }
        return response.division(inputTestDivision);
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
            InputTestDivision inputTestDivision
    ) throws NotFoundException {
            List<InputTestRequestResponse> responses = inputTestRequestRepo.findAllByCondition(
                    warehouseId,
                    lotTypeId,
                    itemNoAndName,
                    testType,
                    itemGroupId,
                    requestType,
                    fromDate,
                    toDate,
                    inputTestDivision
            );
            for (InputTestRequestResponse response : responses) {
                List<Integer> testAmountList = inputTestDetailRepo.findTestAmountByInputTestRequestId(response.getId());
                int testAmountSum = testAmountList.stream().mapToInt(Integer::intValue).sum();
                response.setTestAmount(testAmountSum);

                // 완제품에 대한 작업지시번호
                if (inputTestDivision.equals(PRODUCT)) {
                    Long lotId = response.getLotId();
                    String workOrderNo = inputTestRequestRepo.findWorkOrderNoByLotId(lotId)
                            .orElseThrow(() -> new NotFoundException("해당하는 lot 로 등록된 작업지시 정보가 없음."));
                    response.setWorkOrderNo(workOrderNo);
                }
            }
            return responses.stream().map(res -> res.division(inputTestDivision)).collect(Collectors.toList());
    }

    // 외주수입검사의뢰 수정
    @Override
    public InputTestRequestResponse updateInputTestRequest(
            Long id,
            InputTestRequestUpdateRequest inputTestRequestUpdateRequest,
            InputTestDivision inputTestDivision
    ) throws BadRequestException, NotFoundException {
        InputTestRequest findInputTestRequest = getInputTestRequestOrThrow(id, inputTestDivision);
        LotMaster findLotMaster = findInputTestRequest.getLotMaster();
        int beforeRequestAmount = findInputTestRequest.getRequestAmount();

        int newRequestAmount = inputTestRequestUpdateRequest.getRequestAmount();
        // 입력받은 요청수량이 lot 의 입고수량보다 많은지 체크
        throwIfRequestAmountGreaterThanInputAmount(findLotMaster.getId(), (findLotMaster.getCheckRequestAmount() - beforeRequestAmount) + newRequestAmount);

        InputTestRequest newInputTestRequest = modelMapper.toEntity(inputTestRequestUpdateRequest, InputTestRequest.class);
        findInputTestRequest.update(newInputTestRequest, inputTestDivision);
        findLotMaster.setCheckRequestAmount((findLotMaster.getCheckRequestAmount() - beforeRequestAmount) + inputTestRequestUpdateRequest.getRequestAmount());
        inputTestRequestRepo.save(findInputTestRequest);
        lotMasterRepo.save(findLotMaster);
        return getInputTestRequestResponse(id, inputTestDivision);
    }

    @Override
    public InputTestRequest getInputTestRequestOrThrow(Long id, InputTestDivision inputTestDivision) throws NotFoundException {
        return inputTestRequestRepo.findByIdAndInputTestDivisionAndDeleteYnFalse(id, inputTestDivision)
                .orElseThrow(() -> new NotFoundException("inputTestRequest does not exist. input id: " + id));
    }

    // 외주수입검사의뢰 삭제
    @Override
    public void deleteInputTestRequest(Long id, InputTestDivision inputTestDivision) throws NotFoundException, BadRequestException {
        InputTestRequest findInputTestRequest = getInputTestRequestOrThrow(id, inputTestDivision);

        // 검사요청에 대한 검사등록 정보가 있을 시 삭제 불가
        List<Integer> inputTest = inputTestDetailRepo.findTestAmountByInputTestRequestId(findInputTestRequest.getId());
        if (!inputTest.isEmpty()) {
            throw new BadRequestException("입력한 검사요청에 대한 검사등록이 존재하므로 삭제할 수 없음.");
        }

        LotMaster findLotMaster = findInputTestRequest.getLotMaster();
        findInputTestRequest.delete();
        findLotMaster.setCheckRequestAmount(findLotMaster.getCheckRequestAmount() - findInputTestRequest.getRequestAmount());
        lotMasterRepo.save(findLotMaster);
    }

    // 입고된 갯수만큼만 요청수량을 등록 할 수 있음.
    // 요청수량 재고수량 비교
    private void throwIfRequestAmountGreaterThanInputAmount(Long lotId, int requestAmount) throws BadRequestException {
        Integer stockAmountFromLotMaster = inputTestRequestRepo.findLotMasterStockAmountByLotMasterId(lotId);
        if (requestAmount > stockAmountFromLotMaster)
            throw new BadRequestException("input requestAmount must not be greater than stockAmount. " +
                    "input requestAmount: " + requestAmount + ", stockAmount: " + stockAmountFromLotMaster);
    }
}
