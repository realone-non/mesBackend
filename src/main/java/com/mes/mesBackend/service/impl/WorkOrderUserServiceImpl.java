package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.WorkOrderUserResponse;
import com.mes.mesBackend.entity.ProduceOrder;
import com.mes.mesBackend.entity.ProductionPerformance;
import com.mes.mesBackend.entity.User;
import com.mes.mesBackend.entity.WorkOrderDetail;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.WorkOrderStateHelper;
import com.mes.mesBackend.repository.ProduceOrderRepository;
import com.mes.mesBackend.repository.ProductionPerformanceRepository;
import com.mes.mesBackend.repository.WorkOrderDetailRepository;
import com.mes.mesBackend.service.UserService;
import com.mes.mesBackend.service.WorkOrderUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.mes.mesBackend.entity.enumeration.OrderState.*;

// 8-2. 작업자 투입 수정
@Service
@RequiredArgsConstructor
public class WorkOrderUserServiceImpl implements WorkOrderUserService {
    private final WorkOrderDetailRepository workOrderDetailRepository;
    private final UserService userService;
    private final ProduceOrderRepository produceOrderRepo;
    private final WorkOrderDetailRepository workOrderDetailRepo;
    private final WorkOrderStateHelper workOrderStateHelper;
    private final ProductionPerformanceRepository productionPerformanceRepo;

    @Override
    public List<WorkOrderUserResponse> getWorkOrderUsers(
            Long workLineId,
            String produceOrderNo,
            Long itemAccountId,
            OrderState orderState,
            LocalDate fromDate,
            LocalDate toDate,
            String contractNo
    ) {
        List<WorkOrderUserResponse> findWorkOrderUsers =
                workOrderDetailRepository.findWorkOrderUserResponsesByCondition(workLineId, produceOrderNo, itemAccountId, orderState, fromDate, toDate, contractNo);
        findWorkOrderUsers.forEach(WorkOrderUserResponse::putCostTime);
        return findWorkOrderUsers;
    }

    // 작업자 투입 단일 조회
    @Override
    public WorkOrderUserResponse getWorkOrderUserResponseOrThrow(Long workOrderId) throws NotFoundException {
        WorkOrderUserResponse workOrderUser = workOrderDetailRepository.findWorkOrderUserResponseByIdAndDeleteYn(workOrderId)
                .orElseThrow(() -> new NotFoundException("workOrderUser does not exist. input workOrderDetail id: " + workOrderId));
        workOrderUser.putCostTime();
        return workOrderUser;
    }

    // 작업자 투입 수정
    @Override
    public WorkOrderUserResponse updateWorkOrderUser(
            Long workOrderDetailId,
            Long newUserId,
            LocalDateTime newStartDate,
            LocalDateTime newEndDate
    ) throws NotFoundException, BadRequestException {
        WorkOrderDetail workOrderDetail = workOrderDetailRepository.findByIdAndDeleteYnFalse(workOrderDetailId)
                .orElseThrow(() -> new NotFoundException("workOrderDetail does not exist. input id: " + workOrderDetailId));

        // 완료된 작업지시에 대해서만 날짜 수정 가능.
        if (!workOrderDetail.getOrderState().equals(COMPLETION)) throw new BadRequestException("작업지시 상태가 완료가 아니면 입력 또는 변경이 불가능 합니다.");
        // 시작날짜보다 종료날짜가 뒤면 예외
        checkStartDateAndEndDate(newStartDate, newEndDate);

        OrderState orderState;
        if (newStartDate != null && newEndDate != null) {
            orderState = COMPLETION;
        } else if (newStartDate != null) {
            orderState = ONGOING;
        } else if (newEndDate != null) {
            throw new BadRequestException("startDate cannot be null if endDate exists.");
        } else {
            throw new BadRequestException("startDate, endDate cannot be null.");
        }

        User newUser = userService.getUserOrThrow(newUserId);
        workOrderDetail.setOrderState(orderState);
        workOrderDetail.setStartDate(newStartDate);
        workOrderDetail.setEndDate(newEndDate);
        workOrderDetail.setUser(newUser);
        workOrderDetailRepository.save(workOrderDetail);

        // produceOrder(제조오더): 제조오더에 해당하는 workOrderDetail(작업지시) 의 orderState 상태값 별로 제조오더의 상태값도 변경됨.
        ProduceOrder produceOrder = getProduceOrderOrThrow(workOrderDetail.getProduceOrder().getId());
        produceOrder.setOrderState(getWorkOrderStateDesc(produceOrder.getId()));
        produceOrderRepo.save(produceOrder);

        // 생산실적: 작업지시에 해당하는 생산실적 없으면 새로 생성, 있으면 공정에 해당하는 컬럼에 update
        ProductionPerformance productionPerformance = workOrderStateHelper.getProductionPerformanceOrCreate(workOrderDetail);
        productionPerformance.updateProcessDateTime(workOrderDetail.getWorkProcess(), workOrderDetail.getOrderState());
        productionPerformanceRepo.save(productionPerformance);

        return getWorkOrderUserResponseOrThrow(workOrderDetailId);
    }

    // 제조오더 단일 조회 및 예외
    private ProduceOrder getProduceOrderOrThrow(Long id) throws NotFoundException {
        return produceOrderRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("[workOrderStateHelperError] produceOrder does not exist. produceOrderId: " + id));
    }

    // 제조오더에 해당하는 가장 최근 등록된 작업지시의 orderState
    private OrderState getWorkOrderStateDesc(Long produceOrderId) {
        return workOrderDetailRepo.findOrderStatesByProduceOrderId(produceOrderId)
                .orElse(SCHEDULE);
    }

//     시작날짜, 종료날자 둘다 입력 받았을 시 endDate 가 startDate 보다 과거면 badRequestException.
    private void checkStartDateAndEndDate(LocalDateTime startDate, LocalDateTime endDateTime) throws BadRequestException {
        if (startDate != null && endDateTime != null) {
            if (endDateTime.isBefore(startDate)) {
                throw new BadRequestException("endDate cannot be in the past than startDate. input startDateTime: " + startDate +", endDateTime: " + endDateTime);
            }
        }
    }
}
