package com.mes.mesBackend.helper.impl;

import ch.qos.logback.core.joran.conditional.IfAction;
import com.mes.mesBackend.entity.ProduceOrder;
import com.mes.mesBackend.entity.WorkOrderDetail;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.WorkOrderStateHelper;
import com.mes.mesBackend.repository.ProduceOrderRepository;
import com.mes.mesBackend.repository.WorkOrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.mes.mesBackend.entity.enumeration.OrderState.*;
import static com.mes.mesBackend.entity.enumeration.WorkProcessDivision.MATERIAL_MIXING;

// 작업지시 orderState 변경
@Component
@RequiredArgsConstructor
public class WorkOrderStateHelperImpl implements WorkOrderStateHelper {
    private final WorkOrderDetailRepository workOrderDetailRepo;
    private final ProduceOrderRepository produceOrderRepo;

    // 작업지시 orderState 변경
    /*
     * - orderState 별로 workOrderDetail 의 scheduleDate, startDate, endDate 값 update
     * - 제조오더에 해당하는 젤 마지막 등록 작업지시의 orderState 값으로 제조오더 orderState 값 update
     * - 제조오더에 해당하는 productionPerformance 가 없고, 작업시지의 상태값이 COMPLETION 이면 생성, 있으면 공정에 해당하는 컬럼에 값 update
     * */
    @Override
    public void updateOrderState(Long workOrderDetailId, OrderState orderState) throws NotFoundException {
        // 작업지시: orderState 따라 작업지시 날짜값 update
        WorkOrderDetail workOrder = getWorkOrderDetailOrThrow(workOrderDetailId);
        workOrder.changeOrderStateDate(orderState);
        workOrder.setOrderState(orderState);
        workOrderDetailRepo.save(workOrder);

        // 제조오더: 작업지시 상태값 따라서 제조오더 상태값 update
        ProduceOrder produceOrder = getProduceOrderOrThrow(workOrder.getProduceOrder().getId());
        produceOrder.setOrderState(getWorkOrderStateDesc(produceOrder.getId()));
        produceOrderRepo.save(produceOrder);
    }

    // 작업수량, 지시수량으로 상태값 구하기
    /*
    * 지시수량보다 작업수량이 같거나 커지면? COMPLETION
    * 지시수량보다 작업수량이 작으면? ONGOING
    * 작업수량이 0 이면? SCHEDULE
    * 원료혼합 공정일 경우? COMPLETION 으로 변경되지 않음
    * */
    @Override
    public OrderState findOrderStateByOrderAmountAndProductAmount(int orderAmount, int productAmount, WorkProcessDivision workProcessDivision) {
        if (workProcessDivision.equals(MATERIAL_MIXING)) return ONGOING;
        else {
            if (productAmount >= orderAmount) return COMPLETION;
            else if (productAmount == 0) return SCHEDULE;
            else return ONGOING;
        }
    }

    // 제조오더에 해당되는 작업지시의 모든 지시상태
    @Override
    public OrderState getWorkOrderStateDesc(Long produceOrderId) {
        List<OrderState> orderStates = workOrderDetailRepo.findOrderStatesByProduceOrderId(produceOrderId);

        // 모든 상태값이 COMPLETION 이면 COMPLETION
        // 하나라도 ONGOING 이면 ONGOING
        // 모두 SCHEDULE 이면 SCHEDULE
        return orderStates.stream().allMatch(a -> a.equals(COMPLETION)) ? COMPLETION
                : orderStates.stream().anyMatch(o -> o.equals(ONGOING)) ? ONGOING
                : SCHEDULE;
    }

    // 작업지시 단일 조회 및 예외
    private WorkOrderDetail getWorkOrderDetailOrThrow(Long id) throws NotFoundException {
        return workOrderDetailRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("[workOrderStateHelperError] workOrderDetail does not exist. workOrderDetailId: " + id));
    }

    // 제조오더 단일 조회 및 예외
    private ProduceOrder getProduceOrderOrThrow(Long id) throws NotFoundException {
        return produceOrderRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("[workOrderStateHelperError] produceOrder does not exist. produceOrderId: " + id));
    }
}
