package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.*;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

// 8-5. 불량 등록
public interface BadItemEnrollmentService {
    // 작업지시 정보 리스트 조회, 검색조건: 작업장 id, 작업공정 id, 품목그룹 id, 제조오더번호, JOB NO, 작업기간 fromDate~toDate, 품번|품목
    List<BadItemWorkOrderResponse> getWorkOrders(
            Long workCenterId,
            Long inputWorkProcessId,
            Long itemGroupId,
            String produceOrderNo,
            String workOrderNo,
            LocalDate fromDate,
            LocalDate toDate,
            String itemNoAndItemName
    ) throws NotFoundException;
    // 작업지시 별 작업완료 상세 리스트
    List<WorkOrderDetailResponse> getWorkOrderDetails(Long workOrderId) throws NotFoundException;
    // 작업완료 상세 리스트 별 불량정보 조회
    List<WorkOrderDetailBadItemResponse> getBadItemEnrollments(Long workOrderId, Long equipmentLotId) throws NotFoundException;
    // 작업완료 상세 리스트 별 불량정보 생성
    WorkOrderDetailBadItemResponse createBadItemEnrollment(Long workOrderId, Long equipmentLotId, Long badItemId, int badItemAmount) throws NotFoundException, BadRequestException;
    // 작업완료 상세 리스트 별 불량정보 수정
    WorkOrderDetailBadItemResponse updateBadItemEnrollment(Long workOrderId, Long equipmentLotId, Long badItemEnrollmentId, int badItemAmount) throws NotFoundException, BadRequestException;
    // 작업완료 상세 리스트 별 불량정보 삭제
    void deleteBadItemEnrollment(Long workOrderId, Long equipmentLotId, Long badItemEnrollmentId) throws NotFoundException, BadRequestException;

    // ====================================== 작업지시 불량률 조회 =================================
    // 작업지시 불량률 조회
    List<WorkOrderBadItemStatusResponse> getWorkOrderBadItems(Long workProcessId, String workOrderNo, String itemNoAndItemName, Long userId, LocalDate fromDate, LocalDate toDate) throws NotFoundException;
    // 작업지시 불량률 상세 조회
    List<WorkOrderBadItemStatusDetailResponse> getWorkOrderBadItemDetails(Long workOrderId) throws NotFoundException;

//    // 불량유형 정보 생성
//    BadItemEnrollmentResponse createBadItemEnrollment(Long workOrderId, Long badItemId, int badItemAmount) throws NotFoundException, BadRequestException;
//    // 불량유형 정보 전체 조회
//    List<BadItemEnrollmentResponse> getBadItemEnrollments(Long workOrderId) throws NotFoundException;
//    // 불량유형 정보 수정 (불량수량)
//    BadItemEnrollmentResponse updateBadItemEnrollment(Long workOrderId, Long badItemEnrollmentId, int badItemAmount) throws NotFoundException, BadRequestException;
//    // 불량유형 정보 삭제
//    void deleteBadItemEnrollment(Long workOrderId, Long badItemId) throws NotFoundException, BadRequestException;
}
