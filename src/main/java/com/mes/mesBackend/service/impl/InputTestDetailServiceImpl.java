package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.InputTestDetailRequest;
import com.mes.mesBackend.dto.response.InputTestDetailResponse;
import com.mes.mesBackend.dto.response.InputTestRequestInfoResponse;
import com.mes.mesBackend.entity.InputTestDetail;
import com.mes.mesBackend.entity.InputTestRequest;
import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.entity.User;
import com.mes.mesBackend.entity.enumeration.InputTestDivision;
import com.mes.mesBackend.entity.enumeration.TestType;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.AmountHelper;
import com.mes.mesBackend.helper.S3Uploader;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.InputTestDetailRepository;
import com.mes.mesBackend.repository.InputTestRequestRepository;
import com.mes.mesBackend.repository.LotMasterRepository;
import com.mes.mesBackend.service.InputTestDetailService;
import com.mes.mesBackend.service.InputTestRequestService;
import com.mes.mesBackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.mes.mesBackend.entity.enumeration.InputTestDivision.*;
import static com.mes.mesBackend.entity.enumeration.InputTestState.*;
import static com.mes.mesBackend.entity.enumeration.ItemLogType.BAD_AMOUNT;

// 14-2. 검사 등록
// 15-2. 검사 등록
// 16-2. 검사 등록
@Service
@RequiredArgsConstructor
public class InputTestDetailServiceImpl implements InputTestDetailService {
    private final InputTestDetailRepository inputTestDetailRepo;
    private final InputTestRequestService inputTestRequestService;
    private final ModelMapper mapper;
    private final InputTestRequestRepository inputTestRequestRepo;
    private final UserService userService;
    private final LotMasterRepository lotMasterRepo;
    private final S3Uploader s3Uploader;
    private final AmountHelper amountHelper;

    // 검사요청정보 리스트 조회
    // 검색조건: 창고 id, 품명|품목, 완료여부, 입고번호, 품목그룹 id, LOT 유형 id, 요청기간 from~toDate, 제조사 id
    @Override
    public List<InputTestRequestInfoResponse> getInputTestRequestInfo(
            Long warehouseId,
            String itemNoAndName,
            Boolean completionYn,
            Long purchaseInputNo,
            Long itemGroupId,
            Long lotTypeId,
            LocalDate fromDate,
            LocalDate toDate,
            Long manufactureId,
            InputTestDivision inputTestDivision,
            TestType testType
    ) throws NotFoundException {
        List<InputTestRequestInfoResponse> responses = inputTestDetailRepo.findInputTestRequestInfoResponseByCondition(
                warehouseId, itemNoAndName, completionYn, purchaseInputNo, itemGroupId, lotTypeId, fromDate, toDate, manufactureId, inputTestDivision, testType
        );
        for (InputTestRequestInfoResponse response : responses) {
            int testAmount = inputTestDetailRepo.findTestAmountByInputTestRequestId(response.getId()).stream().mapToInt(Integer::intValue).sum();
            response.setTestAmount(testAmount);
            if (inputTestDivision.equals(PRODUCT)) {
                    String workOrderNo = inputTestRequestRepo.findWorkOrderNoByLotId(response.getLotMasterId()).orElseThrow(() -> new NotFoundException("lot 에 해당하는 작업지시가 없음."));
                    response.setWorkOrderNo(workOrderNo);
                }
            }
        return responses.stream().map(res -> res.division(inputTestDivision)).collect(Collectors.toList());
    }

    /*
    * 검사정보 생성
    * 1. 검사정보의 총 검사수량이 검사의뢰요청한 요청수량과 같아지면 COMPLETION 변경
    * 3. 검사의뢰요청의 상태값이 COMPLETION 이면 등록 x
    * 4. lotMaster 재고수량(양품수량), 불량수량(부적합수량), 검사수량(총검사수량) 변경
    * */
    @Override
    public InputTestDetailResponse createInputTestDetail(
            Long inputTestRequestId,
            InputTestDetailRequest inputTestDetailRequest,
            InputTestDivision inputTestDivision
    ) throws NotFoundException, BadRequestException {
        int inputFairQualityAmount = inputTestDetailRequest.getFairQualityAmount();
        int inputIncongruityAmount = inputTestDetailRequest.getIncongruityAmount();
        int inputTestAmount = inputTestDetailRequest.getTestAmount();

        // 양품수량, 부적합수량이 검사수량보다 크면 예외
        throwIfFairQualityAmountAndIncongruityAmountGreaterThanTestAmount(inputFairQualityAmount, inputIncongruityAmount, inputTestAmount);

        InputTestRequest inputTestRequest = inputTestRequestService.getInputTestRequestOrThrow(inputTestRequestId, inputTestDivision);
        LotMaster lotMaster = inputTestRequest.getLotMaster();
        int checkRequestAmount = inputTestRequest.getRequestAmount();

        int allTestAmount = inputTestDetailRepo.findTestAmountByInputTestRequestId(inputTestRequestId)
                .stream().mapToInt(Integer::intValue).sum();

        // 기존 검사수량 + 입력 검사수량 이 검사요청수량보다 크면 예외
        throwIfTestAmountGreaterThanCheckRequestAmount(allTestAmount, checkRequestAmount, inputTestAmount);

        User user = userService.getUserOrThrow(inputTestDetailRequest.getUserId());
        InputTestDetail inputTestDetail = mapper.toEntity(inputTestDetailRequest, InputTestDetail.class);
        inputTestDetail.create(inputTestRequest, user);

        // 검사요청에 해당하는 총 검사수량이 lotMaster 의 checkRequestAmount 와 같으면 COMPLETION
        if ((allTestAmount + inputTestAmount) == checkRequestAmount) inputTestRequest.setInputTestState(COMPLETION);
        else inputTestRequest.setInputTestState(ONGOING);

        // lotMaster 의 검사수량 변경
        // 재고수량(양품수량), 불량수량(부적합수량), 검사수량(총검사수량) 변경
        lotMaster.putStockAmountAndBadItemAmountAndCheckAmount(
                lotMaster.getStockAmount() - inputIncongruityAmount,
                lotMaster.getBadItemAmount() + inputTestDetail.getIncongruityAmount(),
                lotMaster.getCheckAmount() + inputTestDetail.getTestAmount()
        );

        lotMasterRepo.save(lotMaster);
        inputTestDetailRepo.save(inputTestDetail);       // 검사상세정보 저장
        inputTestRequestRepo.save(inputTestRequest);     // 검사의뢰요청 저장
        // 수량변동에 대한 기록저장
        amountHelper.amountUpdate(lotMaster.getItem().getId(), lotMaster.getWareHouse().getId(), null, BAD_AMOUNT, inputTestDetail.getIncongruityAmount(), inputTestDivision.equals(OUT_SOURCING));
        return getInputTestDetail(inputTestRequestId, inputTestDetail.getId(), inputTestDivision);
    }

    // 검사정보 단일조회
    @Override
    public InputTestDetailResponse getInputTestDetail(
            Long inputTestRequestId,
            Long inputTestDetailId,
            InputTestDivision inputTestDivision
    ) throws NotFoundException {
        return inputTestDetailRepo.findDetailByInputTestRequestIdAndInputTestDetailIdAndDeleteYnFalse(inputTestRequestId, inputTestDetailId, inputTestDivision)
                .orElseThrow(() -> new NotFoundException("inputTestDetail does not exist. " +
                        "input inputTestRequestId: " + inputTestRequestId + ", " +
                        "input inputTestDetailId: " + inputTestDetailId)).division(inputTestDivision);
    }

    // 검사정보 전체조회
    @Override
    public List<InputTestDetailResponse> getInputTestDetails(Long inputTestRequestId, InputTestDivision inputTestDivision) throws NotFoundException {
        InputTestRequest inputTestRequest = inputTestRequestService.getInputTestRequestOrThrow(inputTestRequestId, inputTestDivision);
        return inputTestDetailRepo.findDetailsByInputTestRequestIdAndDeleteYnFalse(inputTestRequest.getId())
                .stream().map(res -> res.division(inputTestDivision)).collect(Collectors.toList());
    }

    // 검사정보 수정
    /*
    * 1. 총 검사수량과 입력받은 검사수량이 검사요청수량보다 크면 예외
    * 2. 검사정보의 총 검사수량이 검사의뢰요청한 요청수량과 같아지면 COMPLETION 변경
    * 3. lotMaster 의 검사수량 변경
    * */
    @Override
    public InputTestDetailResponse updateInputTestDetail(
            Long inputTestRequestId,
            Long inputTestDetailId,
            InputTestDetailRequest inputTestDetailRequest,
            InputTestDivision inputTestDivision
    ) throws NotFoundException, BadRequestException {
        int newTestAmount = inputTestDetailRequest.getTestAmount();
        int newFairQualityAmount = inputTestDetailRequest.getFairQualityAmount();
        int newIncongruityAmount = inputTestDetailRequest.getIncongruityAmount();

        // 양품수량, 부적합수량이 검사수량보다 크면 예외
        throwIfFairQualityAmountAndIncongruityAmountGreaterThanTestAmount(newFairQualityAmount, newIncongruityAmount, newTestAmount);

        InputTestRequest inputTestRequest = inputTestRequestService.getInputTestRequestOrThrow(inputTestRequestId, inputTestDivision);
        InputTestDetail findInputTestDetail = getInputTestDetailOrThrow(inputTestRequestId, inputTestDetailId, inputTestDivision);

        LotMaster lotMaster = inputTestRequest.getLotMaster();

        int allTestAmount = inputTestDetailRepo.findTestAmountByInputTestRequestId(inputTestRequestId)
                .stream().mapToInt(Integer::intValue).sum();

        int findTestAmount = findInputTestDetail.getTestAmount();                   // 기존 검사수량
        int findIncongruityAmount = findInputTestDetail.getIncongruityAmount();     // 기존 부적합수량
        int findFairQualityAmount = findInputTestDetail.getFairQualityAmount();

        // 기존 검사수량 + 입력 검사수량이 검사요청수량보다 크면 예외
        throwIfTestAmountGreaterThanCheckRequestAmount(
                allTestAmount - findTestAmount,
                lotMaster.getCheckRequestAmount(),
                newTestAmount
        );

        InputTestDetail newInputTestDetail = mapper.toEntity(inputTestDetailRequest, InputTestDetail.class);
        User newUser = userService.getUserOrThrow(inputTestDetailRequest.getUserId());
        findInputTestDetail.update(newInputTestDetail, newUser);

        int allIncongruityAmount = lotMaster.getBadItemAmount();        // 총 부적합 수량
        int allFairQualityAmount = lotMaster.getStockAmount();          // 총 양품수량

        // 검사요청에 해당하는 총 검사수량이 lotMaster 의 checkRequestAmount 와 같으면 COMPLETION
        if (((allTestAmount - findTestAmount) + newTestAmount) == lotMaster.getCheckRequestAmount()) inputTestRequest.setInputTestState(COMPLETION);
        else inputTestRequest.setInputTestState(ONGOING);

        inputTestDetailRepo.save(findInputTestDetail);
        inputTestRequestRepo.save(inputTestRequest);

        // 3. lotMaster 의 검사수량 변경
        // 재고수량(수정된 양품수량), 불량수량(수정된 부적합수량), 검사수량(수정된 검사수량) 변경
        lotMaster.putStockAmountAndBadItemAmountAndCheckAmount(
                (allFairQualityAmount + findIncongruityAmount) - newIncongruityAmount,
                (allIncongruityAmount - findIncongruityAmount) + newIncongruityAmount,
                (lotMaster.getCheckAmount() - findTestAmount) + newTestAmount
        );

        lotMasterRepo.save(lotMaster);

        // 수량변동에 대한 기록저장
        amountHelper.amountUpdate(lotMaster.getItem().getId(), lotMaster.getWareHouse().getId(), null, BAD_AMOUNT, (findIncongruityAmount - newIncongruityAmount) * -1, inputTestDivision.equals(OUT_SOURCING));
        amountHelper.amountUpdate(lotMaster.getItem().getId(), lotMaster.getWareHouse().getId(), null, BAD_AMOUNT, (findFairQualityAmount - newFairQualityAmount) * -1, inputTestDivision.equals(OUT_SOURCING));
        return getInputTestDetail(inputTestRequestId, inputTestDetailId, inputTestDivision);
    }

    // 검사정보 삭제
    @Override
    public void deleteInputTestDetail(Long inputTestRequestId, Long inputTestDetailId, InputTestDivision inputTestDivision) throws NotFoundException {
        InputTestRequest inputTestRequest = inputTestRequestService.getInputTestRequestOrThrow(inputTestRequestId, inputTestDivision);
        InputTestDetail findInputTestDetail = getInputTestDetailOrThrow(inputTestRequestId, inputTestDetailId, inputTestDivision);

        findInputTestDetail.delete();

        int findTestAmount = findInputTestDetail.getTestAmount();
        int findIncongruityAmount = findInputTestDetail.getIncongruityAmount();
        int findFairQualityAmount = findInputTestDetail.getFairQualityAmount();

        // lotMaster 재고수량, 불량수량, 검사수량 변경
        LotMaster lotMaster = inputTestRequest.getLotMaster();
        lotMaster.putStockAmountAndBadItemAmountAndCheckAmount(
                lotMaster.getStockAmount() + findIncongruityAmount,
                lotMaster.getBadItemAmount() - findIncongruityAmount,
                lotMaster.getCheckAmount() - findTestAmount
        );

        if (lotMaster.getCheckAmount() > 1) inputTestRequest.setInputTestState(ONGOING);
        else inputTestRequest.setInputTestState(SCHEDULE);

        inputTestDetailRepo.save(findInputTestDetail);      // 삭제
        inputTestRequestRepo.save(inputTestRequest);

        // 수량변동에 대한 기록저장
        amountHelper.amountUpdate(lotMaster.getItem().getId(), lotMaster.getWareHouse().getId(), null, BAD_AMOUNT, findIncongruityAmount * -1, inputTestDivision.equals(OUT_SOURCING));
        amountHelper.amountUpdate(lotMaster.getItem().getId(), lotMaster.getWareHouse().getId(), null, BAD_AMOUNT, findFairQualityAmount * -1, inputTestDivision.equals(OUT_SOURCING));

        lotMasterRepo.save(lotMaster);
    }

    // 검사성적서 파일
    // 검사성적서 파일 명: input-test/검사정보 id/test-report-files/파일명(현재날짜)
    @Override
    public InputTestDetailResponse createTestReportFileToInputTestDetail(
            Long inputTestRequestId,
            Long inputTestDetailId,
            MultipartFile testReportFile,
            InputTestDivision inputTestDivision
    ) throws NotFoundException, BadRequestException, IOException {
        InputTestDetail inputTestDetail = getInputTestDetailOrThrow(inputTestRequestId, inputTestDetailId, inputTestDivision);
        String partTestReportFileName = "part-input-test/" + inputTestDetail.getId() + "/test-report-files/";
        String outsourcingTestReportFileName = "outsourcing-input-test/" + inputTestDetail.getId() + "/test-report-files/";
        // inputTestDivision 따라서 파일명 다르게 리턴
        String testReportFileName = inputTestDivision.equals(PART) ? partTestReportFileName : outsourcingTestReportFileName;

        String testReportFileUrl = s3Uploader.upload(testReportFile, testReportFileName);
        inputTestDetail.setTestReportFileUrl(testReportFileUrl);
        inputTestDetailRepo.save(inputTestDetail);

        return getInputTestDetail(inputTestRequestId, inputTestDetailId, inputTestDivision);
    }

    // COC 파일 추가
    // COC 파일 명: input-test/검사정보 id/coc-files/파일명(현재날짜)
    @Override
    public InputTestDetailResponse createCocFileToInputTestDetail(
            Long inputTestRequestId,
            Long inputTestDetailId,
            MultipartFile cocFile,
            InputTestDivision inputTestDivision
    ) throws NotFoundException, BadRequestException, IOException {
        InputTestDetail inputTestDetail = getInputTestDetailOrThrow(inputTestRequestId, inputTestDetailId, inputTestDivision);
        String partCocFileName = "part-input-test/" + inputTestDetail.getId() + "/coc-files/";
        String outsourcingCocFileName = "outsourcing-input-test/" + inputTestDetail.getId() + "/coc-files/";

        String cocFileName = inputTestDivision.equals(PART) ? partCocFileName : outsourcingCocFileName;

        String cocFileUrl = s3Uploader.upload(cocFile, cocFileName);
        inputTestDetail.setCocFileUrl(cocFileUrl);
        inputTestDetailRepo.save(inputTestDetail);

        return getInputTestDetail(inputTestRequestId, inputTestDetailId, inputTestDivision);
    }

    // 파일 삭제
    // 구매입고의 coc, testReport false 변경
    @Override
    public void deleteTestReportFileAndCocFileToInputTestDetail(
            Long inputTestRequestId,
            Long inputTestDetailId,
            boolean testReportDeleteYn,
            boolean cocDeleteYn,
            InputTestDivision inputTestDivision
    ) throws NotFoundException {
        InputTestDetail inputTestDetail = getInputTestDetailOrThrow(inputTestRequestId, inputTestDetailId, inputTestDivision);
        if (testReportDeleteYn) inputTestDetail.setTestReportFileUrl(null);
        if (cocDeleteYn) inputTestDetail.setCocFileUrl(null);
        inputTestDetailRepo.save(inputTestDetail);
    }

    // 검사정보 단일 조회 및 예외
    private InputTestDetail getInputTestDetailOrThrow(Long inputTestRequestId, Long inputTestDetailId, InputTestDivision inputTestDivision) throws NotFoundException {
        InputTestRequest inputTestRequest = inputTestRequestService.getInputTestRequestOrThrow(inputTestRequestId, inputTestDivision);
        return inputTestDetailRepo.findByInputTestRequestAndIdAndDeleteYnFalse(inputTestRequest, inputTestDetailId)
                .orElseThrow(() -> new NotFoundException("inputTestDetail does not exist. " +
                        "input inputTestRequestId: " + inputTestRequestId + ", " +
                        "input inputTestDetailId: " + inputTestDetailId)
                );
    }

    // 입력받은 양품수량 + 부적합수량이 입력받은 검사수량보다 크면 예외처리
    private void throwIfFairQualityAmountAndIncongruityAmountGreaterThanTestAmount(
            int fairQualityAmount,
            int incongruityAmount,
            int testAmount
    ) throws BadRequestException {
        if (incongruityAmount + fairQualityAmount > testAmount) {
            throw new BadRequestException(
                    "incongruityAmount and fairQualityAmount cannot be greater than testAmount. " +
                            "input testAmount: " + testAmount + ", " +
                            "input fairQualityAmount: " + fairQualityAmount + ", " +
                            "input incongruityAmount: " + incongruityAmount
            );
        }
    }

    // 기존 검사수량 + 입력 검사수량 이 검사요청수량보다 크면 예외
    private void throwIfTestAmountGreaterThanCheckRequestAmount(int allTestAmount, int checkRequestAmount, int inputTestAmount) throws BadRequestException {
        if ((inputTestAmount + allTestAmount) > checkRequestAmount) {
            throw new BadRequestException("testAmount cannot be greater than checkRequestAmount. " +
                    "input testAmount: " + inputTestAmount + ", " +
                    "checkRequestAmount: " + checkRequestAmount + ", " +
                    "allTestAmount: " + allTestAmount
            );
        }
    }
}
