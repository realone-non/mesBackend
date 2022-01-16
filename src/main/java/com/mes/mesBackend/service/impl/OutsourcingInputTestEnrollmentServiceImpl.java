package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.InputTestRequestInfoResponse;
import com.mes.mesBackend.helper.S3Uploader;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.InputTestDetailRepository;
import com.mes.mesBackend.repository.InputTestRequestRepository;
import com.mes.mesBackend.repository.LotMasterRepository;
import com.mes.mesBackend.service.InputTestRequestService;
import com.mes.mesBackend.service.OutsourcingInputTestEnrollmentService;
import com.mes.mesBackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// 15-2. 검사등록
@Service
@RequiredArgsConstructor
public class OutsourcingInputTestEnrollmentServiceImpl implements OutsourcingInputTestEnrollmentService {
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
        return inputTestDetailRepo.findInputTestRequestInfoResponseByCondition(warehouseId, itemNoAndName, completionYn, purchaseInputNo, itemGroupId, lotTypeId, fromDate, toDate, manufactureId, false);
    }



}
