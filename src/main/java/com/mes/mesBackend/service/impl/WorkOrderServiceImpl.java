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
import com.mes.mesBackend.helper.ProductionPerformanceHelper;
import com.mes.mesBackend.helper.WorkOrderStateHelper;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.ItemRepository;
import com.mes.mesBackend.repository.LotLogRepository;
import com.mes.mesBackend.repository.WorkOrderDetailRepository;
import com.mes.mesBackend.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.mes.mesBackend.entity.enumeration.OrderState.*;
import static com.mes.mesBackend.entity.enumeration.WorkProcessDivision.*;

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
    private final ItemRepository itemRepository;
    private final ProductionPerformanceHelper productionPerformanceHelper;
    private final LotLogRepository lotLogRepository;

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

    // 품목 조회 및 없으면 null
    private Item getItemOrNull(Long id) {
        return itemRepository.findByIdAndDeleteYnFalse(id).orElse(null);
    }

    // 작업지시 생성
    @Override
    public WorkOrderResponse createWorkOrder(Long produceOrderId, WorkOrderCreateRequest workOrderRequest) throws NotFoundException, BadRequestException {
        ProduceOrder produceOrder = produceOrderService.getProduceOrderOrThrow(produceOrderId);
        WorkProcess workProcess = workProcessService.getWorkProcessOrThrow(workOrderRequest.getWorkProcess());

        Item item = workProcess.getWorkProcessDivision().equals(PACKAGING) ? getItemOrNull(produceOrder.getContractItem().getItem().getId())
                : workOrderDetailRepo.findBomDetailHalfProductByBomMasterItemIdAndWorkProcessId(produceOrder.getContractItem().getItem().getId(), workProcess.getId(), null)
                .orElse(null);

        // 수주품목에 해당하는 품목이 BomMaster 에 없을경우 예외
        if (item == null) {
            throw new BadRequestException("수주품목에 해당하는 공정별 BOM 과 BOM 상세정보가 등록되지 않았습니다. 등록 후 다시 시도해주세요.");
        }

        WorkLine workLine = workLineService.getWorkLineOrThrow(workOrderRequest.getWorkLine());
        User user = workOrderRequest.getUser() != null ? userService.getUserOrThrow(workOrderRequest.getUser()) : null;

        // orderAmount 가 0 이면 제조오더 정보의(productOrder) 수주품목수량(contractItem.amount) 으로 저장
        // orderAmount: 사용자가 입력한 지시수량
        int orderAmount = workOrderRequest.getOrderAmount() != 0 ? workOrderRequest.getOrderAmount() : produceOrder.getContractItem().getAmount();

        // 입력받은 공정이 기존에 제조오더에 등록되어 있는 공정이면 예외(하나의 제조오더에 하나의 작업공정만 등록 가능)
        throwIfWorkProcessByProduceOrder(produceOrderId, workOrderRequest.getWorkProcess());

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

        Item item = workOrderResponse.getWorkProcessDivision().equals(PACKAGING) ? getItemOrNull(workOrderResponse.getProduceOrderItemId())
                : workOrderDetailRepo.findBomDetailHalfProductByBomMasterItemIdAndWorkProcessId(workOrderResponse.getProduceOrderItemId(), workOrderResponse.getWorkProcessId(), null)
                .orElse(null);

        workOrderResponse.setCostTime();        // 소요시간
        if (item != null) {
            workOrderResponse.setUnitCodeName(item.getUnit().getUnitCode());       // 공정에 해당하는 반제품 품목 단위
            workOrderResponse.setTestCategory(item.getTestCategory());             // 공정에 해당하는 반제품 품목 검사종류
            workOrderResponse.setTestType(item.getTestType());                     // 공정에 해당하는 반제품 품목 검사유형
        }

        return workOrderResponse;
    }

    // 작업지시 리스트 조회
    @Override
    public List<WorkOrderResponse> getWorkOrders(Long produceOrderId) throws NotFoundException {
        ProduceOrder produceOrder = produceOrderService.getProduceOrderOrThrow(produceOrderId);
        List<WorkOrderResponse> workOrderDetails = workOrderDetailRepo.findWorkOrderResponseByProduceOrderIdAndDeleteYnFalse(produceOrder.getId());

        for (WorkOrderResponse response : workOrderDetails) {
            Item item = response.getWorkProcessDivision().equals(PACKAGING) ? getItemOrNull(response.getProduceOrderItemId())
                    : workOrderDetailRepo.findBomDetailHalfProductByBomMasterItemIdAndWorkProcessId(response.getProduceOrderItemId(), response.getWorkProcessId(), null)
                    .orElse(null);

//            Item item = response.getWorkProcessDivision().equals(PACKAGING) ?
//                    produceOrder.getContractItem().getItem()
//                    : workOrderDetailRepo.findBomDetailHalfProductByBomMasterItemIdAndWorkProcessId(response.getProduceOrderItemId(), response.getWorkProcessId(), null)
//                    .orElse(null);
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
    ) throws NotFoundException, BadRequestException {
        WorkOrderDetail findWorkOrderDetail = getWorkOrderDetailOrThrow(workOrderId, produceOrderId);

        // 생산수량이 0 일경우 지시수량을 0으로 변경 할 수 없음.
        throwIfProductAmount(newWorkOrderRequest.getOrderAmount(), findWorkOrderDetail.getProductionAmount());

        WorkLine newWorkLine = workLineService.getWorkLineOrThrow(newWorkOrderRequest.getWorkLine());
        User newUser = newWorkOrderRequest.getUser() != null ? userService.getUserOrThrow(newWorkOrderRequest.getUser()) : null;

        WorkOrderDetail newWorkOrderDetail = mapper.toEntity(newWorkOrderRequest, WorkOrderDetail.class);

        // 작업지시의 상태값 구하기
        OrderState orderState = workOrderStateHelper.findOrderStateByOrderAmountAndProductAmount(newWorkOrderRequest.getOrderAmount(), findWorkOrderDetail.getProductionAmount(), findWorkOrderDetail.getWorkProcess().getWorkProcessDivision());

        findWorkOrderDetail.setOrderState(orderState);
        findWorkOrderDetail.update(newWorkOrderDetail, newWorkLine, newUser);
        workOrderDetailRepo.save(findWorkOrderDetail);

        if (findWorkOrderDetail.getOrderState().equals(COMPLETION)) {
            // 충진 공정일 경우 작업지시의 상태값이 완료로 변경 되면 같은 제조오더의 원료혼합 작업지시의 상태값도 완료로 변경한다.
            if (findWorkOrderDetail.getWorkProcess().getWorkProcessDivision().equals(FILLING)) {
                // 찾은 원료혼합 공정 작업지시 상태값 COMPLETION 으로 변경
                WorkOrderDetail materialMixingWorkOrder = workOrderDetailRepo.findWorkOrderIsFillingByProduceOrderId(findWorkOrderDetail.getProduceOrder().getId())
                        .orElse(null);
                if (materialMixingWorkOrder != null) {      // 충진공정의 변경된 지시상태랑 원료혼합의 지시상태랑 같게 변경
                    materialMixingWorkOrder.setOrderState(findWorkOrderDetail.getOrderState());
                    materialMixingWorkOrder.changeOrderStateDate(materialMixingWorkOrder.getOrderState());  // 완료날짜도 변경
                    workOrderDetailRepo.save(materialMixingWorkOrder);
                    workOrderStateHelper.updateOrderState(materialMixingWorkOrder.getId(), materialMixingWorkOrder.getOrderState());
                }
            }
        }

        // 작업지시 상태값, 제조오더 상태값 변경
        workOrderStateHelper.updateOrderState(findWorkOrderDetail.getId(), findWorkOrderDetail.getOrderState());

        return getWorkOrderResponseOrThrow(produceOrderId, workOrderId);
    }

    // 생산수량이 0 일경우 지시수량을 0으로 변경 할 수 없음.
    private void throwIfProductAmount(int newOrderAmount, int productAmount) throws BadRequestException {
        if (productAmount == 0) {
            if (newOrderAmount == 0) throw new BadRequestException("생산수량이 0 일 경우 지시수량을 0으로 변경 할 수 없습니다.");
        }
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

    // 입력받은 공정이 기존에 제조오더에 등록되어 있는 공정이면 예외(하나의 제조오더에 하나의 작업공정만 등록 가능)
    private void throwIfWorkProcessByProduceOrder(Long produceOrderId, Long workProcessId) throws BadRequestException {
        boolean existByWorkProcess = workOrderDetailRepo.existByWorkProcess(produceOrderId, workProcessId);
        if (existByWorkProcess) {
            throw new BadRequestException("해당 공정에 대한 작업지시가 이미 등록되어 있으므로, 삭제 후 등록 바랍니다.");
        }
    }
}
