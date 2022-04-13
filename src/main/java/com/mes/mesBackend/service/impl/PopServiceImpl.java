package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.LotMasterRequest;
import com.mes.mesBackend.dto.response.*;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.*;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.AmountHelper;
import com.mes.mesBackend.helper.LotHelper;
import com.mes.mesBackend.helper.LotLogHelper;
import com.mes.mesBackend.helper.WorkOrderStateHelper;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.*;
import com.mes.mesBackend.service.LotMasterService;
import com.mes.mesBackend.service.PopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.mes.mesBackend.entity.enumeration.EnrollmentType.PRODUCTION;
import static com.mes.mesBackend.entity.enumeration.GoodsType.HALF_PRODUCT;
import static com.mes.mesBackend.entity.enumeration.ItemLogType.INPUT_AMOUNT;
import static com.mes.mesBackend.entity.enumeration.ItemLogType.STORE_AMOUNT;
import static com.mes.mesBackend.entity.enumeration.LotConnectDivision.EXHAUST;
import static com.mes.mesBackend.entity.enumeration.LotConnectDivision.FAMILY;
import static com.mes.mesBackend.entity.enumeration.LotMasterDivision.*;
import static com.mes.mesBackend.entity.enumeration.OrderState.*;
import static com.mes.mesBackend.entity.enumeration.ProcessStatus.MATERIAL_REGISTRATION;
import static com.mes.mesBackend.entity.enumeration.ProcessStatus.MIDDLE_TEST;
import static com.mes.mesBackend.entity.enumeration.WorkProcessDivision.*;

@Service
@RequiredArgsConstructor
public class PopServiceImpl implements PopService {
    private final WorkOrderDetailRepository workOrderDetailRepository;
    private final WorkProcessRepository workProcessRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final LotMasterService lotMasterService;
    private final WorkOrderStateHelper workOrderStateHelper;
//    private final ProductionPerformanceHelper productionPerformanceHelper;
    private final LotLogHelper lotLogHelper;
    private final WorkOrderUserLogRepository workOrderUserLogRepo;
    private final EquipmentRepository equipmentRepository;
    private final LotHelper lotHelper;
    private final LotMasterRepository lotMasterRepo;
    private final LotConnectRepository lotConnectRepo;
    private final ModelMapper mapper;
    private final AmountHelper amountHelper;
    private final BadItemRepository badItemRepo;
    private final WorkOrderBadItemRepository workOrderBadItemRepo;
    private final LotEquipmentConnectRepository lotEquipmentConnectRepo;
    private final LotLogRepository lotLogRepo;
    private final EquipmentBreakdownRepository equipmentBreakdownRepo;

    // 작업공정 전체 조회
    @Override
    public List<WorkProcessResponse> getPopWorkProcesses(Boolean recycleYn) {
        List<WorkProcess> workProcesses = workProcessRepository.findAllByDeleteYnFalseOrderByCreatedDateDesc()
                .stream().sorted(Comparator.comparing(WorkProcess::getOrders)).collect(Collectors.toList());
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

    // 작업지시 정보 리스트 api, 조건: 작업공정, 오늘
    @Override
    public List<PopWorkOrderResponse> getPopWorkOrders(WorkProcessDivision workProcessDivision) throws NotFoundException {
        Long workProcessId = lotLogHelper.getWorkProcessByDivisionOrThrow(workProcessDivision);
        WorkProcess workProcess = getWorkProcessIdOrThrow(workProcessId);
        LocalDate now = LocalDate.now();
        // 조건: 작업예정일이 오늘, 작업공정
        List<PopWorkOrderResponse> todayWorkOrders = workOrderDetailRepository.findPopWorkOrderResponsesByCondition(workProcessId, now);

        for (PopWorkOrderResponse todayWorkOrder : todayWorkOrders) {
            Item item = workProcessDivision.equals(PACKAGING) ?
                    workOrderDetailRepository.findPopWorkOrderItem(workProcessId, now, todayWorkOrder.getWorkOrderId()).orElseThrow(() -> new NotFoundException("[데이터 오류] 포장공정의 완제품을 찾을 수 없습니다."))
                    : workOrderDetailRepository.findBomDetailHalfProductByBomMasterItemIdAndWorkProcessId(todayWorkOrder.getProduceOrderItemId(), workProcess.getId(), null)
                            .orElseThrow(() -> new NotFoundException("[데이터 오류] 공정에 맞는 반제품 또는 완제품을 찾을 수 없습니다."));

            todayWorkOrder.setItemId(item.getId());
            todayWorkOrder.setItemNo(item.getItemNo());
            todayWorkOrder.setItemName(item.getItemName());
            todayWorkOrder.setUnitCode(item.getUnit().getUnitCode());
        }
        return todayWorkOrders;
    }

    // 작업지시 진행상태 정보 조회
    @Override
    public List<PopWorkOrderStates> getPopWorkOrderStates(Long workOrderId) throws NotFoundException {
        WorkOrderDetail workOrderDetail = getWorkOrderDetailOrThrow(workOrderId);
        LotLog lotLog = lotLogRepo.findByWorkOrderDetailId(workOrderDetail.getId())
                .orElseThrow(() -> new NotFoundException("[데이터오류] 작업지시에 해당하는 lotLog 를 찾을 수 없습니다."));
        LotMaster dummyLot = lotLog.getLotMaster();
        // dummyLot 로 lotEquipmentLot 조회
        return lotEquipmentConnectRepo.findPopWorkOrderStates(dummyLot.getId());
    }

    // 작업지시 진행상태 변경
    // 2022.03.03 추가
    // lotMaster 의 공정이 충진이고, 상태값이 MIDDLE_TEST 일 때 원료혼합에서 만든 반제품 로트의 stockAmount 를 0 으로 변경한다. (전체수량 소모)
    @Override
    public void updatePopWorkOrderState(Long lotMasterId, ProcessStatus processStatus) throws NotFoundException, BadRequestException {
        LotMaster equipmentLot = getLotMasterOrThrow(lotMasterId);
        LotEquipmentConnect lotEquipmentConnect = getLotEquipmentConnectByChildLotOrThrow(equipmentLot.getId());
        lotEquipmentConnect.setProcessStatus(processStatus);
        lotEquipmentConnectRepo.save(lotEquipmentConnect);

        if (equipmentLot.getWorkProcess().getWorkProcessDivision().equals(FILLING) && processStatus.equals(MIDDLE_TEST)) {
            LotLog lotLog = lotLogRepo.findByLotMasterIdAndWorkProcessDivision(lotEquipmentConnect.getParentLot().getId(), FILLING)
                    .orElseThrow(() -> new NotFoundException("[데이터오류] 입력받은 설비로트에 해당하는 제조오더에 원료혼합 공정에서 만들어진 반제품이 존재하지 않습니다."));
            ProduceOrder produceOrder = lotLog.getWorkOrderDetail().getProduceOrder();

            // 설비 고장이 아니면 진행
            boolean b = lotConnectRepo.existsByProduceOrderLotConnectIsError(produceOrder.getId(), equipmentLot.getId());
            if (!b) {
                LotConnect findLotConnect = lotConnectRepo.findByTodayProduceOrderAndEquipmentIdEqAndLotStockAmountOneLoe(produceOrder.getId(), equipmentLot.getEquipment().getId(), LocalDate.now())
                        .orElseThrow(() -> new BadRequestException("[데이터오류] 입력한 설비로트와 같은 제조오더의 원료혼합 공정에서 생성 된 반제품이 존재하지 않습니다."));
                LotMaster halfLot = findLotConnect.getChildLot();

                // lotConnect 소진으로 변경
                LotConnect lotConnect = new LotConnect();
                lotConnect.setParentLot(lotEquipmentConnect);
                lotConnect.setChildLot(halfLot);
                lotConnect.setDivision(EXHAUST);
                lotConnect.setErrorYn(false);
                lotConnect.setAmount(halfLot.getStockAmount());
                lotConnectRepo.save(lotConnect);

                // 원료혼합 반제품 lot 재고수량 모두 소진
                halfLot.setStockAmount(0);
                lotMasterRepo.save(halfLot);

                // 설비 생산 가능 여부 상태값 true 로 변경
                Equipment equipment = equipmentLot.getEquipment();
                equipment.setProduceYn(true);
                equipmentRepository.save(equipment);
            }
        }
    }

    /*
     * workOrderDetail: 상태값 변경, 작업자 변경, 작업수량 변경, startDate 변경, endDate 변경 update
     * productOrder: 상태값 변경 update
     * lotMaster(작업지시의 상태값이 SCHEDULE -> ONGOING 으로 변경되는 시점에서 생성): 품목, 작업 공정, 창고, 생성수량, 등록유형(PRODUCTION)
     *      - lotMaster 2개 생성 해야됨
     *              - 더미로트: 품목, 작업 공정, 창고, 생성수량, 등록유형(PRODUCTION), 더미여부 true
     *              - 설비로트: 품목, 작업 공정, 창고, 생성수량, 등록유형(PRODUCTION), 더미여부 true
     * lotEquipmentConnect: 위에 로트 2개 생성 후 생성 (parentLot: 더미로트, childLot: 설비로트)
     * lotLog(lotMaster 생성 후 생성): 더미로트로 생성
     * productionPerformance(작업지시의 상태값이 ONGOING -> COMPLETION 으로 변경되는 시점에서 create or update): 더미로트로 생성
     * workOrderDetailUserLog: 작업지시에 수량이 update 될 때 마다 insert, equipmentLotMaster: 설비로트
     * */
    @Override
    public Long createWorkOrder(
            Long workOrderId,
            Long itemId,
            String userCode,
            int productAmount,  // 생산수량
            int stockAmount,    // 양품수량
            int badItemAmount,  // 불량수량
            Long equipmentId,
            Long fillingEquipmentCode     // 원료혼합에서만 입력하는 충진공정 설비 id(생성되는 equipmentLot 에 넣어야함)
    ) throws NotFoundException, BadRequestException {
        WorkOrderDetail workOrder = getWorkOrderDetailOrThrow(workOrderId);
        WorkProcess workProcess = workOrder.getWorkProcess();
        int beforeProductionAmount = workOrder.getProductionAmount();
        Equipment equipment = getEquipmentOrThrow(equipmentId);
        BadItem badItem = new BadItem();

        // 공정에 해당하는 첫번째 불량유형
        if (badItemAmount != 0) {
            badItem = workOrderBadItemRepo.findByWorkOrderIdLimitOne(workProcess.getId())
                    .orElseThrow(() -> new NotFoundException("해당 공정에 등록된 불량유형이 존재하지 않습니다."));
        }

        // 원료혼합 공정에서 선택한 충진공정 설비 produceYn false 로 변경
        if (fillingEquipmentCode != null) {
            Equipment fillingEquipment = getEquipmentOrThrow(fillingEquipmentCode);

            // 입력한 설비가 produceYn 이 true 인지 체크(true: 생산 가능, false: 생산 불가능)
            if (!fillingEquipment.isProduceYn()) throw new BadRequestException("입력한 설비는 현재 사용 다른 제품 생산중이므로 사용이 불가능 합니다.");

            fillingEquipment.setProduceYn(false);
            equipmentRepository.save(fillingEquipment);
        }

        // 작업지시의 상태가 COMPLETION 일 경우 더 이상 추가 할 수 없음. 추가하려면 workOrderDetail 의 productionAmount(지시수량) 을 늘려야함
        // 원료혼합 공정은 제외
//        if (!workOrder.getWorkProcess().getWorkProcessDivision().equals(MATERIAL_MIXING)) {       <- 2022.04.01 로직 없앰
        throwIfWorkOrderStateIsCompletion(workOrder.getOrderState());
//        }
        // 작업수량이 0 이면 예외
        throwIfProductAmountIsNotZero(productAmount);
        // 생산수량 체크
        throwIfProductAmountCheck(stockAmount, badItemAmount, productAmount);

        // workOderDetail: user 변경
        User user = getUserByUserCode(userCode);
        workOrder.setUser(user);

        // 기존 작업지시의 상태값이 SCHEDULE 일 때 ?
        // dummyLotMaster: 생성
        // EquipmentLotMaster: 생성
        //        // lotEquipmentConnect: 위에 lot 2개 생성 후 생성, parentLot: dummyLot, childLot: equipmentLot
        // workOrderDetail: 상태값 변경 및 startDate 및 endDate 변경
        // productOrder: 상태값 변경
        // lotLog: 생성
        LotMaster dummyLot;
        LotMaster equipmentLot = new LotMaster();
        LotMasterRequest dummyLotRequest = new LotMasterRequest();
        LotMasterRequest equipmentLotRequest = new LotMasterRequest();
        LotEquipmentConnect lotEquipmentConnect = new LotEquipmentConnect();
        Item item = getItemOrThrow(itemId);
        WareHouse wareHouse = lotMasterService.getLotMasterWareHouseOrThrow();


        Long fillEquipmentId = workProcess.getWorkProcessDivision().equals(MATERIAL_MIXING) ? fillingEquipmentCode : null;

        // 작업지시의 공정이 충진일때 전 공정인 원료혼합에서 만든 반제품이 없으면 예외
        if (workProcess.getWorkProcessDivision().equals(FILLING)) {
            lotConnectRepo.findByTodayProduceOrderAndEquipmentIdEqAndLotStockAmountOneLoe(workOrder.getProduceOrder().getId(), equipment.getId(), LocalDate.now())
                    .orElseThrow(() -> new BadRequestException("입력한 설비로트와 같은 제조오더의 원료혼합 공정에서 생성 된 반제품이 존재하지 않습니다. " +
                            "원료혼합 공정에서 반제품 생성 한 다음 충진공정 설비 지정 후에 다시 시도해주세요."));
        }

        if (workOrder.getOrderState().equals(SCHEDULE)) {
            // dummyLot 생성: 품목, 창고, 생성수량, 등록유형, 설비유형, lot 생성 구분
            dummyLotRequest.putPopWorkOrder(    // 더미로트에 재고수량은 관리 안함
                    item, workProcess.getWorkProcessDivision(), wareHouse, productAmount, 0, badItemAmount, PRODUCTION, equipmentId, DUMMY_LOT, null
            );
            dummyLot = lotHelper.createLotMaster(dummyLotRequest);

            // equipmentLot 생성: 품목, 창고, 생성수량, 등록유형, 설비유형, lot 생성 구분
            equipmentLotRequest.putPopWorkOrder(
                    item, workProcess.getWorkProcessDivision(), wareHouse, productAmount, stockAmount, badItemAmount, PRODUCTION, equipmentId, EQUIPMENT_LOT, fillEquipmentId
            );
            equipmentLot = lotHelper.createLotMaster(equipmentLotRequest);

            // lotEquipmentConnect 생성:
            lotEquipmentConnect.create(dummyLot, equipmentLot, MATERIAL_REGISTRATION);
            lotEquipmentConnectRepo.save(lotEquipmentConnect);

            // 불량유형 아무거나로 생성 : 불량수량이 0 이 아닐 경우
            if (badItemAmount != 0) {
                WorkOrderBadItem workOrderBadItem = new WorkOrderBadItem();
                workOrderBadItem.popCreate(badItem, workOrder, equipmentLot, badItemAmount, EQUIPMENT_LOT);
                workOrderBadItemRepo.save(workOrderBadItem);
            }

            // workOrderDetail: 상태값 변경 및 startDate 및 endDate 변경
            // productOrder: 상태값 변경
            OrderState orderState =
                    workOrderStateHelper.findOrderStateByOrderAmountAndProductAmount(workOrder.getOrderAmount(), productAmount + beforeProductionAmount, workProcess.getWorkProcessDivision());
            workOrderStateHelper.updateOrderState(workOrderId, orderState);

            // lotLog 생성
            if (dummyLot.getLotMasterDivision().equals(DUMMY_LOT)) {
                lotLogHelper.createLotLog(dummyLot.getId(), workOrderId, workProcess.getId());
            }

            if (orderState.equals(COMPLETION)) {
//                productionPerformanceHelper.updateOrInsertProductionPerformance(workOrderId, dummyLot.getId());  // productionPerformance: create 및 update
//                if (workProcess.getWorkProcessDivision().equals(MATERIAL_MIXING)) {
//                    workOrder.setOrderState(ONGOING);
//                    workOrder.changeOrderStateDate(workOrder.getOrderState());
//                    workOrderDetailRepository.save(workOrder);
//                }
                // 충진공정 일 경우
                if (workProcess.getWorkProcessDivision().equals(FILLING)) {
                    WorkOrderDetail materialMixingWorkOrder = workOrderDetailRepository.findWorkOrderIsFillingByProduceOrderId(workOrder.getProduceOrder().getId())
                            .orElseThrow(() -> new BadRequestException("해당하는 제조오더에 대한 원료혼합 공정 작업지시가 존재하지 않습니다."));
                    materialMixingWorkOrder.setOrderState(workOrder.getOrderState());
                    materialMixingWorkOrder.changeOrderStateDate(materialMixingWorkOrder.getOrderState());
                    workOrderDetailRepository.save(materialMixingWorkOrder);
                }
            }
        } else if (workOrder.getOrderState().equals(ONGOING)) {
            // 기존의 작업지시 상태값이 진행중이면?
            // lotMaster:
            //          dummyLot: lotLog 로 찾아서 값 update
            //          equipmentLot: - 설비가 변경되었을땐 새로 생성 품목, 창고, 생성수량, 등록유형, 설비유형, lot 생성 구분
            //                        - 당일, 같은설비 일 경우엔 찾아서 update
            // lotEquipmentConnect: 생성
            // workOrderDetail: ONGOING -> COMPLETION ? orderState update 및 endDate update
            // produceOrder: ONGOING -> COMPLETION ? orderState update

            // workOrderDetail id, workProcess id 로 LotLog 찾음
            dummyLot = lotLogHelper.getLotLogByWorkOrderDetailIdAndWorkProcessIdOrThrow(workOrder.getId(), workProcess.getId()).getLotMaster();


            // 2022.03.07 (월)
            /*
            * [원료혼합]
            * 금일 기준으로 충진공정의 동일한 설비당 하나의 equipmentLot 생성 될 수 있음, (조건: stockAmount 0 또는 폐기 Lot 일때)
            * */
            if(workProcess.getWorkProcessDivision().equals(MATERIAL_MIXING)) {
                LotMaster materialMixingRealLot = lotMasterRepo.findByTodayAndWorkProcessDivisionEqAndInputEquipmentEq(LocalDate.now(), MATERIAL_MIXING, equipmentId)
                        .orElse(null);  // null 일때만 생성 가능

                if (materialMixingRealLot == null) {
                    // equipmentLot 생성
                    equipmentLotRequest.putPopWorkOrder(item, workProcess.getWorkProcessDivision(), wareHouse, productAmount, stockAmount, badItemAmount, PRODUCTION, equipmentId, EQUIPMENT_LOT, fillEquipmentId);
                    equipmentLot = lotHelper.createLotMaster(equipmentLotRequest);
                    lotMasterRepo.save(equipmentLot);

                    // lotEquipmentConnect 생성
                    lotEquipmentConnect.create(dummyLot, equipmentLot, MATERIAL_REGISTRATION);
                    lotEquipmentConnectRepo.save(lotEquipmentConnect);

                    // 불량유형 아무거나로 생성
                    if (badItemAmount != 0) {
                        WorkOrderBadItem workOrderBadItem = new WorkOrderBadItem();
                        workOrderBadItem.popCreate(badItem, workOrder, equipmentLot, badItemAmount, EQUIPMENT_LOT);
                        workOrderBadItemRepo.save(workOrderBadItem);
                    }
                } else {
                    throw new BadRequestException("해당 설비는 다른 원료혼합 반제품이 진행중이므로 선택할 수 없습니다. " +
                            "진행중인 반제품 LotNo: " + materialMixingRealLot.getLotNo() + ", " +
                            "진행중인 반제품 LotId: " + materialMixingRealLot.getId() + ", " +
                            "진행중인 충진공정 설비 명: " + materialMixingRealLot.getInputEquipment().getEquipmentName()
                    );
                }
            } else {
                // 작업수량 들어올때마다 equipmentLot 생성으로 로직변경 - 2022.03.02
                equipmentLotRequest.putPopWorkOrder(item, workProcess.getWorkProcessDivision(), wareHouse, productAmount, stockAmount, badItemAmount, PRODUCTION, equipmentId, EQUIPMENT_LOT, fillEquipmentId);
                equipmentLot = lotHelper.createLotMaster(equipmentLotRequest);
                lotMasterRepo.save(equipmentLot);

                // lotEquipmentConnect 생성
                lotEquipmentConnect.create(dummyLot, equipmentLot, MATERIAL_REGISTRATION);
                lotEquipmentConnectRepo.save(lotEquipmentConnect);

                // 불량유형 아무거나로 생성
                if (badItemAmount != 0) {
                    WorkOrderBadItem workOrderBadItem = new WorkOrderBadItem();
                    workOrderBadItem.popCreate(badItem, workOrder, equipmentLot, badItemAmount, EQUIPMENT_LOT);
                    workOrderBadItemRepo.save(workOrderBadItem);
                }
            }

            dummyLot.setCreatedAmount(dummyLot.getCreatedAmount() + productAmount);   // lotMaster: 생성수량 update
            dummyLot.setBadItemAmount(dummyLot.getBadItemAmount() + badItemAmount);   // lotMaster: 불량수량 update
            lotMasterRepo.save(dummyLot);

            // workOrderDetail
            OrderState orderState =
                    workOrderStateHelper.findOrderStateByOrderAmountAndProductAmount(workOrder.getOrderAmount(), beforeProductionAmount + productAmount, workProcess.getWorkProcessDivision());

            // workOrderDetail: ONGOING -> COMPLETION ? orderState update 및 endDate update
            // produceOrder: ONGOING -> COMPLETION ? orderState update
            // productionPerformance: create 및 update
            if (orderState.equals(COMPLETION)) {
                // workOrderDetail: ONGOING -> COMPLETION ? orderState update 및 endDate update
                // produceOrder: ONGOING -> COMPLETION ? orderState update
                workOrderStateHelper.updateOrderState(workOrderId, orderState);
//                productionPerformanceHelper.updateOrInsertProductionPerformance(workOrderId, dummyLot.getId());  // productionPerformance: create 및 update
//                if (workProcess.getWorkProcessDivision().equals(MATERIAL_MIXING)) {
//                    workOrder.setOrderState(ONGOING);
//                    workOrder.changeOrderStateDate(workOrder.getOrderState());
//                    workOrderDetailRepository.save(workOrder);
//                }
//                if (workProcess.getWorkProcessDivision().equals(FILLING)) {     // 완료된 작업공정이 충진일 경우 원료혼합 지시상태도 완료로 바꾼다.
//                    WorkOrderDetail materialMixingWorkOrder = workOrderDetailRepository.findWorkOrderIsFillingByProduceOrderId(workOrder.getProduceOrder().getId())
//                            .orElseThrow(() -> new BadRequestException("해당 작업지시로 생성 된 원료혼합 공정의 작업지시가 존재하지 않습니다."));
//                    materialMixingWorkOrder.setOrderState(COMPLETION);
//                    materialMixingWorkOrder.changeOrderStateDate(materialMixingWorkOrder.getOrderState());
//                    workOrderDetailRepository.save(workOrder);
//                    workOrderStateHelper.updateOrderState(materialMixingWorkOrder.getId(), materialMixingWorkOrder.getOrderState());
//                }
            }
        }


        workOrder.setProductionAmount(beforeProductionAmount + productAmount);  // productionAmount 변경
        workOrderDetailRepository.save(workOrder);

        // workOrderDetailUserLog: 작업지시에 수량이 update 될 때 마다 insert
        WorkOrderUserLog workOrderUserLog = new WorkOrderUserLog();

        System.out.println(equipmentLot.getId());
        workOrderUserLog.create(workOrder, user, productAmount, equipmentLot);
        workOrderUserLogRepo.save(workOrderUserLog);

        return equipmentLot.getId();
    }

    // 공정으로 공정에 해당하는 설비정보 가져오기 GET
    // 생산가능 true 만 조회
    @Override
    public List<PopEquipmentResponse> getPopEquipments(WorkProcessDivision workProcessDivision, Boolean produceYn) throws NotFoundException {
        Long workProcessId = lotLogHelper.getWorkProcessByDivisionOrThrow(workProcessDivision);
        WorkProcess workProcess = getWorkProcessIdOrThrow(workProcessId);
        return equipmentRepository.findPopEquipmentResponseByWorkProcess(workProcess.getId(), produceYn);
//                .stream().filter(PopEquipmentResponse::isProduceYn).collect(Collectors.toList());
    }

    // 해당 품목(반제품)에 대한 원자재, 부자재 정보 가져와야함
    @Override
    public List<PopBomDetailItemResponse> getPopBomDetailItems(Long lotMasterId) throws NotFoundException {
        LotMaster lotMaster = getLotMasterOrThrow(lotMasterId);
        Item bomMasterItem = lotMaster.getItem();
        WorkProcessDivision workProcessDivision = lotMaster.getWorkProcess().getWorkProcessDivision();

        List<PopBomDetailItemResponse> popBomDetailItemResponses = new ArrayList<>();

        List<Item> items = workProcessDivision.equals(PACKAGING) ?
                workOrderDetailRepository.findBomDetailItemByBomMasterItemWorkProcessPackaging(bomMasterItem.getId())
                : workOrderDetailRepository.findBomDetailItemByBomMasterItem(bomMasterItem.getId(), workProcessDivision);

        for (Item item : items) {
            PopBomDetailItemResponse response = new PopBomDetailItemResponse();
            response.setBomMasterItemId(bomMasterItem.getId());
            response.setBomMasterItemNo(bomMasterItem.getItemNo());
            response.setBomMasterItemName(bomMasterItem.getItemName());
            response.setBomMasterItemAmount(lotMaster.getCreatedAmount());

            response.setBomDetailItemId(item.getId());
            response.setBomDetailItemNo(item.getItemNo());
            response.setBomDetailItemName(item.getItemName());
            response.setBomDetailExhaustYn(item.getUnit().isExhaustYn());
            response.setBomDetailUnitCodeName(item.getUnit().getUnitCode());
            response.setGoodsType(item.getItemAccount().getGoodsType());

            // 소진량
            List<LotConnect> lotConnects = lotConnectRepo.findLotConnectsByItemIdOfChildLotMasterEqAndDivisionExhaust(item.getId(), lotMasterId);
            for (LotConnect lotConnect : lotConnects) {
                if (item.getId().equals(lotConnect.getChildLot().getItem().getId())) {
                    int allAmount = lotConnects.stream().mapToInt(LotConnect::getAmount).sum();
                    response.setBomDetailExhaustAmount(allAmount);
                } else
                    response.setBomDetailExhaustAmount(0);
            }
            popBomDetailItemResponses.add(response);
        }

        if (lotMaster.getWorkProcess().getWorkProcessDivision().equals(FILLING)) {
            return popBomDetailItemResponses.stream().filter(f -> !f.getGoodsType().equals(HALF_PRODUCT)).collect(Collectors.toList());
        } else
            return popBomDetailItemResponses;
    }

    // 원자재, 부자재에 해당되는 lotMaster 조회, stockAmount 1 이상
    @Override
    public List<PopBomDetailLotMasterResponse> getPopBomDetailLotMasters(Long lotMasterId, Long itemId, String lotNo) throws NotFoundException {
        // 해당 lot 에 등록된 사용정보는 보여주지 않음.
        LotMaster lotMaster = getLotMasterOrThrow(lotMasterId);
        List<PopBomDetailLotMasterResponse> responses = lotMasterRepo.findAllByItemIdAndLotNo(itemId, lotNo);
        // 부모 lotMaster 와 같은 자식 lotMasterId 모두 조회
        List<Long> childLotIds = lotConnectRepo.findChildLotIdByParentLotIdAndDivisionExhaust(lotMaster.getId());

        return responses.stream().filter(f -> !childLotIds.contains(f.getLotMasterId())).collect(Collectors.toList());  // 이미 등록되어 있는 사용정보는 제외
    }

    // 원부자재 lot 사용정보 등록
    // lotMasterConnect insert: 반제품 lotMaster, 사용한 lotMaster, 수량
    @Override
    public PopBomDetailLotMasterResponse createLotMasterExhaust(Long lotMasterId, Long itemId, Long exhaustLotMasterId, int exhaustAmount) throws NotFoundException, BadRequestException {
        LotMaster lotMaster = getLotMasterOrThrow(lotMasterId);                  // 반제품 lotMaster
        LotEquipmentConnect lotEquipmentConnect = getLotEquipmentConnectByChildLotOrThrow(lotMaster.getId());
        LotMaster exhaustLotMaster = getLotMasterOrThrow(exhaustLotMasterId);    // 사용한 lotMaster1
        int beforeLotMasterStockAmount = exhaustLotMaster.getStockAmount();      // 사용한 lotMaster 의 변경되기 전 재고수량
        Item item = getItemOrThrow(itemId);     // 원부자재

        // 기존에 등록되어 있는 사용정보는 등록 불가능, 재등록 요청 시 예외
        LotConnect orElse = lotConnectRepo.findByParentLotIdAndChildLotIdAndDivisionExhaust(lotMasterId, exhaustLotMasterId).orElse(null);
        if (orElse != null) throw new BadRequestException("해당 사용정보는 기존에 등록되어 있어 중복 생성이 불가능 합니다. 변동 사항은 수정이나 삭제를 해주세요.");

        // 입력한 사용 lotMaster 의 품목과 입력한 품목이 다르면 예외
       throwIfInputItemAndInputExhaustLotMasterEq(exhaustLotMaster.getItem(), item);
        // 소진유무가 false 인데 수량 0 이 들어오면 예외
        throwIfExhaustYnIsFalseCheck(exhaustLotMaster.getItem().getUnit().isExhaustYn(), exhaustAmount);
        // 소진수량이 재고수량 보다 클 경우 예외
        throwIfExhaustAmountGreaterThanStockAmount(exhaustLotMaster.getStockAmount(), exhaustAmount);

        // 사용 LOT 재고수량, 투입수량 변경
        exhaustLotMaster.setStockAmount(beforeLotMasterStockAmount - exhaustAmount);    // 사용한 lotMaster 재고수량 변경
        exhaustLotMaster.setInputAmount(exhaustLotMaster.getInputAmount() + exhaustAmount); // 소진된 수량만큼 투입수량 변경
        lotMasterRepo.save(exhaustLotMaster);

        // LotConnect 생성
        LotConnect lotConnect = new LotConnect();
        lotConnect.setParentLot(lotEquipmentConnect);    // 만들어진 lot
        lotConnect.setChildLot(exhaustLotMaster);       // 사용한 lot
        lotConnect.setAmount(exhaustAmount);            // 소진수량
        lotConnect.setDivision(EXHAUST);
        lotConnectRepo.save(lotConnect);

        // return response
        PopBomDetailLotMasterResponse response = new PopBomDetailLotMasterResponse();
        response.setLotMasterId(exhaustLotMaster.getId());                              // lot id
        response.setLotNo(exhaustLotMaster.getLotNo());                                 // lot no
        response.setStockAmount(exhaustLotMaster.getStockAmount());                     // 수량
        response.setUnitCodeName(exhaustLotMaster.getItem().getUnit().getUnitCode());   // 단위
        response.setExhaustYn(exhaustLotMaster.getItem().getUnit().isExhaustYn());      // 소진유무
        response.setExhaustAmount(lotConnect.getAmount());                              // 소진량

        // amountHelper
        amountHelper.amountUpdate(exhaustLotMaster.getItem().getId(), exhaustLotMaster.getWareHouse().getId(), null, INPUT_AMOUNT, exhaustAmount, false);

        return response;
    }

    // 입력한 사용 lotMaster 의 품목과 입력한 품목이 다르면 예외
    private void throwIfInputItemAndInputExhaustLotMasterEq(Item exhaustItem, Item inputItem) throws BadRequestException {
        if (!exhaustItem.equals(inputItem)) throw new BadRequestException("소진할 로트의 품목정보는 입력한 품목정보와 다릅니다. ");
    }

    // 원부자재 lot 사용정보 수정
    // 수량만 수정 가능
    @Override
    public PopBomDetailLotMasterResponse putLotMasterExhaust(Long lotMasterId, Long itemId, Long exhaustLotMasterId, int exhaustAmount) throws NotFoundException, BadRequestException {
        LotMaster lotMaster = getLotMasterOrThrow(lotMasterId);                  // 반제품 lotMaster
        LotEquipmentConnect lotEquipmentConnect = getLotEquipmentConnectByChildLotOrThrow(lotMaster.getId());
        LotMaster exhaustLotMaster = getLotMasterOrThrow(exhaustLotMasterId);    // 사용한 lotMaster
        Item item = getItemOrThrow(itemId);

        // 입력한 사용 lotMaster 의 품목과 입력한 품목이 다르면 예외
        throwIfInputItemAndInputExhaustLotMasterEq(exhaustLotMaster.getItem(), item);

        LotConnect lotConnect = getLotConnectExhaustByOrThrow(lotEquipmentConnect.getChildLot().getId(), exhaustLotMaster.getId());
        int beforeLotMasterStockAmount = exhaustLotMaster.getStockAmount() + lotConnect.getAmount();        // 수정되기 전 재고수량
        int beforeLotMasterInputAmount = exhaustLotMaster.getInputAmount() - lotConnect.getAmount();        // 수정되기 전 투입수량

        // 소진유무가 false 인데 수량 0 이 들어오면 예외
        throwIfExhaustYnIsFalseCheck(exhaustLotMaster.getItem().getUnit().isExhaustYn(), exhaustAmount);
        // 소진수량이 재고수량 보다 클 경우 예외
        throwIfExhaustAmountGreaterThanStockAmount(beforeLotMasterStockAmount, exhaustAmount);

        // 사용 lotMaster 재고수량, 투입수량 변경
        exhaustLotMaster.setStockAmount(beforeLotMasterStockAmount - exhaustAmount);    // 재고수량
        exhaustLotMaster.setInputAmount(beforeLotMasterInputAmount + exhaustAmount);    // 투입수량
        lotMasterRepo.save(exhaustLotMaster);

        // lotConnect 수량변경
        lotConnect.setAmount(exhaustAmount);
        lotConnectRepo.save(lotConnect);

        // return response
        PopBomDetailLotMasterResponse response = new PopBomDetailLotMasterResponse();
        response.setLotMasterId(exhaustLotMaster.getId());                              // lot id
        response.setLotNo(exhaustLotMaster.getLotNo());                                 // lot no
        response.setStockAmount(exhaustLotMaster.getStockAmount());                     // 수량
        response.setUnitCodeName(exhaustLotMaster.getItem().getUnit().getUnitCode());   // 단위
        response.setExhaustYn(exhaustLotMaster.getItem().getUnit().isExhaustYn());      // 소진유무
        response.setExhaustAmount(lotConnect.getAmount());                              // 소진량

        // amountHelper
        amountHelper.amountUpdate(exhaustLotMaster.getItem().getId(), exhaustLotMaster.getWareHouse().getId(), null, INPUT_AMOUNT, beforeLotMasterStockAmount - exhaustAmount, false);
        return response;
    }

    // 원부자재 lot 사용정보 삭제
    @Override
    public void deleteLotMasterExhaust(Long lotMasterId, Long itemId, Long exhaustLotMasterId) throws NotFoundException {
        LotMaster lotMaster = getLotMasterOrThrow(lotMasterId);                  // 반제품 lotMaster
        LotEquipmentConnect lotEquipmentConnect = getLotEquipmentConnectByChildLotOrThrow(lotMaster.getId());
        LotMaster exhaustLotMaster = getLotMasterOrThrow(exhaustLotMasterId);    // 사용한 lotMaster
        LotConnect lotConnect = getLotConnectExhaustByOrThrow(lotEquipmentConnect.getChildLot().getId(), exhaustLotMaster.getId());
        int exhaustAmount = lotConnect.getAmount(); // 소진수량

        // 사용 LOT 재고수량, 투입수량 변경
        exhaustLotMaster.setStockAmount(exhaustLotMaster.getStockAmount() + exhaustAmount); // 재고수량 변경
        exhaustLotMaster.setInputAmount(exhaustLotMaster.getInputAmount() - exhaustAmount); // 투입수량 변경
        lotMasterRepo.save(exhaustLotMaster);

        // LotConnect 삭제
        lotConnectRepo.deleteById(lotConnect.getId());

        // amountHelper
        amountHelper.amountUpdate(exhaustLotMaster.getItem().getId(), exhaustLotMaster.getWareHouse().getId(), null, STORE_AMOUNT, exhaustLotMaster.getStockAmount(), false);
    }

    // 원부자재 lot 사용정보 조회
    @Override
    public List<PopBomDetailLotMasterResponse> getLotMasterExhaust(Long lotMasterId, Long itemId) {
        return lotConnectRepo.findExhaustLotResponseByParentLotAndDivisionExhaust(lotMasterId, itemId);
    }

    // 중간검사 품목 정보 조회
    @Override
    public PopTestItemResponse getPopTestItem(Long lotMasterId) throws NotFoundException {
        LotMaster lotMaster = getLotMasterOrThrow(lotMasterId);
        PopTestItemResponse response = new PopTestItemResponse();
        int equipmentAllBadItemAmount = workOrderBadItemRepo.findBadItemAmountByEquipmentLotMaster(lotMasterId).stream().mapToInt(Integer::intValue).sum();
        response.put(lotMaster, equipmentAllBadItemAmount);
        return response;
    }

    // 공정에 해당하는 불량유형 조회
    @Override
    public List<PopBadItemTypeResponse> getPopTestBadItemTypes(Long lotMasterId) throws NotFoundException {
        LotMaster equipmentLot = getLotMasterOrThrow(lotMasterId);

        List<Long> enrollmentBadItemTypeId = workOrderBadItemRepo.findPopTestBadItemResponseByLotMasterId(equipmentLot.getId())
                .stream().map(PopTestBadItemResponse::getBadItemTypeId).collect(Collectors.toList());

        return workOrderBadItemRepo.findPopBadItemTypeByWorkProcessId(equipmentLot.getWorkProcess().getId())
                .stream().filter(f -> !enrollmentBadItemTypeId.contains(f.getBadItemTypeId())).collect(Collectors.toList());    // 이미 등록되어 있는 불량정보 제외
    }

    // 중간검사 등록된 불량 조회
    @Override
    public List<PopTestBadItemResponse> getPopBadItemEnrollments(Long lotMasterId) throws NotFoundException {
        LotMaster lotMaster = getLotMasterOrThrow(lotMasterId);
        return workOrderBadItemRepo.findPopTestBadItemResponseByLotMasterId(lotMaster.getId());
    }

    // 불량 등록
    @Override
    public PopTestBadItemResponse createPopBadItemEnrollment(
            Long lotMasterId,   // 설비로트
            Long badItemTypeId,
            int badItemAmount
    ) throws NotFoundException, BadRequestException {
        LotMaster equipmentLot = getLotMasterOrThrow(lotMasterId);
        LotEquipmentConnect lotEquipmentConnect = getLotEquipmentConnectByChildLotOrThrow(equipmentLot.getId());
        LotMaster dummyLot = lotEquipmentConnect.getParentLot();

        // lotMaster 에 해당하는 작업지시 조회
        WorkOrderDetail workOrderDetail = lotLogRepo.findByLotMasterIdAndWorkProcessId(
                dummyLot.getId(),
                equipmentLot.getWorkProcess().getId()
        ).orElseThrow(() -> new BadRequestException("[데이터오류] 더미로트와 작업공정에 해당하는 lotLog 가 없습니다.")).getWorkOrderDetail();
        BadItem badItem = getBadItemTypeOrThrow(badItemTypeId);

        // badItemAmount 가 equipmentLot 의 BadItemAmount 보다 크면 예외
//        throwIfBadItemAmountGreaterThanEquipmentLotBadItemAmount(badItemAmount, equipmentLot.getBadItemAmount(), equipmentLot.getId(), 0);
        int equipmentLotBadItemSum = workOrderBadItemRepo.findBadItemAmountByEquipmentLotMaster(equipmentLot.getId()).stream().mapToInt(Integer::intValue).sum();
        if (equipmentLotBadItemSum + badItemAmount > equipmentLot.getCreatedAmount()) {
            throw new BadRequestException("총 불량수량과 입력한 불량수량의 합계가 equipmentLot 의 생성수량을 초과할수 없습니다. " +
                    "생성수량: " + equipmentLot.getCreatedAmount() + ", " +
                    "현재 등록된 불량수량: " + equipmentLotBadItemSum + ", " +
                    "입력한 불량수량: " + badItemAmount + ", " +
                    "입력 가능불량수량: " + (equipmentLot.getCreatedAmount() - equipmentLotBadItemSum)
            );
        }
        // 하나의 lotMaster 에 중복 불량유형이 있으면 안됨 예외
        throwIfBadItemIdInLotMaster(lotMasterId, badItemTypeId);

        // workOrderBadItem 생성
        WorkOrderBadItem workOrderBadItem = new WorkOrderBadItem();
        workOrderBadItem.popCreate(badItem, workOrderDetail, equipmentLot, badItemAmount, EQUIPMENT_LOT);
        workOrderBadItemRepo.save(workOrderBadItem);

        // equipmentLot 불량수량, 양품수량 변경
        equipmentLot.setBadItemAmount(equipmentLotBadItemSum + badItemAmount);
        equipmentLot.setStockAmount(equipmentLot.getCreatedAmount() - (equipmentLotBadItemSum + badItemAmount));
        lotMasterRepo.save(equipmentLot);

        // dummyLot 불량수량, 양품수량 변경
        int dummyLotBadItemSum = workOrderBadItemRepo.findBadItemAmountByDummyLotMaster(dummyLot.getId()).stream().mapToInt(Integer::intValue).sum();
        dummyLot.setBadItemAmount(dummyLotBadItemSum);
        lotMasterRepo.save(dummyLot);

        PopTestBadItemResponse response = new PopTestBadItemResponse();
        return response.put(workOrderBadItem);
    }


    // 불량 수량 수정
    @Override
    public PopTestBadItemResponse putPopBadItemEnrollment(Long enrollmentBadItemId, int badItemAmount) throws NotFoundException, BadRequestException {
        WorkOrderBadItem workOrderBadItem = getWorkOrderBadItemOrThrow(enrollmentBadItemId, EQUIPMENT_LOT);

        int beforeAmount = workOrderBadItem.getBadItemAmount();
        LotMaster equipmentLot = workOrderBadItem.getLotMaster();
        LotEquipmentConnect lotEquipmentConnect = getLotEquipmentConnectByChildLotOrThrow(equipmentLot.getId());
        LotMaster dummyLot = lotEquipmentConnect.getParentLot();

        // badItemAmount 가 equipmentLot 의 BadItemAmount 보다 크면 예외
//        throwIfBadItemAmountGreaterThanEquipmentLotBadItemAmount(badItemAmount, equipmentLot.getBadItemAmount(), equipmentLot.getId(), beforeAmount);

        int equipmentLotBadItemSumCheck = workOrderBadItemRepo.findBadItemAmountByEquipmentLotMaster(equipmentLot.getId()).stream().mapToInt(Integer::intValue).sum();
        // 20 > (15 - 5) +7
        if ((equipmentLotBadItemSumCheck - beforeAmount) + badItemAmount > equipmentLot.getCreatedAmount()) {
            throw new BadRequestException("총 불량수량과 입력한 불량수량의 합계가 equipmentLot 의 생성수량을 초과할수 없습니다. " +
                    "생성수량: " + equipmentLot.getCreatedAmount() + ", " +
                    "현재 등록된 불량수량: " + (equipmentLotBadItemSumCheck - beforeAmount) + ", " +
                    "입력한 불량수량: " + badItemAmount + ", " +
                    "입력 가능한 불량수량: " + (equipmentLot.getCreatedAmount() - (equipmentLotBadItemSumCheck - workOrderBadItem.getBadItemAmount()))
            );
        }
        // 불량수량 수정
        workOrderBadItem.update(badItemAmount);
        workOrderBadItemRepo.save(workOrderBadItem);

        // equipmentLot 불량수량, 양품수량 변경
        int equipmentLotBadItemSum = workOrderBadItemRepo.findBadItemAmountByEquipmentLotMaster(equipmentLot.getId()).stream().mapToInt(Integer::intValue).sum();
        equipmentLot.setBadItemAmount(equipmentLotBadItemSum);
        equipmentLot.setStockAmount(equipmentLot.getCreatedAmount() - equipmentLotBadItemSum);
        lotMasterRepo.save(equipmentLot);

        // dummyLot 불량수량, 양품수량 변경
        int dummyLotBadItemSum = workOrderBadItemRepo.findBadItemAmountByDummyLotMaster(dummyLot.getId()).stream().mapToInt(Integer::intValue).sum();
        dummyLot.setBadItemAmount(dummyLotBadItemSum);
        lotMasterRepo.save(dummyLot);

        PopTestBadItemResponse response = new PopTestBadItemResponse();
        return response.put(workOrderBadItem);
    }

    // 불량 삭제
    @Override
    public void deletePopBadItemEnrollment(Long enrollmentBadItemId) throws NotFoundException {
        WorkOrderBadItem workOrderBadItem = getWorkOrderBadItemOrThrow(enrollmentBadItemId, EQUIPMENT_LOT);

        LotMaster equipmentLot = workOrderBadItem.getLotMaster();
        LotEquipmentConnect lotEquipmentConnect = getLotEquipmentConnectByChildLotOrThrow(equipmentLot.getId());
        LotMaster dummyLot = lotEquipmentConnect.getParentLot();

        // 불량 삭제
        workOrderBadItem.delete();
        workOrderBadItemRepo.save(workOrderBadItem);

        // equipmentLot 불량수량, 양품수량 변경
        int equipmentLotBadItemSum = workOrderBadItemRepo.findBadItemAmountByEquipmentLotMaster(equipmentLot.getId()).stream().mapToInt(Integer::intValue).sum();
        equipmentLot.setBadItemAmount(equipmentLotBadItemSum);
        equipmentLot.setStockAmount(equipmentLot.getCreatedAmount() - equipmentLotBadItemSum);
        lotMasterRepo.save(equipmentLot);

        // dummyLot 불량수량, 양품수량 변경
        int dummyLotBadItemSum = workOrderBadItemRepo.findBadItemAmountByDummyLotMaster(dummyLot.getId()).stream().mapToInt(Integer::intValue).sum();
        dummyLot.setBadItemAmount(dummyLotBadItemSum);
        lotMasterRepo.save(dummyLot);
    }

    // 분할 lot 조회
    @Override
    public List<PopLotMasterResponse> getPopLotMasters(Long lotMasterId) throws NotFoundException {
        LotMaster equipmentLot = getLotMasterOrThrow(lotMasterId);
        return lotConnectRepo.findPopLotMasterResponseByEquipmentLotId(equipmentLot.getId());
    }

    // 분할 lot 생성
    @Override
    public PopLotMasterResponse createPopLotMasters(Long lotMasterId, int amount) throws NotFoundException, BadRequestException {
        LotMaster equipmentLot = getLotMasterOrThrow(lotMasterId);
        LotEquipmentConnect lotEquipmentConnect = getLotEquipmentConnectByChildLotOrThrow(equipmentLot.getId());

        // amount 가 equipmentLot 의 stockAmount 보다 크면 예외
        throwIfAmountGreaterThanLotMasterStockAmount(amount, equipmentLot.getStockAmount());

        LotMasterRequest realLotRequest = new LotMasterRequest();
        LotMaster realLot;

        realLotRequest.putPopWorkOrder(
                equipmentLot.getItem(),
                equipmentLot.getWorkProcess().getWorkProcessDivision(),
                equipmentLot.getWareHouse(),
                amount,
                amount,
                0,
                equipmentLot.getEnrollmentType(),
                equipmentLot.getEquipment().getId(),
                REAL_LOT,
                null
        );
        realLot = lotHelper.createLotMaster(realLotRequest);
        // 분할 된 lot 와 부모로트 생성
        LotConnect lotConnect = new LotConnect();
        lotConnect.create(lotEquipmentConnect, realLot, amount, FAMILY);
        lotConnectRepo.save(lotConnect);

        // equipmentLot 의 stockAmount 변경
        equipmentLot.setStockAmount(equipmentLot.getStockAmount() - amount);
        lotMasterRepo.save(equipmentLot);

        PopLotMasterResponse popLotMasterResponse = new PopLotMasterResponse();
        return popLotMasterResponse.put(realLot);
    }

    // 분할 lot 수정
    @Override
    public PopLotMasterResponse putPopLotMasters(Long lotMasterId, int amount) throws NotFoundException, BadRequestException {
        LotMaster realLot = getLotMasterOrThrow(lotMasterId);
        LotConnect lotConnect = getLotConnectFamilyByOrTrow(realLot.getId());
        LotMaster equipmentLot = getLotEquipmentConnectByChildLotOrThrow(lotConnect.getParentLot().getChildLot().getId()).getChildLot();
        int beforeAmount = lotConnect.getAmount();

        // amount 가 equipmentLot 의 stockAmount + beforeAmount 보다 클 수 없음
        throwIfAmountGreaterThanLotMasterStockAmount(amount, equipmentLot.getStockAmount() + beforeAmount);

        // equipmentLot stockAmount 변경
        equipmentLot.setStockAmount((equipmentLot.getStockAmount() + beforeAmount) - amount);
        lotMasterRepo.save(equipmentLot);

        // realLot stockAmount 변경, createdAmount 변경
        realLot.setStockAmount(amount);
        realLot.setCreatedAmount(amount);
        lotMasterRepo.save(realLot);

        lotConnect.setAmount(amount);
        lotConnectRepo.save(lotConnect);

        PopLotMasterResponse response = new PopLotMasterResponse();
        return response.put(realLot);
    }

    // 분할 lot 삭제
    @Override
    public void deletePopLotMasters(Long lotMasterId) throws NotFoundException {
        LotMaster realLot = getLotMasterOrThrow(lotMasterId);
        LotConnect lotConnect = getLotConnectFamilyByOrTrow(realLot.getId());
        LotEquipmentConnect lotEquipmentConnect = getLotEquipmentConnectByChildLotOrThrow(lotConnect.getParentLot().getChildLot().getId());
        LotMaster equipmentLot = lotEquipmentConnect.getChildLot();
        int beforeAmount = lotConnect.getAmount();

        // equipmentLot stockAmount 변경
        equipmentLot.setStockAmount(equipmentLot.getStockAmount() + beforeAmount);
        lotMasterRepo.save(equipmentLot);

        // realLot 삭제
        realLot.delete();
        lotMasterRepo.save(realLot);

        // lotConnect 삭제
        lotConnectRepo.deleteById(lotConnect.getId());
    }

    // 충진공정 설비 선택
    // realLot, equipmentLot 에 inputEquipment 저장
    @Override
    public void putFillingEquipmentOfRealLot(Long lotMasterId, Long equipmentId) throws NotFoundException, BadRequestException {
        LotMaster realLot = getLotMasterOrThrow(lotMasterId);
        Equipment fillingEquipment = getEquipmentOrThrow(equipmentId);

        // 입력한 설비가 충진공정의 설비가 맞는지 체크
        if (!fillingEquipment.getWorkProcess().getWorkProcessDivision().equals(FILLING))
            throw new BadRequestException("입력한 설비가 충진공정의 설비에 해당되지 않습니다.");

        // 입력한 설비가 produceYn 이 true 인지 체크(true: 생산 가능, false: 생산 불가능)
//        if (!fillingEquipment.isProduceYn())
//            throw new BadRequestException("입력한 설비는 현재 사용 다른 제품 생산중이므로 사용이 불가능 합니다.");

        // realLot 에 inputEquipment 저장
        realLot.setInputEquipment(fillingEquipment);
        lotMasterRepo.save(realLot);

        // 설비 생산불가능 상태로 변경
//        fillingEquipment.setProduceYn(false);
//        equipmentRepository.save(fillingEquipment);
    }

    // 충진공정 설비 고장등록 api
    @Override
    public void createFillingEquipmentError(
            Long workOrderId,   // 충진공정 작업지시 id
            Long lotMasterId,   // 충진공정 설비 lot id
            BreakReason breakReason     // 고장이유
    ) throws NotFoundException {
        LotMaster equipmentLot = getLotMasterOrThrow(lotMasterId);
        WorkOrderDetail workOrder = getWorkOrderDetailOrThrow(workOrderId);
        LotEquipmentConnect lotEquipmentConnect = getLotEquipmentConnectByChildLotOrThrow(equipmentLot.getId());
        ProduceOrder produceOrder = workOrder.getProduceOrder();

        LotConnect findLotConnect =
                lotConnectRepo.findByTodayProduceOrderAndEquipmentIdEqAndLotStockAmountOneLoe(produceOrder.getId(), equipmentLot.getEquipment().getId(), LocalDate.now())
                        .orElseThrow(() -> new NotFoundException("[데이터 오류] 입력한 설비로트와 같은 제조오더의 원료혼합 공정에서 생성 된 반제품이 존재하지 않습니다."));
        LotMaster materialMixingLot = findLotConnect.getChildLot();   // 같은 제조오더의 원료혼합 공정에서 당일에 만들어진 realLot

        // lotConnect: 고장난 설비에 대한 반제품 소모 등록
        LotConnect beforeEquipmentLotConnect = new LotConnect();
        beforeEquipmentLotConnect.setAmount(materialMixingLot.getStockAmount());
        beforeEquipmentLotConnect.setErrorYn(true);
        beforeEquipmentLotConnect.setDivision(EXHAUST);
        beforeEquipmentLotConnect.setParentLot(lotEquipmentConnect);
        beforeEquipmentLotConnect.setChildLot(materialMixingLot);
        lotConnectRepo.save(beforeEquipmentLotConnect);

        // lotMaster: 폐기 여부 true 로 변경
        materialMixingLot.setStockAmount(0);
        materialMixingLot.setExhaustYn(true);
        lotMasterRepo.save(materialMixingLot);

        // equipment: 사용설비 생산가능 여부 true 변경
        Equipment equipment = equipmentLot.getEquipment();
        equipment.setProduceYn(true);
        equipmentRepository.save(equipment);

        // equipmentBreakdown: 설비 고장내역 등록
        EquipmentBreakdown equipmentBreakdown = new EquipmentBreakdown();
        equipmentBreakdown.setBreakDownDate(LocalDate.now());
        equipmentBreakdown.setEquipment(equipmentLot.getEquipment());
        equipmentBreakdown.setReportDate(LocalDateTime.now());
        equipmentBreakdown.setBreakReason(breakReason);
        equipmentBreakdown.setVisibleYn(true);
        equipmentBreakdownRepo.save(equipmentBreakdown);
    }

    // 설비 단일 조회 및 예외
    private Equipment getEquipmentOrThrow(Long id) throws NotFoundException {
        return equipmentRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("equipment does not exist. input id: " + id));
    }

    // 분할 amount 는 equipmentLot 의 stockAmount 보다 많을 수 없음.
    private void throwIfAmountGreaterThanLotMasterStockAmount(int inputAmount, int lotStockAmount) throws BadRequestException {
        if (inputAmount > lotStockAmount)
            throw new BadRequestException("입력한 LOT 생성 수량은 양품수량 보다 많을 수 없습니다. 가능수량: " + lotStockAmount);
    }

    // 불량등록 단일 조회 및 예외
    private WorkOrderBadItem getWorkOrderBadItemOrThrow(Long id, LotMasterDivision division) throws NotFoundException {
        return workOrderBadItemRepo.findByIdAndDeleteYnFalseAndDivision(id, division)
                .orElseThrow(() -> new NotFoundException("해당하는 불량을 찾을 수 없습니다."));
    }

    // lotConnect 단일 조회 및 예외
    private LotConnect getLotConnectExhaustByOrThrow(Long lotMasterId, Long exhaustLotMasterId) throws NotFoundException {
        return lotConnectRepo.findByParentLotIdAndChildLotIdAndDivisionExhaust(lotMasterId, exhaustLotMasterId)
                .orElseThrow(() -> new NotFoundException("해당 정보로 등록 된 원부자재 lot 사용정보가 없습니다."));
    }

    // 분할 lotConnect 로트 단일 조회 및 예외
    private LotConnect getLotConnectFamilyByOrTrow(Long realLotId) throws NotFoundException {
        return lotConnectRepo.findByChildLotIdAndDivisionFamily(realLotId)
                .orElseThrow(() -> new NotFoundException("입력한 분할로트에 대한 정보가 없습니다."));
    }

    // 소진유무가 false 인데 수량 0 이 들어오면 예외
    private void throwIfExhaustYnIsFalseCheck(boolean exhaustYn, int exhaustAmount) throws BadRequestException {
        if (!exhaustYn && exhaustAmount == 0) throw new BadRequestException("소진유무에 해당하지 않는 품목 이므로, 소진수량 0 을 입력할 수 없습니다.");
    }
    // 소진수량이 재고수량 보다 클 경우 예외
    private void throwIfExhaustAmountGreaterThanStockAmount(int stockAmount, int exhaustAmount) throws BadRequestException {
        if (exhaustAmount > stockAmount) throw new BadRequestException("소진수량이 재고수량 보다 클 수 없습니다. ");
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

    // 불량항목 단일 조회 및 예외
    private BadItem getBadItemTypeOrThrow(Long id) throws NotFoundException {
        return badItemRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("badItemType does not exist. input id: " + id));
    }

    // 불량수량 등록 체크
    private void throwIfBadItemAmountGreaterThanEquipmentLotBadItemAmount(int inputBadItemAmount, int equipmentLotBadItemAmount, Long equipmentLotId, int beforeAmount) throws BadRequestException {
        int equipmentAllBadItemAmount = workOrderBadItemRepo.findBadItemAmountByEquipmentLotMaster(equipmentLotId).stream().mapToInt(Integer::intValue).sum() - beforeAmount;
        int possibleBadItemAmount = equipmentLotBadItemAmount - equipmentAllBadItemAmount;
        if (possibleBadItemAmount < inputBadItemAmount) {
            throw new BadRequestException("불량입력 가능 수량을 초과했습니다. " +
                    "작업완료 시 입력한 불량수량: " + equipmentLotBadItemAmount + ", " +
                    "등록되어 있는 불량수량: " + equipmentAllBadItemAmount + ", " +
                    "현재 입력한 불량수량: " + inputBadItemAmount + ", " +
                    "등록 가능한 불량수량: " + possibleBadItemAmount
            );
        }
    }

    // 입력받은 lotMaster 는 같은 불량유형이 존재하면 안됨.
    private void throwIfBadItemIdInLotMaster(Long lotMasterId, Long badItemTypeId) throws BadRequestException {
        List<Long> findBadItemIdByLotMasterId = workOrderBadItemRepo.findBadItemIdByLotMasterId(lotMasterId);
        boolean badItemIdAnyMatchByLotMaster = findBadItemIdByLotMasterId.stream().anyMatch(id -> id.equals(badItemTypeId));
        if (badItemIdAnyMatchByLotMaster) throw new BadRequestException("하나의 로트는 같은 불량유형을 두개이상 등록 할 수 없음.");
    }

    // lotEquipmentConnect 단일 조회 및 예외
    private LotEquipmentConnect getLotEquipmentConnectByChildLotOrThrow(Long childLotId) throws NotFoundException {
        return lotEquipmentConnectRepo.findByChildId(childLotId)
                .orElseThrow(() -> new NotFoundException("해당하는 설비 lot 가 존재하지 않습니다."));
    }

    // 작업지시의 상태가 COMPLETION 일 경우 더 이상 추가 할 수 없음. 추가하려면 workOrderDetail 의 productionAmount(지시수량) 을 늘려야함
    private void throwIfWorkOrderStateIsCompletion(OrderState orderState) throws BadRequestException {
        if (orderState.equals(COMPLETION)) throw new BadRequestException("작업지시의 상태가 완료일 경우엔 더 이상 추가 할 수 없습니다.");
    }

    // 작업수량이 0 이면 예외
    private void throwIfProductAmountIsNotZero(int productAmount) throws BadRequestException {
        if (productAmount == 0) throw new BadRequestException("입력한 작업수량은 0 일 수 없습니다.");
    }

    // 생산수량 체크
    private void throwIfProductAmountCheck(int stockAmount, int badItemAmount, int productAmount) throws BadRequestException {
        if ((stockAmount + badItemAmount) != productAmount) {
            throw new BadRequestException("입력한 양품수량과 불량수량을 합한 수량이 입력한 생산수량과 같지 않습니다.");
        }
    }
}
