package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.BadItemEnrollmentResponse;
import com.mes.mesBackend.dto.response.BadItemWorkOrderResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

// 8-5. 불량 등록
public interface BadItemEnrollmentService {
    // 작업지시 정보 리스트 조회, 검색조건: 작업장 id, 작업라인 id, 품목그룹 id, 제조오더번호, JOB NO, 작업기간 fromDate~toDate, 품번|품목
    List<BadItemWorkOrderResponse> getWorkOrders(
            Long workCenterId,
            Long workLineId,
            Long itemGroupId,
            String produceOrderNo,
            String workOrderNo,
            LocalDate fromDate,
            LocalDate toDate,
            String itemNoAndItemName
    ) throws NotFoundException;
    // 불량유형 정보 생성
    BadItemEnrollmentResponse createBadItemEnrollment(Long workOrderId, Long badItemId, int badItemAmount) throws NotFoundException, BadRequestException;
    // 불량유형 정보 전체 조회
    List<BadItemEnrollmentResponse> getBadItemEnrollments(Long workOrderId) throws NotFoundException;
    // 불량유형 정보 수정 (불량수량)
    BadItemEnrollmentResponse updateBadItemEnrollment(Long workOrderId, Long badItemEnrollmentId, int badItemAmount) throws NotFoundException, BadRequestException;
    // 불량유형 정보 삭제
    void deleteBadItemEnrollment(Long workOrderId, Long badItemEnrollmentId) throws NotFoundException;
}
