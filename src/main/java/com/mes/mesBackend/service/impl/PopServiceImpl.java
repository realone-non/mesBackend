package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.LotMasterRequest;
import com.mes.mesBackend.dto.response.PopBomDetailItemResponse;
import com.mes.mesBackend.dto.response.PopEquipmentResponse;
import com.mes.mesBackend.dto.response.PopWorkOrderResponse;
import com.mes.mesBackend.dto.response.WorkProcessResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.LotHelper;
import com.mes.mesBackend.helper.LotLogHelper;
import com.mes.mesBackend.helper.ProductionPerformanceHelper;
import com.mes.mesBackend.helper.WorkOrderStateHelper;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.*;
import com.mes.mesBackend.service.LotMasterService;
import com.mes.mesBackend.service.PopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private final WorkOrderUserLogRepository workOrderUserLogRepo;
    private final EquipmentRepository equipmentRepository;
    private final LotHelper lotHelper;
    private final LotMasterRepository lotMasterRepo;
    private final LotConnectRepository lotConnectRepo;
    private final ModelMapper mapper;

    // 작업공정 전체 조회
    @Override
    public List<WorkProcessResponse> getPopWorkProcesses(Boolean recycleYn) {
        List<WorkProcess> workProcesses = workProcessRepository.findAllByDeleteYnFalse();
        List<WorkProcessResponse> workProcessResponses = mapper.toListResponses(workProcesses, WorkProcessResponse.class);
        List<WorkProcessResponse> responses = new ArrayList<>();
        if (recycleYn != null && recycleYn) {
            for (WorkProcessResponse res : workProcessResponses) {
                if (!res.isRecycleYn()) res = null;
                responses.add(res);
                responses.remove(null);
            }
            return responses;
        } else {
            return workProcessResponses;
        }
    }


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
                    .orElseThrow(() -> new NotFoundException("[데이터 오류] 공정에 맞는 반제품을 찾을 수 없습니다."));

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
    * workOrderDetailUserLog: 작업지시에 수량이 update 될 때 마다 insert
    * */
    @Override
    public Long createCreateWorkOrder(Long workOrderId, Long itemId, String userCode, int productAmount, Long equipmentId) throws NotFoundException, BadRequestException {
        WorkOrderDetail workOrder = getWorkOrderDetailOrThrow(workOrderId);
        WorkProcess workProcess = workOrder.getWorkProcess();
        int beforeProductionAmount = workOrder.getProductionAmount();

        // 작업지시의 상태가 COMPLETION 일 경우 더 이상 추가 할 수 없음. 추가하려면 workOrderDetail 의 productionAmount(지시수량) 을 늘려야함
        if (workOrder.getOrderState().equals(COMPLETION)) throw new BadRequestException("작업지시의 상태가 완료일 경우엔 더 이상 추가 할 수 없습니다.");
        // 작업수량 0 은 입력 할 수 없음.
        if (productAmount == 0) throw new BadRequestException("입력한 작업수량은 0 일 수 없습니다.");

        // workOderDetail: user 변경
        User user = getUserByUserCode(userCode);
        workOrder.setUser(user);

        // 기존 작업지시의 상태값이 SCHEDULE 일 때 ?
        // lotMaster: 생성
        // workOrderDetail: 상태값 변경 및 startDate 및 endDate 변경
        // productOrder: 상태값 변경
        // lotLog: 생성
        LotMaster lotMaster = new LotMaster();
        if (workOrder.getOrderState().equals(SCHEDULE)) {
            LotMasterRequest lotMasterRequest = new LotMasterRequest();
            Item item = getItemOrThrow(itemId);
            WareHouse wareHouse = lotMasterService.getLotMasterWareHouseOrThrow();

            // lotMaster 생성
            lotMasterRequest.setItem(item);                         // 품목
//            lotMasterRequest.setWorkProcessId(workProcess.getId()); // 작업공정
            lotMasterRequest.setWorkProcessDivision(workProcess.getWorkProcessDivision());
            lotMasterRequest.setWareHouse(wareHouse);               // 창고
            lotMasterRequest.setCreatedAmount(productAmount);       // 생성수량
            lotMasterRequest.setEnrollmentType(PRODUCTION);         // 등록유형
            lotMasterRequest.setEquipmentId(equipmentId);           // 설비유형
            lotMasterRequest.setDummyYn(true);

            lotHelper.createLotMaster(lotMasterRequest);

//            lotMaster.setItem(item);
//            lotMaster.setWorkProcess(workProcess);
//            lotMaster.setWareHouse(wareHouse);
//            lotMaster.setCreatedAmount(productAmount);
//            lotMaster.setEnrollmentType(PRODUCTION);
//            lotMaster.setLotNo("pop_test_원료혼합 공정");
//            lotMaster.setDummyYn(true);
//            lotMasterRepository.save(lotMaster);
//            lotMaster = lotMasterService.createLotMaster(lotMasterRequest);

            // workOrderDetail: 상태값 변경 및 startDate 및 endDate 변경
            // productOrder: 상태값 변경
            OrderState orderState =
                    workOrderStateHelper.findOrderStateByOrderAmountAndProductAmount(workOrder.getOrderAmount(), productAmount + beforeProductionAmount);
            workOrderStateHelper.updateOrderState(workOrderId, orderState);

            // lotLog 생성
            lotLogHelper.createLotLog(lotMaster.getId(), workOrderId, workProcess.getId());

            if (orderState.equals(COMPLETION)) {
                productionPerformanceHelper.updateOrInsertProductionPerformance(workOrderId, lotMaster.getId());  // productionPerformance: create 및 update
            }
        } else if (workOrder.getOrderState().equals(ONGOING)) {
            // 기존의 작업지시 상태값이 진행중이면?
            // lotMaster: lotLog 로 찾아서 값 update
            // workOrderDetail: ONGOING -> COMPLETION ? orderState update 및 endDate update
            // produceOrder: ONGOING -> COMPLETION ? orderState update

            // workOrderDetail id, workProcess id 로 LotLog 찾음
            lotMaster = lotLogHelper.getLotLogByWorkOrderDetailIdAndWorkProcessIdOrThrow(workOrder.getId(), workProcess.getId()).getLotMaster();
            lotMaster.setCreatedAmount(lotMaster.getCreatedAmount() + productAmount);   // lotMaster: 생성수량 update

            // workOrderDetail
            OrderState orderState =
                    workOrderStateHelper.findOrderStateByOrderAmountAndProductAmount(workOrder.getOrderAmount(), beforeProductionAmount + productAmount);

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
        workOrder.setProductionAmount(beforeProductionAmount + productAmount);  // productionAmount 변경
        workOrderDetailRepository.save(workOrder);

        // workOrderDetailUserLog: 작업지시에 수량이 update 될 때 마다 insert
        WorkOrderUserLog workOrderUserLog = new WorkOrderUserLog();
        workOrderUserLog.create(workOrder, user, productAmount);
        workOrderUserLogRepo.save(workOrderUserLog);

        return lotMaster.getId();
    }

    // 공정으로 공정에 해당하는 설비정보 가져오기 GET
    @Override
    public List<PopEquipmentResponse> getPopEquipments(Long workProcessId) throws NotFoundException {
        WorkProcess workProcess = getWorkProcessIdOrThrow(workProcessId);
        return equipmentRepository.findPopEquipmentResponseByWorkProcess(workProcess.getId());
    }

    // 해당 품목(반제품)에 대한 원자재, 부자재 정보 가져와야함
    @Override
    public List<PopBomDetailItemResponse> getPopBomDetailItems(Long lotMasterId) throws NotFoundException {
        LotMaster lotMaster = getLotMasterOrThrow(lotMasterId);
        Item bomMasterItem = lotMaster.getItem();
        // lotMaster 의 item 에 해당하는 bomDetail 의 item 정보 가져옴
        List<Item> bomDetailItems = workOrderDetailRepository.findBomDetailItemByBomMasterItem(bomMasterItem.getId());

        List<PopBomDetailItemResponse> popBomDetailItemResponses = new ArrayList<>();
        for (Item bomDetailItem : bomDetailItems) {
//            for (PopBomDetailItemResponse response : popBomDetailItemResponses) {
                PopBomDetailItemResponse response = new PopBomDetailItemResponse();
                response.setBomMasterItemId(bomMasterItem.getId());
                response.setBomMasterItemNo(bomMasterItem.getItemNo());
                response.setBomMasterItemName(bomMasterItem.getItemName());
                response.setBomMasterItemAmount(lotMaster.getCreatedAmount());

                response.setBomDetailItemId(bomDetailItem.getId());
                response.setBomDetailItemNo(bomDetailItem.getItemNo());
                response.setBomDetailItemName(bomDetailItem.getItemName());

                // 소진량
                List<LotConnect> lotConnects = lotConnectRepo.findLotConnectsByItemIdOfChildLotMasterEqAndDivisionExhaust(bomDetailItem.getId());
                for (LotConnect lotConnect : lotConnects) {
                    if (bomDetailItem.getId().equals(lotConnect.getChildLot().getItem().getId())) {
                        int allAmount = lotConnects.stream().mapToInt(LotConnect::getAmount).sum();
                        response.setBomDetailExhaustAmount(allAmount);
                    } else
                        response.setBomDetailExhaustAmount(0);
                }

                popBomDetailItemResponses.add(response);
//            }
        }
        return popBomDetailItemResponses;
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
    // lotMaster 단일 조회 및 예외
    private LotMaster getLotMasterOrThrow(Long id) throws NotFoundException {
        return lotMasterRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("lotMaster does not exist. input id:" + id));
    }
}
