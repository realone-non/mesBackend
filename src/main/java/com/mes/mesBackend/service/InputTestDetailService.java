package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.InputTestDetailRequest;
import com.mes.mesBackend.dto.response.InputTestDetailResponse;
import com.mes.mesBackend.dto.response.InputTestRequestInfoResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

// 14-2. 검사 등록
public interface InputTestDetailService {
    // 검사요청정보 리스트 조회
    // 검색조건: 창고 id, 품명|품목, 완료여부, 입고번호, 품목그룹 id, LOT 유형 id, 요청기간 from~toDate, 제조사 id
    List<InputTestRequestInfoResponse> getInputTestRequestInfo(
            Long warehouseId,
            String itemNoAndName,
            Boolean completionYn,
            Long purchaseInputNo,
            Long itemGroupId,
            Long lotTypeId,
            LocalDate fromDate,
            LocalDate toDate,
            Long manufactureId
    );
    // 검사정보 생성
    InputTestDetailResponse createInputTestDetail(Long inputTestRequestId, InputTestDetailRequest inputTestDetailRequest) throws NotFoundException, BadRequestException;
    // 검사정보 단일조회
    InputTestDetailResponse getInputTestDetail(Long inputTestRequestId, Long inputTestDetailId) throws NotFoundException;
    // 검사정보 전체조회
    List<InputTestDetailResponse> getInputTestDetails(Long inputTestRequestId) throws NotFoundException;
    // 검사정보 수정
    InputTestDetailResponse updateInputTestDetail(Long inputTestRequestId, Long inputTestDetailId, InputTestDetailRequest inputTestDetailRequest) throws NotFoundException, BadRequestException;
    // 검사정보 삭제
    void deleteInputTestDetail(Long inputTestRequestId, Long inputTestDetailId) throws NotFoundException;
    // 검사성적서 파일
    InputTestDetailResponse createTestReportFileToInputTestDetail(Long inputTestRequestId, Long inputTestDetailId, MultipartFile testReportFile) throws NotFoundException, BadRequestException, IOException;
    // COC 파일 추가
    InputTestDetailResponse createCocFileToInputTestDetail(Long inputTestRequestId, Long inputTestDetailId, MultipartFile cocFile) throws NotFoundException, BadRequestException, IOException;
    // 파일 삭제
    void deleteTestReportFileAndCocFileToInputTestDetail(Long inputTestRequestId, Long inputTestDetailId, boolean testReportDeleteYn, boolean cocDeleteYn) throws NotFoundException;
}
