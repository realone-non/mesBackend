package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.InputTestDetailRequest;
import com.mes.mesBackend.dto.response.InputTestDetailResponse;
import com.mes.mesBackend.dto.response.InputTestRequestInfoResponse;
import com.mes.mesBackend.entity.InputTestDetail;
import com.mes.mesBackend.entity.InputTestRequest;
import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.entity.User;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
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

import static com.mes.mesBackend.entity.enumeration.InputTestState.COMPLETION;
import static com.mes.mesBackend.entity.enumeration.InputTestState.ONGOING;

// 14-2. 검사 등록
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
            Long manufactureId
    ) {
        return inputTestDetailRepo.findInputTestRequestInfoResponseByCondition(warehouseId, itemNoAndName, completionYn, purchaseInputNo, itemGroupId, lotTypeId, fromDate, toDate, manufactureId);
    }

    /*
    * 검사정보 생성
    * 1. 검사정보의 총 검사수량이 검사의뢰요청한 요청수량과 같아지면 COMPLETION 변경
    * 3. 검사의뢰요청의 상태값이 COMPLETION 이면 등록 x
    * 4. lotMaster 재고수량(양품수량), 불량수량(부적합수량), 검사수량(총검사수량) 변경
    * */
    @Override
    public InputTestDetailResponse createInputTestDetail(Long inputTestRequestId, InputTestDetailRequest inputTestDetailRequest) throws NotFoundException, BadRequestException {
        throwIfFairQualityAmountAndIncongruityAmountGreaterThanTestAmount(
                inputTestDetailRequest.getFairQualityAmount(),
                inputTestDetailRequest.getIncongruityAmount(),
                inputTestDetailRequest.getTestAmount()
        );      // 양품수량, 부적합수량이 검사수량보다 크면 예외

        InputTestRequest inputTestRequest = inputTestRequestService.getInputTestRequestOrThrow(inputTestRequestId);
        User user = userService.getUserOrThrow(inputTestDetailRequest.getUserId());
        InputTestDetail inputTestDetail = mapper.toEntity(inputTestDetailRequest, InputTestDetail.class);
        inputTestDetail.create(inputTestRequest, user);

        if (inputTestRequest.getRequestAmount() == inputTestDetail.getTestAmount()) inputTestRequest.setInputTestState(COMPLETION);
        else inputTestRequest.setInputTestState(ONGOING);

        inputTestDetailRepo.save(inputTestDetail);       // 검사상세정보 저장
        inputTestRequestRepo.save(inputTestRequest);     // 검사의뢰요청 저장

        LotMaster lotMaster = inputTestRequest.getLotMaster();      // 4. lotMaster 의 검사수량 변경

        // 기존 재고수량, 기존불량수량
        // 재고수량(양품수량), 불량수량(부적합수량), 검사수량(총검사수량) 변경
        lotMaster.putStockAmountAndBadItemAmountAndCheckAmount(
                inputTestDetail.getFairQualityAmount(),
                inputTestDetail.getIncongruityAmount(),
                inputTestDetailRequest.getTestAmount()
        );
        lotMasterRepo.save(lotMaster);
        return getInputTestDetail(inputTestRequestId, inputTestDetail.getId());
    }

    // 검사정보 단일조회
    @Override
    public InputTestDetailResponse getInputTestDetail(Long inputTestRequestId, Long inputTestDetailId) throws NotFoundException {
        return inputTestDetailRepo.findDetailByInputTestRequestIdAndInputTestDetailIdAndDeleteYnFalse(inputTestRequestId, inputTestDetailId)
                .orElseThrow(() -> new NotFoundException("inputTestDetail does not exist. " +
                        "input inputTestRequestId: " + inputTestRequestId + ", " +
                        "input inputTestDetailId: " + inputTestDetailId));
    }

    // 검사정보 전체조회
    @Override
    public List<InputTestDetailResponse> getInputTestDetails(Long inputTestRequestId) throws NotFoundException {
        InputTestRequest inputTestRequest = inputTestRequestService.getInputTestRequestOrThrow(inputTestRequestId);
        return inputTestDetailRepo.findDetailsByInputTestRequestIdAndDeleteYnFalse(inputTestRequest.getId());
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
            InputTestDetailRequest inputTestDetailRequest
    ) throws NotFoundException, BadRequestException {
        throwIfFairQualityAmountAndIncongruityAmountGreaterThanTestAmount(      // 양품수량, 부적합수량이 검사수량보다 크면 예외
                inputTestDetailRequest.getFairQualityAmount(),
                inputTestDetailRequest.getIncongruityAmount(),
                inputTestDetailRequest.getTestAmount()
        );

        InputTestRequest inputTestRequest = inputTestRequestService.getInputTestRequestOrThrow(inputTestRequestId);
        InputTestDetail findInputTestDetail = getInputTestDetailOrThrow(inputTestRequestId, inputTestDetailId);
        InputTestDetail newInputTestDetail = mapper.toEntity(inputTestDetailRequest, InputTestDetail.class);
        User newUser = userService.getUserOrThrow(inputTestDetailRequest.getUserId());
        findInputTestDetail.update(newInputTestDetail, newUser);

        if (inputTestRequest.getRequestAmount() == newInputTestDetail.getTestAmount()) inputTestRequest.setInputTestState(COMPLETION);
        else inputTestRequest.setInputTestState(ONGOING);

        inputTestDetailRepo.save(findInputTestDetail);
        inputTestRequestRepo.save(inputTestRequest);

        LotMaster lotMaster = inputTestRequest.getLotMaster();      // 3. lotMaster 의 검사수량 변경

        // 재고수량(수정된 양품수량), 불량수량(수정된 부적합수량), 검사수량(수정된 검사수량) 변경
        lotMaster.putStockAmountAndBadItemAmountAndCheckAmount(
                inputTestDetailRequest.getFairQualityAmount(),
                inputTestDetailRequest.getIncongruityAmount(),
                inputTestDetailRequest.getTestAmount()
        );

        lotMasterRepo.save(lotMaster);
        return getInputTestDetail(inputTestRequestId, inputTestDetailId);
    }

    // 검사정보 삭제
    @Override
    public void deleteInputTestDetail(Long inputTestRequestId, Long inputTestDetailId) throws NotFoundException {
        InputTestRequest inputTestRequest = inputTestRequestService.getInputTestRequestOrThrow(inputTestRequestId);
        InputTestDetail inputTestDetail = getInputTestDetailOrThrow(inputTestRequestId, inputTestDetailId);
        inputTestDetail.delete();
        inputTestDetailRepo.save(inputTestDetail);      // 삭제

        inputTestRequest.changedSchedule();           // 검사의뢰요청 상태값 Schedule 변경
        inputTestRequestRepo.save(inputTestRequest);

        LotMaster lotMaster = inputTestRequest.getLotMaster();      // lotMaster 검사수량 변경
        lotMaster.putStockAmountAndBadItemAmountAndCheckAmount(0, 0, lotMaster.getCreatedAmount());
        lotMasterRepo.save(lotMaster);
    }

    // 검사성적서 파일
    // 검사성적서 파일 명: input-test/검사정보 id/test-report-files/파일명(현재날짜)
    @Override
    public InputTestDetailResponse createTestReportFileToInputTestDetail(
            Long inputTestRequestId,
            Long inputTestDetailId,
            MultipartFile testReportFile
    ) throws NotFoundException, BadRequestException, IOException {
        InputTestDetail inputTestDetail = getInputTestDetailOrThrow(inputTestRequestId, inputTestDetailId);
        String testReportFileName = "input-test/" + inputTestDetail.getId() + "/test-report-files/";
        String testReportFileUrl = s3Uploader.upload(testReportFile, testReportFileName);
        inputTestDetail.setTestReportFileUrl(testReportFileUrl);
        inputTestDetailRepo.save(inputTestDetail);

        return getInputTestDetail(inputTestRequestId, inputTestDetailId);
    }

    // COC 파일 추가
    // COC 파일 명: input-test/검사정보 id/coc-files/파일명(현재날짜)
    @Override
    public InputTestDetailResponse createCocFileToInputTestDetail(
            Long inputTestRequestId,
            Long inputTestDetailId,
            MultipartFile cocFile
    ) throws NotFoundException, BadRequestException, IOException {
        InputTestDetail inputTestDetail = getInputTestDetailOrThrow(inputTestRequestId, inputTestDetailId);
        String cocFileName = "input-test/" + inputTestDetail.getId() + "/coc-files/";
        String cocFileUrl = s3Uploader.upload(cocFile, cocFileName);
        inputTestDetail.setCocFileUrl(cocFileUrl);
        inputTestDetailRepo.save(inputTestDetail);

        return getInputTestDetail(inputTestRequestId, inputTestDetailId);
    }

    // 파일 삭제
    // 구매입고의 coc, testReport false 변경
    @Override
    public void deleteTestReportFileAndCocFileToInputTestDetail(
            Long inputTestRequestId,
            Long inputTestDetailId,
            boolean testReportDeleteYn,
            boolean cocDeleteYn
    ) throws NotFoundException {
        InputTestDetail inputTestDetail = getInputTestDetailOrThrow(inputTestRequestId, inputTestDetailId);
        if (testReportDeleteYn) inputTestDetail.setTestReportFileUrl(null);
        if (cocDeleteYn) inputTestDetail.setCocFileUrl(null);
        inputTestDetailRepo.save(inputTestDetail);
    }

    // 검사정보 단일 조회 및 예외
    private InputTestDetail getInputTestDetailOrThrow(Long inputTestRequestId, Long inputTestDetailId) throws NotFoundException {
        InputTestRequest inputTestRequest = inputTestRequestService.getInputTestRequestOrThrow(inputTestRequestId);
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

    private void throwIfTestAmountGreaterThanRequestAmount(int testAmount, int requestAmount) throws BadRequestException {
        if (testAmount > requestAmount) {   // 총 검사수량과 입력받은 검사수량이 검사요청수량보다 크면
            throw new BadRequestException("testAmount is greater than requestAmount. " +
                    "input testAmount: " + testAmount + ", " +
                    "requestAmount: " + requestAmount);
        }
    }
}
