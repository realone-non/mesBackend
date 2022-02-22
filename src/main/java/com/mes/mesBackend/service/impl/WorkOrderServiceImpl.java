package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.WorkOrderCreateRequest;
import com.mes.mesBackend.dto.request.WorkOrderUpdateRequest;
import com.mes.mesBackend.dto.response.WorkOrderProduceOrderResponse;
import com.mes.mesBackend.dto.response.WorkOrderResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.NumberAutomatic;
import com.mes.mesBackend.helper.WorkOrderStateHelper;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.WorkOrderDetailRepository;
import com.mes.mesBackend.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.mes.mesBackend.entity.enumeration.OrderState.SCHEDULE;
import static com.mes.mesBackend.entity.enumeration.WorkProcessDivision.PACKAGING;

// 6-2. 작업지시 등록
@Service
@RequiredArgsConstructor
public class WorkOrderServiceImpl implements WorkOrderService {
    private final WorkOrderDetailRepository workOrderDetailRepo;
    private final WorkProcessService workProcessService;
    private final WorkLineService workLineService;
    private final UserService userService;
    private final ProduceOrderService produceOrderService;
    private final ModelMapper mapper;
    private final NumberAutomatic numberAutomatic;
    private final WorkOrderStateHelper workOrderStateHelper;

    // 제조오더 정보 리스트 조회
    // 검색조건: 품목그룹 id, 품명|품번, 수주번호, 제조오더번호, 착수예정일 fromDate~endDate, 지시상태
    @Override
    public List<WorkOrderProduceOrderResponse> getProduceOrders(
            Long itemGroupId,
            String itemNoAndName,
            String contractNo,
            String produceOrderNo,
            LocalDate fromDate,
            LocalDate toDate,
            OrderState orderState
    ) {
        return workOrderDetailRepo.findAllByCondition(itemGroupId, itemNoAndName, contractNo, produceOrderNo, fromDate, toDate, orderState);
    }

    // 작업지시 생성
    @Override
    public WorkOrderResponse createWorkOrder(Long produceOrderId, WorkOrderCreateRequest workOrderRequest) throws NotFoundException, BadRequestException {
        ProduceOrder produceOrder = produceOrderService.getProduceOrderOrThrow(produceOrderId);
        WorkProcess workProcess = workProcessService.getWorkProcessOrThrow(workOrderRequest.getWorkProcess());
        WorkLine workLine = workLineService.getWorkLineOrThrow(workOrderRequest.getWorkLine());
        User user = workOrderRequest.getUser() != null ? userService.getUserOrThrow(workOrderRequest.getUser()) : null;

        // orderAmount 가 0 이면 제조오더 정보의(productOrder) 수주품목수량(contractItem.amount) 으로 저장
        // orderAmount: 사용자가 입력한 지시수량
        int orderAmount = workOrderRequest.getOrderAmount() != 0 ? workOrderRequest.getOrderAmount() : produceOrder.getContractItem().getAmount();

        // 입력받은 공정이 기존에 제조오더에 등록되어 있는 공정이면 예외(하나의 제조오더에 하나의 작업공정만 등록 가능)
        throwIfWorkProcessByProduceOrder(produceOrderId, workOrderRequest.getWorkProcess());
        // 사용자가 입력한 지시수량이 수주품목의 수량보다 크면 예외
        // 지시수량을 수주 수량보다 더 크게 할 수 있음.
//        throwIfOrderAmountGreaterThanProduceOrderAmount(orderAmount, produceOrder.getContractItem().getAmount());
        // 사용자가 입력한 생산수량이 지시수량보다 크면 예외
        // 생상수량이 지시수량보다 클 수 있음.
//        throwIfProductionAmountGreaterThanOrderAmount(workOrderRequest.getProductionAmount(), orderAmount);

        WorkOrderDetail workOrderDetail = mapper.toEntity(workOrderRequest, WorkOrderDetail.class);
        String workOrderNo = numberAutomatic.createDateTimeNo();

        workOrderDetail.add(workProcess, workLine, user, produceOrder);
        workOrderDetail.setOrderNo(workOrderNo);
        workOrderDetail.setOrderAmount(orderAmount);
        workOrderDetailRepo.save(workOrderDetail);
        workOrderStateHelper.updateOrderState(workOrderDetail.getId(), SCHEDULE);

        return getWorkOrderResponseOrThrow(produceOrderId, workOrderDetail.getId());
    }

    // 작업지시 단일조회
    @Override
    public WorkOrderResponse getWorkOrderResponseOrThrow(Long produceOrderId, Long workOrderId) throws NotFoundException {
        WorkOrderResponse workOrderResponse = workOrderDetailRepo.findWorkOrderResponseByProduceOrderIdAndWorkOrderId(produceOrderId, workOrderId)
                .orElseThrow(() -> new NotFoundException("workOrderDetail does not exists. input id: " + workOrderId));

        Item bomDetailItem = workOrderDetailRepo.findBomDetailHalfProductByBomMasterItemIdAndWorkProcessId(workOrderResponse.getProduceOrderItemId(), workOrderResponse.getWorkProcessId(), null)
                .orElse(null);

        workOrderResponse.setCostTime();        // 소요시간
        if (bomDetailItem != null) {
            workOrderResponse.setUnitCodeName(bomDetailItem.getUnit().getUnitCode());       // 공정에 해당하는 반제품 품목 단위
            workOrderResponse.setTestCategory(bomDetailItem.getTestCategory());             // 공정에 해당하는 반제품 품목 검사종류
            workOrderResponse.setTestType(bomDetailItem.getTestType());                     // 공정에 해당하는 반제품 품목 검사유형
        }

        return workOrderResponse;
    }

    // 작업지시 리스트 조회
    @Override
    public List<WorkOrderResponse> getWorkOrders(Long produceOrderId) throws NotFoundException {
        ProduceOrder produceOrder = produceOrderService.getProduceOrderOrThrow(produceOrderId);
        List<WorkOrderResponse> workOrderDetails = workOrderDetailRepo.findWorkOrderResponseByProduceOrderIdAndDeleteYnFalse(produceOrder.getId());

        for (WorkOrderResponse response : workOrderDetails) {
            Item item = response.getWorkProcessDivision().equals(PACKAGING) ?
                    produceOrder.getContractItem().getItem()
                    : workOrderDetailRepo.findBomDetailHalfProductByBomMasterItemIdAndWorkProcessId(response.getProduceOrderItemId(), response.getWorkProcessId(), null)
                    .orElse(null);
            response.setCostTime();
            if (item != null) {
                response.setUnitCodeName(item.getUnit().getUnitCode());       // 공정에 해당하는 반제품 품목 단위
                response.setTestCategory(item.getTestCategory());             // 공정에 해당하는 반제품 품목 검사종류
                response.setTestType(item.getTestType());                     // 공정에 해당하는 반제품 품목 검사유형
            }
        }
        return workOrderDetails;
    }

    // 작업지시 수정
    /*
    * 작업공정 수정 불가
    * */
    @Override
    public WorkOrderResponse updateWorkOrder(
            Long produceOrderId,
            Long workOrderId,
            WorkOrderUpdateRequest newWorkOrderRequest
    ) throws NotFoundException {
        WorkOrderDetail findWorkOrderDetail = getWorkOrderDetailOrThrow(workOrderId, produceOrderId);

        WorkLine newWorkLine = workLineService.getWorkLineOrThrow(newWorkOrderRequest.getWorkLine());
        User newUser = newWorkOrderRequest.getUser() != null ? userService.getUserOrThrow(newWorkOrderRequest.getUser()) : null;
        ProduceOrder produceOrder = produceOrderService.getProduceOrderOrThrow(produceOrderId);

        // orderAmount 가 0 이면 제조오더 정보의(productOrder) 수주품목수량(contractItem.amount) 으로 저장
        // orderAmount: 사용자가 입력한 지시수량
        int orderAmount = newWorkOrderRequest.getOrderAmount() != 0 ? newWorkOrderRequest.getOrderAmount() : produceOrder.getContractItem().getAmount();

        if (orderAmount !=  findWorkOrderDetail.getOrderAmount()) {
            // 사용자가 입력한 지시수량이 수주품목의 수량보다 크면 예외
//            throwIfOrderAmountGreaterThanProduceOrderAmount(orderAmount, produceOrder.getContractItem().getAmount());
            // 사용자가 입력한 생산수량이 지시수량보다 크면 예외
//            throwIfProductionAmountGreaterThanOrderAmount(newWorkOrderRequest.getProductionAmount(), orderAmount);
        }

        newWorkOrderRequest.setOrderAmount(orderAmount);
        WorkOrderDetail newWorkOrderDetail = mapper.toEntity(newWorkOrderRequest, WorkOrderDetail.class);
        findWorkOrderDetail.update(newWorkOrderDetail, newWorkLine, newUser);
        workOrderDetailRepo.save(findWorkOrderDetail);
        workOrderStateHelper.updateOrderState(findWorkOrderDetail.getId(), findWorkOrderDetail.getOrderState());

        return getWorkOrderResponseOrThrow(produceOrderId, workOrderId);
    }

    // 작업지시 삭제
    /*
    * 진행중, 완료는 삭제 불가
    * */
    @Override
    public void deleteWorkOrder(Long produceOrderId, Long workOrderId) throws NotFoundException, BadRequestException {
        WorkOrderDetail workOrderDetail = getWorkOrderDetailOrThrow(workOrderId, produceOrderId);
        if (!workOrderDetail.getOrderState().equals(SCHEDULE)) {
            throw new BadRequestException("진행중이거나 완료가 된 작업지시는 삭제 할 수 없습니다.");
        }
            workOrderDetail.delete();
            workOrderDetailRepo.save(workOrderDetail);
    }


    // 작업지시 단일 조회 및 예외
    @Override
    public WorkOrderDetail getWorkOrderDetailOrThrow (Long id, Long produceOrderId) throws NotFoundException {
        ProduceOrder produceOrder = produceOrderService.getProduceOrderOrThrow(produceOrderId);
        return workOrderDetailRepo.findByIdAndProduceOrderAndDeleteYnFalse(id, produceOrder)
                .orElseThrow(() -> new NotFoundException("workOrderDetail does not exist. input id: " + id));
    }


//     produceOrder(제조오더): 제조오더에 해당하는 workOrderDetail(작업지시) 의 orderState 상태값 별로 제조오더의 상태값도 변경됨.
//    @Override
//    public void changeOrderStateOfProduceOrder(ProduceOrder produceOrder) {
//        OrderState orderState = workOrderDetailRepo.findOrderStatesByProduceOrderId(produceOrder.getId())
//                .orElse(SCHEDULE);
//        switch (orderState) {
//            case SCHEDULE: produceOrder.setOrderState(SCHEDULE);
//                break;
//            case ONGOING: produceOrder.setOrderState(ONGOING);
//                break;
//            case COMPLETION: produceOrder.setOrderState(COMPLETION);
//                break;
//        }
//    }

        // 수주품목의 수주품목수량(오더수량) 보다 여태 저장된 지시수량 + 입력받은 지시수량이 크면 예외
//    private void throwIfTotalOrderAmountGreaterThanAmountOfProduceOrder(Long produceOrderId, int orderAmount, int amountOfProduceOrder) throws BadRequestException {
//        List<Integer> orderAmounts = workOrderDetailRepo.findOrderAmountsByProduceOrderId(produceOrderId);
//        int orderAmountSum = orderAmounts.stream().mapToInt(Integer::intValue).sum();
//        if (orderAmountSum + orderAmount > amountOfProduceOrder) {
//            throw new BadRequestException("total orderAmount must not be greater than the amount of produceOrder. " +
//                    "input orderAmount: " + orderAmount + ", " +
//                    "total orderAmount: " + orderAmountSum + ", " +
//                    "amount of produceOrder: " + amountOfProduceOrder
//            );
//        }
//    }

    // 사용자가 입력한 지시수량이 수주품목의 수량보다 크면 예외
    private void throwIfOrderAmountGreaterThanProduceOrderAmount(int orderAmount, int orderAmountOfProduceOrder) throws BadRequestException {
        if (orderAmount > orderAmountOfProduceOrder) {
            throw new BadRequestException("input orderAmount must not be greater than amount of produceOrder." +
                    " input orderAmount: " + orderAmount + ", " +
                    "amount of produceOrder: " + orderAmountOfProduceOrder
            );
        }
    }

    // 사용자가 입력한 생산수량이 지시수량보다 크면 예외
    private void throwIfProductionAmountGreaterThanOrderAmount(int productionAmount, int orderAmount) throws BadRequestException {
        if (productionAmount > orderAmount) {
            throw new BadRequestException("input productionAmount must not be greater than orderAmount. " +
                    "input productionAmount: " + productionAmount + ", " +
                    "orderAmount: " + orderAmount
            );
        }
    }

    // 입력받은 공정이 기존에 제조오더에 등록되어 있는 공정이면 예외(하나의 제조오더에 하나의 작업공정만 등록 가능)
    private void throwIfWorkProcessByProduceOrder(Long produceOrderId, Long workProcessId) throws BadRequestException {
        boolean existByWorkProcess = workOrderDetailRepo.existByWorkProcess(produceOrderId, workProcessId);
        if (existByWorkProcess) {
            throw new BadRequestException("해당 공정에 대한 작업지시가 이미 등록되어 있으므로, 삭제 후 등록 바랍니다.");
        }
    }
}
