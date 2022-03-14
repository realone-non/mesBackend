package com.mes.mesBackend.helper;

import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.exception.NotFoundException;

// 작업지시 orderState 변경
public interface WorkOrderStateHelper {
    // 작업지시 orderState 변경
    /*
    * - orderState 별로 workOrderDetail 의 scheduleDate, startDate, endDate 값 update
    * - 제조오더에 해당하는 젤 마지막 등록 작업지시의 orderState 값으로 제조오더 orderState 값 update
    * */
    void updateOrderState(Long workOrderDetailId, OrderState orderState) throws NotFoundException;
    // 작업수량, 지시수량으로 상태값 구하기
    OrderState findOrderStateByOrderAmountAndProductAmount(int orderAmount, int productAmount);
    // 제조오더에 해당되는 작업지시의 모든 지시상태
    OrderState getWorkOrderStateDesc(Long produceOrderId);
}
