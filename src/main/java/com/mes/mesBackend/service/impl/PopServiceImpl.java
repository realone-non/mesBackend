package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.LotMasterRequest;
import com.mes.mesBackend.dto.response.PopWorkOrderResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.EnrollmentType;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.LotLogHelper;
import com.mes.mesBackend.helper.ProductionPerformanceHelper;
import com.mes.mesBackend.helper.WorkOrderStateHelper;
import com.mes.mesBackend.repository.ItemRepository;
import com.mes.mesBackend.repository.UserRepository;
import com.mes.mesBackend.repository.WorkOrderDetailRepository;
import com.mes.mesBackend.repository.WorkProcessRepository;
import com.mes.mesBackend.service.LotMasterService;
import com.mes.mesBackend.service.PopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.mes.mesBackend.entity.enumeration.EnrollmentType.PRODUCTION;
import static com.mes.mesBackend.entity.enumeration.OrderState.*;

@Service
@RequiredArgsConstructor
public class PopServiceImpl implements PopService {
    private final WorkOrderDetailRepository workOrderDetailRepository;
    private final WorkProcessRepository workProcessRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final LotMasterService lotMasterService;
    private final WorkOrderStateHelper workOrderStateHelper;
    private final ProductionPerformanceHelper productionPerformanceHelper;
    private final LotLogHelper lotLogHelper;


    // 작업지시 정보 리스트 api, 조건: 작업자, 작업공정
    @Override
    public List<PopWorkOrderResponse> getPopWorkOrders(Long workProcessId) throws NotFoundException {
        WorkProcess workProcess = getWorkProcessIdOrThrow(workProcessId);
        LocalDate now = LocalDate.now();
        // 조건: 작업예정일이 오늘, 작업공정
        List<PopWorkOrderResponse> todayWorkOrders = workOrderDetailRepository.findPopWorkOrderResponsesByCondition(workProcessId, now);

        for (PopWorkOrderResponse todayWorkOrder : todayWorkOrders) {
            // 제조오더의 품목정보에 해당하고, 검색공정과 같고, 반제품인 bomItemDetail 을 가져옴
            Item item = workOrderDetailRepository.findBomDetailByBomMasterItemIdAndWorkProcessId(todayWorkOrder.getProduceOrderItemId(), workProcess.getId())
                    .orElseThrow(() -> new NotFoundException("[데이터 오류] 공정에 맞는 반제품을 찾을 수 없습니다."));        // db 데이터 넣을때까지 보류

            todayWorkOrder.setItemId(item.getId());
            todayWorkOrder.setItemNo(item.getItemNo());
            todayWorkOrder.setItemName(item.getItemName());
            todayWorkOrder.setUnitCode(item.getUnit().getUnitCode());
        }
        return todayWorkOrders;
    }

    /*
    * workOrderDetail: 상태값 변경, 작업자 변경, 작업수량 변경, startDate 변경, endDate 변경 update
    * productOrder: 상태값 변경 update
    * lotMaster(작업지시의 상태값이 SCHEDULE -> ONGOING 으로 변경되는 시점에서 생성): 품목, 작업 공정, 창고, 생성수량, 등록유형(PRODUCTION)
    * lotLog(lotMaster 생성 후 생성)
    * productionPerformance(작업지시의 상태값이 ONGOING -> COMPLETION 으로 변경되는 시점에서 create or update):
    * */
    // TODO: 작업자 변경 log
    @Override
    public Long createCreateWorkOrder(Long workOrderId, Long itemId, String userCode, int productAmount) throws NotFoundException, BadRequestException {
        WorkOrderDetail workOrder = getWorkOrderDetailOrThrow(workOrderId);
        WorkProcess workProcess = workOrder.getWorkProcess();
        int beforeProductionAmount = workOrder.getProductionAmount();

        if (workOrder.getOrderState().equals(COMPLETION)) throw new BadRequestException("작업지시의 상태가 완료일 경우엔 더 이상 추가 할 수 없습니다.");
        if (productAmount == 0) throw new BadRequestException("입력한 작업수량은 0 일 수 없습니다.");

        // 작업완료를 여러번에 거쳐서 완료할 수 있음. ex) 10개 씩 세번
        // lotMaster 생성시점? workOrderDetail 의 orderState 가 SCHEDULE 이면서 1이상일때.
        // workOrderDetail 의 orderState 가 ONGOING 일 땐 lotMaster 를 찾아서 가져와야함(lotLog)

        // 기존 작업지시의 상태값이 SCHEDULE 일 때 ?
        // lotMaster: 생성
        // workOrderDetail: 상태값 변경 및 startDate 및 endDate 변경
        // productOrder: 상태값 변경
        // lotLog: 생성
        if (workOrder.getOrderState().equals(SCHEDULE)) {
            LotMasterRequest lotMasterRequest = new LotMasterRequest();
            Item item = getItemOrThrow(itemId);
            WareHouse wareHouse = lotMasterService.getLotMasterWareHouseOrThrow();

            // lotMaster 생성
            lotMasterRequest.setItem(item);                                           // 품목
            lotMasterRequest.setWorkProcessId(workProcess.getId());  // 작업공정
            lotMasterRequest.setWareHouse(wareHouse);            // 창고
            lotMasterRequest.setCreatedAmount(productAmount);    // 생성수량
            lotMasterRequest.setEnrollmentType(PRODUCTION);     // 등록유형
            LotMaster lotMaster = lotMasterService.createLotMaster(lotMasterRequest);

            // workOrderDetail: 상태값 변경 및 startDate 및 endDate 변경
            // productOrder: 상태값 변경
            // 지시상태 구하기
            OrderState orderState =
                    workOrderStateHelper.findOrderStateByOrderAmountAndProductAmount(workOrder.getOrderAmount(), productAmount + beforeProductionAmount);
            workOrderStateHelper.updateOrderState(workOrderId, orderState);

            // lotLog 생성
            lotLogHelper.createLotLog(lotMaster.getId(), workOrderId, workProcess.getId());
        } else if (workOrder.getOrderState().equals(ONGOING)) {
            // 기존의 작업지시 상태값이 진행중이면?
            // lotMaster: lotLog 로 찾아서 값 update
            // workOrderDetail: ONGOING -> COMPLETION ? orderState update 및 endDate update
            // produceOrder: ONGOING -> COMPLETION ? orderState update

            // workOrderDetail id, workProcess id 로 LotLog 찾음
            LotMaster lotMaster =
                    lotLogHelper.getLotLogByWorkOrderDetailIdAndWorkProcessIdOrThrow(workOrder.getId(), workProcess.getId()).getLotMaster();

            // lotMaster: 생성수량 update
            lotMaster.setCreatedAmount(lotMaster.getCreatedAmount() + productAmount);

            // workOrderDetail: productionAmount 변경
            workOrder.setProductionAmount(beforeProductionAmount + productAmount);
            OrderState orderState = workOrderStateHelper.findOrderStateByOrderAmountAndProductAmount(workOrder.getOrderAmount(), workOrder.getProductionAmount());

            // workOrderDetail: ONGOING -> COMPLETION ? orderState update 및 endDate update
            // produceOrder: ONGOING -> COMPLETION ? orderState update
            // productionPerformance: create 및 update
            if (orderState.equals(COMPLETION)) {
                // workOrderDetail: ONGOING -> COMPLETION ? orderState update 및 endDate update
                // produceOrder: ONGOING -> COMPLETION ? orderState update
                workOrderStateHelper.updateOrderState(workOrderId, orderState);
                productionPerformanceHelper.updateOrInsertProductionPerformance(workOrderId, lotMaster.getId());  // productionPerformance: create 및 update
            }
        }
        workOrderDetailRepository.save(workOrder);

        User user = getUserByUserCode(userCode);
        workOrder.setUser(user);


        return null;
    }



    // 작업공정 단일 조회 및 예외
    private WorkProcess getWorkProcessIdOrThrow(Long id) throws NotFoundException {
        return workProcessRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("workProcess does not exist. input id: " + id));
    }
    // 작업지시 단일 조회 및 예외
    private WorkOrderDetail getWorkOrderDetailOrThrow(Long id) throws NotFoundException {
        return workOrderDetailRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("workOrder does not exist. input id: " + id));
    }
    // 직원 단일 조회 및 예외
    private User getUserByUserCode(String userCode) throws NotFoundException {
        return userRepository.findByUserCode(userCode)
                .orElseThrow(() -> new NotFoundException("user does not exist. input userCode: " + userCode));
    }
    // 품목 단일 조회 및 예외
    private Item getItemOrThrow(Long id) throws NotFoundException {
        return itemRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("item does not exist. input id: " + id));
    }


    // 작업지시 상세 정보
    // 위에 해당 작업지시로 bomItemDetail 항목들 가져오기(품번, 품명, 계정, bom 수량, 예약수량)
//    @Override
//    public List<PopWorkOrderDetailResponse> getPopWorkOrderDetails(Long lotMasterId, Long workOrderId) throws NotFoundException {
//        Integer contractItemAmount = workOrderDetailRepository.findContractItemAmountByWorkOrderId(workOrderId);
//        LotMaster lotMaster = lotMasterService.getLotMasterOrThrow(lotMasterId);
//        List<PopWorkOrderDetailResponse> list = workOrderDetailRepository.findPopWorkOrderDetailResponsesByItemId(lotMaster.getItem().getId());
//        for (PopWorkOrderDetailResponse popWorkOrderDetailResponse : list) {
//            popWorkOrderDetailResponse.setReservationAmount(popWorkOrderDetailResponse.getBomAmount() * contractItemAmount);
//        }
//        return list;
//    }
}
