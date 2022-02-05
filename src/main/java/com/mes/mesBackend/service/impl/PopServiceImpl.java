package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.LotMasterRequest;
import com.mes.mesBackend.dto.response.*;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.LotMasterDivision;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.*;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.*;
import com.mes.mesBackend.service.LotMasterService;
import com.mes.mesBackend.service.PopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mes.mesBackend.entity.enumeration.EnrollmentType.PRODUCTION;
import static com.mes.mesBackend.entity.enumeration.ItemLogType.INPUT_AMOUNT;
import static com.mes.mesBackend.entity.enumeration.ItemLogType.STORE_AMOUNT;
import static com.mes.mesBackend.entity.enumeration.LotConnectDivision.EXHAUST;
import static com.mes.mesBackend.entity.enumeration.LotMasterDivision.DUMMY_LOT;
import static com.mes.mesBackend.entity.enumeration.LotMasterDivision.EQUIPMENT_LOT;
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
    private final LotLogRepository lotLogRepository;
    private final AmountHelper amountHelper;
    private final BadItemRepository badItemRepo;
    private final WorkOrderBadItemRepository workOrderBadItemRepo;
    private final LotEquipmentConnectRepository lotEquipmentConnectRepo;

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
    public List<PopWorkOrderResponse> getPopWorkOrders(WorkProcessDivision workProcessDivision) throws NotFoundException {
        Long workProcessId = lotLogHelper.getWorkProcessByDivisionOrThrow(workProcessDivision);
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

    // 작업지시의 상태가 COMPLETION 일 경우 더 이상 추가 할 수 없음. 추가하려면 workOrderDetail 의 productionAmount(지시수량) 을 늘려야함
    private void throwIfWorkOrderStateIsCompletion(OrderState orderState) throws BadRequestException {
        if (orderState.equals(COMPLETION)) throw new BadRequestException("작업지시의 상태가 완료일 경우엔 더 이상 추가 할 수 없습니다.");
    }

    // 작업수량이 0 이면 예외
    private void throwIfProductAmountIsNotZero(int productAmount) throws BadRequestException {
        if (productAmount == 0) throw new BadRequestException("입력한 작업수량은 0 일 수 없습니다.");
    }

    // ========================================= TODO: 여기서부터 수정해야됨 ~~~~~~~ -=========================================
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
    public Long createCreateWorkOrder(
            Long workOrderId,
            Long itemId,
            String userCode,
            int productAmount,
            Long equipmentId
    ) throws NotFoundException, BadRequestException {
        WorkOrderDetail workOrder = getWorkOrderDetailOrThrow(workOrderId);
        WorkProcess workProcess = workOrder.getWorkProcess();
        int beforeProductionAmount = workOrder.getProductionAmount();

        // 작업지시의 상태가 COMPLETION 일 경우 더 이상 추가 할 수 없음. 추가하려면 workOrderDetail 의 productionAmount(지시수량) 을 늘려야함
        throwIfWorkOrderStateIsCompletion(workOrder.getOrderState());
        // 작업수량이 0 이면 예외
        throwIfProductAmountIsNotZero(productAmount);

        // workOderDetail: user 변경
        User user = getUserByUserCode(userCode);
        workOrder.setUser(user);

        // 기존 작업지시의 상태값이 SCHEDULE 일 때 ?
        // dummyLotMaster: 생성
        // EquipmentLotMaster: 생성
        // lotEquipmentConnect: 위에 lot 2개 생성 후 생성, parentLot: dummyLot, childLot: equipmentLot
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

        if (workOrder.getOrderState().equals(SCHEDULE)) {

            // dummyLot 생성: 품목, 창고, 생성수량, 등록유형, 설비유형, lot 생성 구분
            dummyLotRequest.putPopWorkOrder(
                    item, workProcess.getWorkProcessDivision(), wareHouse, productAmount, PRODUCTION, equipmentId, DUMMY_LOT
            );
            dummyLot = lotHelper.createLotMaster(dummyLotRequest);

            // equipmentLot 생성: 품목, 창고, 생성수량, 등록유형, 설비유형, lot 생성 구분
            equipmentLotRequest.putPopWorkOrder(
                    item, workProcess.getWorkProcessDivision(), wareHouse, productAmount, PRODUCTION, equipmentId, EQUIPMENT_LOT
            );
            equipmentLot = lotHelper.createLotMaster(equipmentLotRequest);

            // lotEquipmentConnect 생성:
            lotEquipmentConnect.create(dummyLot, equipmentLot, null);
            lotEquipmentConnectRepo.save(lotEquipmentConnect);

            // workOrderDetail: 상태값 변경 및 startDate 및 endDate 변경
            // productOrder: 상태값 변경
            OrderState orderState =
                    workOrderStateHelper.findOrderStateByOrderAmountAndProductAmount(workOrder.getOrderAmount(), productAmount + beforeProductionAmount);
            workOrderStateHelper.updateOrderState(workOrderId, orderState);

            // lotLog 생성
            lotLogHelper.createLotLog(dummyLot.getId(), workOrderId, workProcess.getId());

            if (orderState.equals(COMPLETION)) {
                productionPerformanceHelper.updateOrInsertProductionPerformance(workOrderId, dummyLot.getId());  // productionPerformance: create 및 update
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

            // 오늘날짜, 같은 설비 기준으로 equipmentLot 조회해서 없으면 생성, 있으면 update
            LotEquipmentConnect equipmentConnect = lotEquipmentConnectRepo.findByTodayAndEquipmentId(equipmentId, LocalDate.now()).orElse(null);

            // 없으면 update
            if (equipmentConnect == null) {
                equipmentLotRequest.putPopWorkOrder(item, workProcess.getWorkProcessDivision(), wareHouse, productAmount, PRODUCTION, equipmentId, EQUIPMENT_LOT);
                equipmentLot = lotHelper.createLotMaster(equipmentLotRequest);
                lotMasterRepo.save(equipmentLot);
                lotEquipmentConnect.create(dummyLot, equipmentLot, null);
                lotEquipmentConnectRepo.save(lotEquipmentConnect);
            } else {
                // 있으면 update
                equipmentLot = equipmentConnect.getChildLot();
                equipmentLot.setCreatedAmount(equipmentLot.getCreatedAmount() + productAmount);
            }

            dummyLot.setCreatedAmount(dummyLot.getCreatedAmount() + productAmount);   // lotMaster: 생성수량 update
            lotMasterRepo.save(dummyLot);

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
                productionPerformanceHelper.updateOrInsertProductionPerformance(workOrderId, dummyLot.getId());  // productionPerformance: create 및 update
            }
        }
        workOrder.setProductionAmount(beforeProductionAmount + productAmount);  // productionAmount 변경
        workOrderDetailRepository.save(workOrder);

        // workOrderDetailUserLog: 작업지시에 수량이 update 될 때 마다 insert
        WorkOrderUserLog workOrderUserLog = new WorkOrderUserLog();
        workOrderUserLog.create(workOrder, user, productAmount, equipmentLot);
        workOrderUserLogRepo.save(workOrderUserLog);

        return equipmentLot.getId();
    }

    // 공정으로 공정에 해당하는 설비정보 가져오기 GET
    @Override
    public List<PopEquipmentResponse> getPopEquipments(WorkProcessDivision workProcessDivision) throws NotFoundException {
        Long workProcessId = lotLogHelper.getWorkProcessByDivisionOrThrow(workProcessDivision);
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
            PopBomDetailItemResponse response = new PopBomDetailItemResponse();
            response.setBomMasterItemId(bomMasterItem.getId());
            response.setBomMasterItemNo(bomMasterItem.getItemNo());
            response.setBomMasterItemName(bomMasterItem.getItemName());
            response.setBomMasterItemAmount(lotMaster.getCreatedAmount());

            response.setBomDetailItemId(bomDetailItem.getId());
            response.setBomDetailItemNo(bomDetailItem.getItemNo());
            response.setBomDetailItemName(bomDetailItem.getItemName());
            response.setBomDetailExhaustYn(bomDetailItem.getUnit().isExhaustYn());
            response.setBomDetailUnitCodeName(bomDetailItem.getUnit().getUnitCode());

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
        }
        return popBomDetailItemResponses;
    }

    // 원자재, 부자재에 해당되는 lotMaster 조회, stockAmount 1 이상
    // TODO: 여기까지 했음.
    @Override
    public List<PopBomDetailLotMasterResponse> getPopBomDetailLotMasters(Long lotMasterId, Long itemId, String lotNo) throws NotFoundException {
//        // 해당 lot 에 등록된 사용정보는 보여주지 않음.
//        LotMaster lotMaster = getLotMasterOrThrow(lotMasterId);
////        List<PopBomDetailLotMasterResponse> responses = lotMasterRepo.findAllByItemIdAndLotNo(itemId, lotNo);
////        // 부모 lotMaster 와 같은 자식 lotMasterId 모두 조회
////        List<Long> childLotIds = lotConnectRepo.findChildLotIdByParentLotIdAndDivisionExhaust(lotMaster.getId());
////
////        return responses.stream().filter(f -> !childLotIds.contains(f.getLotMasterId())).collect(Collectors.toList());  // 이미 등록되어 있는 사용정보는 제외
        return null;
    }

    // 원부자재 lot 사용정보 등록
    /*
    * - lotMasterConnect insert: 반제품 lotMaster, 사용한 lotMaster, 수량
    * */
    @Override
    public PopBomDetailLotMasterResponse createLotMasterExhaust(Long lotMasterId, Long itemId, Long exhaustLotMasterId, int exhaustAmount) throws NotFoundException, BadRequestException {
//        LotMaster lotMaster = getLotMasterOrThrow(lotMasterId);                  // 반제품 lotMaster
//        LotMaster exhaustLotMaster = getLotMasterOrThrow(exhaustLotMasterId);    // 사용한 lotMaster1
//        int beforeLotMasterStockAmount = exhaustLotMaster.getStockAmount();      // 사용한 lotMaster 의 변경되기 전 재고수량
//        Item item = getItemOrThrow(itemId);     // 원부자재
//
//        // 기존에 등록되어 있는 사용정보는 등록 불가능, 재등록 요청 시 예외
//        LotConnect orElse = lotConnectRepo.findByParentLotIdAndChildLotIdAndDivisionExhaust(lotMasterId, exhaustLotMasterId).orElse(null);
//        if (orElse != null) throw new BadRequestException("해당 사용정보는 기존에 등록되어 있어 중복 생성이 불가능 합니다. 변동 사항은 수정이나 삭제를 해주세요.");
//
//        // 입력한 사용 lotMaster 의 품목과 입력한 품목이 다르면 예외
//       throwIfInputItemAndInputExhaustLotMasterEq(exhaustLotMaster.getItem(), item);
//
//        throwIfExhaustYnIsFalseCheck(exhaustLotMaster.getItem().getUnit().isExhaustYn(), exhaustAmount); // 소진유무가 false 인데 수량 0 이 들어오면 예외
//        throwIfExhaustAmountGreaterThanStockAmount(exhaustLotMaster.getStockAmount(), exhaustAmount);    // 소진수량이 재고수량 보다 클 경우 예외
//
//        exhaustLotMaster.setStockAmount(beforeLotMasterStockAmount - exhaustAmount);    // 사용한 lotMaster 재고수량 변경
//        lotMasterRepo.save(exhaustLotMaster);
//
//        LotConnect lotConnect = new LotConnect();
//        lotConnect.setParentLot(lotMaster);             // 만들어진 lot
//        lotConnect.setChildLot(exhaustLotMaster);       // 사용한 lot
//        lotConnect.setAmount(exhaustAmount);            // 소진수량
//        lotConnect.setDivision(EXHAUST);
//        lotConnectRepo.save(lotConnect);
//
//        PopBomDetailLotMasterResponse response = new PopBomDetailLotMasterResponse();
//        response.setLotMasterId(exhaustLotMaster.getId());                              // lot id
//        response.setLotNo(exhaustLotMaster.getLotNo());                                 // lot no
//        response.setStockAmount(exhaustLotMaster.getStockAmount());                     // 수량
//        response.setUnitCodeName(exhaustLotMaster.getItem().getUnit().getUnitCode());   // 단위
//        response.setExhaustYn(exhaustLotMaster.getItem().getUnit().isExhaustYn());      // 소진유무
//        response.setExhaustAmount(lotConnect.getAmount());                              // 소진량
//
//        amountHelper.amountUpdate(exhaustLotMaster.getItem().getId(), exhaustLotMaster.getWareHouse().getId(), null, INPUT_AMOUNT, exhaustAmount, false);
//
//        return response;
        return null;
    }

    // 입력한 사용 lotMaster 의 품목과 입력한 품목이 다르면 예외
    private void throwIfInputItemAndInputExhaustLotMasterEq(Item exhaustItem, Item inputItem) throws BadRequestException {
        if (!exhaustItem.equals(inputItem)) throw new BadRequestException("소진할 로트의 품목정보는 입력한 품목정보와 다릅니다. ");
    }

    // 원부자재 lot 사용정보 수정
    // 수량만 수정 가능
    @Override
    public PopBomDetailLotMasterResponse putLotMasterExhaust(Long lotMasterId, Long itemId, Long exhaustLotMasterId, int exhaustAmount) throws NotFoundException, BadRequestException {
//        LotMaster lotMaster = getLotMasterOrThrow(lotMasterId);                  // 반제품 lotMaster
//        LotMaster exhaustLotMaster = getLotMasterOrThrow(exhaustLotMasterId);    // 사용한 lotMaster
//        Item item = getItemOrThrow(itemId);
//
//        // 입력한 사용 lotMaster 의 품목과 입력한 품목이 다르면 예외
//        throwIfInputItemAndInputExhaustLotMasterEq(exhaustLotMaster.getItem(), item);
//
//        LotConnect lotConnect = getLotConnectByOrThrow(lotMaster.getId(), exhaustLotMaster.getId());
//        int beforeLotMasterStockAmount = exhaustLotMaster.getStockAmount() + lotConnect.getAmount();        // 수정되기 전 수량
//
//        throwIfExhaustYnIsFalseCheck(exhaustLotMaster.getItem().getUnit().isExhaustYn(), exhaustAmount);  // 소진유무가 false 인데 수량 0 이 들어오면 예외
//        throwIfExhaustAmountGreaterThanStockAmount(beforeLotMasterStockAmount, exhaustAmount);      // 소진수량이 재고수량 보다 클 경우 예외
//
//        // 사용한 lotMaster 수량 변경
//        exhaustLotMaster.setStockAmount(beforeLotMasterStockAmount - exhaustAmount);
//        lotMasterRepo.save(exhaustLotMaster);
//        // lotConnect 수량변경
//        lotConnect.setAmount(exhaustAmount);
//        lotConnectRepo.save(lotConnect);
//
//        PopBomDetailLotMasterResponse response = new PopBomDetailLotMasterResponse();
//        response.setLotMasterId(exhaustLotMaster.getId());                              // lot id
//        response.setLotNo(exhaustLotMaster.getLotNo());                                 // lot no
//        response.setStockAmount(exhaustLotMaster.getStockAmount());                     // 수량
//        response.setUnitCodeName(exhaustLotMaster.getItem().getUnit().getUnitCode());   // 단위
//        response.setExhaustYn(exhaustLotMaster.getItem().getUnit().isExhaustYn());      // 소진유무
//        response.setExhaustAmount(lotConnect.getAmount());                              // 소진량
//
//        amountHelper.amountUpdate(exhaustLotMaster.getItem().getId(), exhaustLotMaster.getWareHouse().getId(), null, INPUT_AMOUNT, beforeLotMasterStockAmount - exhaustAmount, false);
//        return response;
        return null;
    }

    // 원부자재 lot 사용정보 삭제
    @Override
    public void deleteLotMasterExhaust(Long lotMasterId, Long itemId, Long exhaustLotMasterId) throws NotFoundException {

//        LotMaster lotMaster = getLotMasterOrThrow(lotMasterId);                  // 반제품 lotMaster
//        LotMaster exhaustLotMaster = getLotMasterOrThrow(exhaustLotMasterId);    // 사용한 lotMaster
//        LotConnect lotConnect = getLotConnectByOrThrow(lotMaster.getId(), exhaustLotMaster.getId());
//
//        exhaustLotMaster.setStockAmount(exhaustLotMaster.getStockAmount() + lotConnect.getAmount());
//
//        lotMasterRepo.save(exhaustLotMaster);
//        lotConnectRepo.deleteById(lotConnect.getId());
//
//        amountHelper.amountUpdate(exhaustLotMaster.getItem().getId(), exhaustLotMaster.getWareHouse().getId(), null, STORE_AMOUNT, exhaustLotMaster.getStockAmount(), false);
    }

    // 원부자재 lot 사용정보 조회
    @Override
    public List<PopBomDetailLotMasterResponse> getLotMasterExhaust(Long lotMasterId, Long itemId) {
        return null;
//        return lotConnectRepo.findExhaustLotResponseByParentLotAndDivisionExhaust(lotMasterId, itemId);
    }

    // 중간검사 품목 정보 조회
    @Override
    public PopTestItemResponse getPopTestItem(Long lotMasterId) throws NotFoundException {
        LotMaster lotMaster = getLotMasterOrThrow(lotMasterId);
        PopTestItemResponse response = new PopTestItemResponse();
        response.put(lotMaster);
        return response;
    }

    // 공정에 해당하는 불량유형 조회
    @Override
    public List<PopBadItemTypeResponse> getPopTestBadItemTypes(WorkProcessDivision workProcessDivision) throws NotFoundException {
        Long workProcessId = lotLogHelper.getWorkProcessByDivisionOrThrow(workProcessDivision);
        return workOrderBadItemRepo.findPopBadItemTypeByWorkProcessId(workProcessId);
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
            Long lotMasterId,
            WorkProcessDivision workProcessDivision,
            Long badItemTypeId,
            int badItemAmount
    ) throws NotFoundException, BadRequestException {
        LotMaster lotMaster = getLotMasterOrThrow(lotMasterId);
        Long workProcessId = lotLogHelper.getWorkProcessByDivisionOrThrow(workProcessDivision);
        BadItem badItem = getBadItemTypeOrThrow(badItemTypeId);

        // 불량 수량이 lotMaster 의 createdAmount 보다 크면 예외
        throwIfBadItemAmountGreaterThanCreatedAmountLotMaster(badItemAmount, lotMaster.getCreatedAmount());
        // 하나의 lotMaster 에 중복 불량유형이 있으면 안됨 예외
        throwIfBadItemIdInLotMaster(lotMasterId, badItemTypeId);

        return null;

    }

    // 불량 수량 수정
    @Override
    public PopTestBadItemResponse putPopBadItemEnrollment(Long enrollmentBadItemId, int badItemAmount) {
        return null;
    }
    // 불량 삭제
    @Override
    public void deletePopBadItemEnrollment(Long enrollmentBadItemId) {

    }
    // 분할 lot 조회
    @Override
    public List<PopLotMasterResponse> getPopLotMasters(Long lotMasterId) {
        return null;
    }
    // 분할 lot 생성
    @Override
    public PopLotMasterResponse createPopLotMasters(Long lotMasterId, int amount) {
        return null;
    }
    // 분할 lot 수정
    @Override
    public PopLotMasterResponse putPopLotMasters(Long lotMasterId, int amount) {
        return null;
    }
    // 분할 lot 삭제
    @Override
    public void deletePopLotMasters(Long lotMasterId) {

    }

    // lotConnect 단일 조회 및 예외
//    private LotConnect getLotConnectByOrThrow(Long lotMasterId, Long exhaustLotMasterId) throws NotFoundException {
//        return lotConnectRepo.findByParentLotIdAndChildLotIdAndDivisionExhaust(lotMasterId, exhaustLotMasterId)
//                .orElseThrow(() -> new NotFoundException("해당 정보로 등록 된 원부자재 lot 사용정보가 없습니다."));
//    }

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

    // 입력받은 badItemAmount 가 해당 lotMaster 의 생성수량보다 크면 안됨
    private void throwIfBadItemAmountGreaterThanCreatedAmountLotMaster(int badItemAmount, int createdAmount) throws BadRequestException {
        if (badItemAmount > createdAmount) {
            throw new BadRequestException("입력한 불량수량이 로트의 생성수량보다 크면 안됩니다.");
        }
    }

    // 입력받은 lotMaster 는 같은 불량유형이 존재하면 안됨.
    private void throwIfBadItemIdInLotMaster(Long lotMasterId, Long badItemTypeId) throws BadRequestException {
        List<Long> findBadItemIdByLotMasterId = workOrderBadItemRepo.findBadItemIdByLotMasterId(lotMasterId);
        boolean badItemIdAnyMatchByLotMaster = findBadItemIdByLotMasterId.stream().anyMatch(id -> id.equals(badItemTypeId));
        if (badItemIdAnyMatchByLotMaster) {
            throw new BadRequestException("하나의 로트는 같은 불량유형을 두개이상 등록 할 수 없음.");
        }
    }
}
