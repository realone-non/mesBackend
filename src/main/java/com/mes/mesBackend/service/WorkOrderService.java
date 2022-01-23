package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.WorkOrderCreateRequest;
import com.mes.mesBackend.dto.request.WorkOrderUpdateRequest;
import com.mes.mesBackend.dto.response.WorkOrderProduceOrderResponse;
import com.mes.mesBackend.dto.response.WorkOrderResponse;
import com.mes.mesBackend.entity.WorkOrderDetail;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

// 6-2. 작업지시 등록
public interface WorkOrderService {
    // 제조오더 정보 리스트 조회
    // 검색조건: 품목그룹 id, 품명|품번, 수주번호, 제조오더번호, 착수예정일 fromDate~endDate, 지시상태
    List<WorkOrderProduceOrderResponse> getProduceOrders(
            Long itemGroupId,
            String itemNoAndName,
            String contractNo,
            String produceOrderNo,
            LocalDate fromDate,
            LocalDate toDate,
            OrderState orderState
    );
    // 작업지시 생성
    WorkOrderResponse createWorkOrder(Long produceOrderId, WorkOrderCreateRequest workOrderRequest) throws NotFoundException, BadRequestException;
    // 작업지시 단일조회
    WorkOrderResponse getWorkOrderResponseOrThrow(Long produceOrderId, Long workOrderId) throws NotFoundException;
    // 작업지시 리스트 조회
    List<WorkOrderResponse> getWorkOrders(Long produceOrderId) throws NotFoundException;
    // 작업지시 수정
    WorkOrderResponse updateWorkOrder(Long produceOrderId, Long workOrderId, WorkOrderUpdateRequest workOrderUpdateRequest) throws NotFoundException, BadRequestException;
    // 작업지시 삭제
    void deleteWorkOrder(Long produceOrderId, Long workOrderId) throws NotFoundException, BadRequestException;
    // 작업지시 단일 조회 및 예외
    WorkOrderDetail getWorkOrderDetailOrThrow(Long id, Long produceOrderId) throws NotFoundException;
    // produceOrder(제조오더): 제조오더에 해당하는 workOrderDetail(작업지시) 의 orderState 상태값 별로 제조오더의 상태값도 변경됨.
//    void changeOrderStateOfProduceOrder(ProduceOrder produceOrder);
}
