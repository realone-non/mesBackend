package com.mes.mesBackend.helper.impl;

import com.mes.mesBackend.entity.ProduceOrder;
import com.mes.mesBackend.entity.WorkOrderDetail;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.WorkOrderStateHelper;
import com.mes.mesBackend.repository.ProduceOrderRepository;
import com.mes.mesBackend.repository.WorkOrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.mes.mesBackend.entity.enumeration.OrderState.*;

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
    * */
    @Override
    public OrderState findOrderStateByOrderAmountAndProductAmount(int orderAmount, int productAmount) {
        OrderState orderState;
        if (productAmount >= orderAmount) orderState = COMPLETION;
        else if (productAmount == 0) orderState = SCHEDULE;
        else  orderState = ONGOING;
        return orderState;
    }

    // 제조오더에 해당하는 가장 최근 등록된 작업지시의 orderState
    private OrderState getWorkOrderStateDesc(Long produceOrderId) {
        return workOrderDetailRepo.findOrderStatesByProduceOrderId(produceOrderId)
                .orElse(SCHEDULE);
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
