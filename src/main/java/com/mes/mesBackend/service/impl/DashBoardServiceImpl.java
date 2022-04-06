package com.mes.mesBackend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mes.mesBackend.dto.response.ItemInventoryStatusResponse;
import com.mes.mesBackend.dto.response.OperationStatusResponse;
import com.mes.mesBackend.dto.response.SalesRelatedStatusResponse;
import com.mes.mesBackend.dto.response.WorkProcessStatusResponse;
import com.mes.mesBackend.entity.enumeration.GoodsType;
import com.mes.mesBackend.helper.CalendarHelper;
import com.mes.mesBackend.repository.ContractRepository;
import com.mes.mesBackend.repository.LotMasterRepository;
import com.mes.mesBackend.repository.ShipmentRepository;
import com.mes.mesBackend.repository.WorkOrderDetailRepository;
import com.mes.mesBackend.service.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mes.mesBackend.entity.enumeration.OrderState.COMPLETION;
import static com.mes.mesBackend.entity.enumeration.OrderState.ONGOING;
import static com.mes.mesBackend.entity.enumeration.WorkProcessDivision.*;

// 대시보드
@Service
@RequiredArgsConstructor
public class DashBoardServiceImpl implements DashBoardService {
    private final WorkOrderDetailRepository workOrderDetailRepo;
    private final ContractRepository contractRepo;
    private final ShipmentRepository shipmentRepo;
    private final LotMasterRepository lotMasterRepo;
    private final CalendarHelper localDateHelper;

    // 생산현황, 수주현황, 출하현황, 출하완료 갯수 조회
    @Override
    public OperationStatusResponse getOperationStatus() {
        OperationStatusResponse response = new OperationStatusResponse();

        // 생산현황(작업오더 수)
        Long ongoingProduceOrderAmount = workOrderDetailRepo.findProduceOrderStateOngoingProductionAmountSum().orElse(0L);
        response.setOngoingProduceOrderAmount(ongoingProduceOrderAmount.intValue());

        // 납기일자 오늘 이후로 남은거 갯수
        Long contractAmount = contractRepo.findContractPeriodDateByTodayAmountSum().orElse(0L);
        response.setContractAmount(contractAmount.intValue());

        // 출하일자가 오늘인거
        Long shipmentTodayAmount = shipmentRepo.findShipmentCountByToday(null).orElse(0L);
        response.setShipmentSchedule(shipmentTodayAmount.intValue());

        // 출하 일자가 오늘이고 출하 상태 값이 완료인 경우
        Long shipmentCompletionAmount = shipmentRepo.findShipmentCountByToday(COMPLETION).orElse(0L);
        response.setShipmentCompletionAmount(shipmentCompletionAmount.intValue());
        return response;
    }

    // 작업 공정 별 정보
    @Override
    public WorkProcessStatusResponse getWorkProcessStatus() {
        WorkProcessStatusResponse response = new WorkProcessStatusResponse();

        // 원료혼합
        response.setMaterialMicingOngoingAmount(workOrderDetailRepo.findOrderStateCountByWorkProcessDivisionAndOrderState(MATERIAL_MIXING, ONGOING).orElse(0L));
        response.setMaterialMicingCompletionAmount(workOrderDetailRepo.findOrderStateCountByWorkProcessDivisionAndOrderState(MATERIAL_MIXING, COMPLETION).orElse(0L));
        response.setMaterialMicingProductionAmount(workOrderDetailRepo.findProductionAmountByWorkProcessDivision(MATERIAL_MIXING).orElse(0));

        // 충진
        response.setFillingOngoingAmount(workOrderDetailRepo.findOrderStateCountByWorkProcessDivisionAndOrderState(FILLING, ONGOING).orElse(0L));
        response.setFillingCompletionAmount(workOrderDetailRepo.findOrderStateCountByWorkProcessDivisionAndOrderState(FILLING, COMPLETION).orElse(0L));
        response.setFillingProductionAmount(workOrderDetailRepo.findProductionAmountByWorkProcessDivision(FILLING).orElse(0));

        // 캡조립
        response.setCapAssemblyOngoingAmount(workOrderDetailRepo.findOrderStateCountByWorkProcessDivisionAndOrderState(CAP_ASSEMBLY, ONGOING).orElse(0L));
        response.setCapAssemblyCompletionAmount(workOrderDetailRepo.findOrderStateCountByWorkProcessDivisionAndOrderState(CAP_ASSEMBLY, COMPLETION).orElse(0L));
        response.setCapAssemblyProductionAmount(workOrderDetailRepo.findProductionAmountByWorkProcessDivision(CAP_ASSEMBLY).orElse(0));

        // 라벨링
        response.setLabelingOngoingAmount(workOrderDetailRepo.findOrderStateCountByWorkProcessDivisionAndOrderState(LABELING, ONGOING).orElse(0L));
        response.setLabelingCompletionAmount(workOrderDetailRepo.findOrderStateCountByWorkProcessDivisionAndOrderState(LABELING, COMPLETION).orElse(0L));
        response.setLabelingProductionAmount(workOrderDetailRepo.findProductionAmountByWorkProcessDivision(LABELING).orElse(0));

        // 포장
        response.setPackagingOngoingAmount(workOrderDetailRepo.findOrderStateCountByWorkProcessDivisionAndOrderState(PACKAGING, ONGOING).orElse(0L));
        response.setPackagingCompletionAmount(workOrderDetailRepo.findOrderStateCountByWorkProcessDivisionAndOrderState(PACKAGING, COMPLETION).orElse(0L));
        response.setPackagingProductionAmount(workOrderDetailRepo.findProductionAmountByWorkProcessDivision(PACKAGING).orElse(0));

        return response;
    }

    // 품목계정 별 재고현황 정보
    // lotMaster 의 realLot 중 stockAmount 가 0 이상이고, 검색조건으로 품목계정이 들어왔을때 품목의 갯수는 5개
    @Override
    public List<ItemInventoryStatusResponse> getItemInventoryStatusResponse(GoodsType goodsType) {
        return lotMasterRepo.findItemInventoryStatusResponseByGoodsType(goodsType);
    }

    // 매출관련현황 - 수주
    @Override
    public List<ObjectNode> getContractSaleRelatedStatus() {
        LocalDate fromDate = localDateHelper.getNowMonthStartDate();
        LocalDate toDate = localDateHelper.getNowMonthEndDate();

        List<SalesRelatedStatusResponse> responses = contractRepo.findSalesRelatedStatusResponseByContractItems(fromDate, toDate);

        LocalDate firstWeekToDate = fromDate.plusWeeks(1);
        LocalDate secondWeekFromDate = firstWeekToDate.plusDays(1);
        LocalDate secondWeekToDate = secondWeekFromDate.plusWeeks(1);
        LocalDate thirdWeekFromDate = secondWeekToDate.plusDays(1);
        LocalDate thirdWeekToDate = thirdWeekFromDate.plusWeeks(1);
        LocalDate fourthWeekFromDate = thirdWeekToDate.plusDays(1);

        Long itemId1;
        String itemName1;
        Long itemId2;
        String itemName2;
        Long itemId3;
        String itemName3;
        Long itemId4;
        String itemName4;
        Long itemId5;
        String itemName5;

        int size = responses.size();

        ObjectMapper objectMapper1 = new ObjectMapper();
        ObjectNode json1 = objectMapper1.createObjectNode();

        ObjectMapper objectMapper2 = new ObjectMapper();
        ObjectNode json2 = objectMapper2.createObjectNode();

        ObjectMapper objectMapper3 = new ObjectMapper();
        ObjectNode json3 = objectMapper3.createObjectNode();

        ObjectMapper objectMapper4 = new ObjectMapper();
        ObjectNode json4 = objectMapper4.createObjectNode();

        List<ObjectNode> list = new ArrayList<>();
        if (size == 5) {
            itemId1 = responses.get(0).getItemId();
            itemName1 = responses.get(0).getItemName();
            itemId2 = responses.get(1).getItemId();
            itemName2 = responses.get(1).getItemName();
            itemId3 = responses.get(2).getItemId();
            itemName3 = responses.get(2).getItemName();
            itemId4 = responses.get(3).getItemId();
            itemName4 = responses.get(3).getItemName();
            itemId5 = responses.get(4).getItemId();
            itemName5 = responses.get(4).getItemName();

            json1.put("name", "첫째주");
            json1.put(itemName1, contractRepo.findWeekAmountByWeekDate(fromDate, firstWeekToDate, itemId1).orElse(0));
            json1.put(itemName2, contractRepo.findWeekAmountByWeekDate(fromDate, firstWeekToDate, itemId2).orElse(0));
            json1.put(itemName3, contractRepo.findWeekAmountByWeekDate(fromDate, firstWeekToDate, itemId3).orElse(0));
            json1.put(itemName4, contractRepo.findWeekAmountByWeekDate(fromDate, firstWeekToDate, itemId4).orElse(0));
            json1.put(itemName5, contractRepo.findWeekAmountByWeekDate(fromDate, firstWeekToDate, itemId5).orElse(0));
            list.add(json1);

            json2.put("name", "둘째주");
            json2.put(itemName1, contractRepo.findWeekAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId1).orElse(0));
            json2.put(itemName2, contractRepo.findWeekAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId2).orElse(0));
            json2.put(itemName3, contractRepo.findWeekAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId3).orElse(0));
            json2.put(itemName4, contractRepo.findWeekAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId4).orElse(0));
            json2.put(itemName5, contractRepo.findWeekAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId5).orElse(0));
            list.add(json2);

            json3.put("name", "셋째주");
            json3.put(itemName1, contractRepo.findWeekAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId1).orElse(0));
            json3.put(itemName2, contractRepo.findWeekAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId2).orElse(0));
            json3.put(itemName3, contractRepo.findWeekAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId3).orElse(0));
            json3.put(itemName4, contractRepo.findWeekAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId4).orElse(0));
            json3.put(itemName5, contractRepo.findWeekAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId5).orElse(0));
            list.add(json3);

            json4.put("name", "넷째주");
            json4.put(itemName1, contractRepo.findWeekAmountByWeekDate(fourthWeekFromDate, toDate, itemId1).orElse(0));
            json4.put(itemName2, contractRepo.findWeekAmountByWeekDate(fourthWeekFromDate, toDate, itemId2).orElse(0));
            json4.put(itemName3, contractRepo.findWeekAmountByWeekDate(fourthWeekFromDate, toDate, itemId3).orElse(0));
            json4.put(itemName4, contractRepo.findWeekAmountByWeekDate(fourthWeekFromDate, toDate, itemId4).orElse(0));
            json4.put(itemName5, contractRepo.findWeekAmountByWeekDate(fourthWeekFromDate, toDate, itemId5).orElse(0));
            list.add(json4);
        }
        if (size == 4) {
            itemId1 = responses.get(0).getItemId();
            itemName1 = responses.get(0).getItemName();
            itemId2 = responses.get(1).getItemId();
            itemName2 = responses.get(1).getItemName();
            itemId3 = responses.get(2).getItemId();
            itemName3 = responses.get(2).getItemName();
            itemId4 = responses.get(3).getItemId();
            itemName4 = responses.get(3).getItemName();
            json1.put("name", "첫째주");
            json1.put(itemName1, contractRepo.findWeekAmountByWeekDate(fromDate, firstWeekToDate, itemId1).orElse(0));
            json1.put(itemName2, contractRepo.findWeekAmountByWeekDate(fromDate, firstWeekToDate, itemId2).orElse(0));
            json1.put(itemName3, contractRepo.findWeekAmountByWeekDate(fromDate, firstWeekToDate, itemId3).orElse(0));
            json1.put(itemName4, contractRepo.findWeekAmountByWeekDate(fromDate, firstWeekToDate, itemId4).orElse(0));
            list.add(json1);

            json2.put("name", "둘째주");
            json2.put(itemName1, contractRepo.findWeekAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId1).orElse(0));
            json2.put(itemName2, contractRepo.findWeekAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId2).orElse(0));
            json2.put(itemName3, contractRepo.findWeekAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId3).orElse(0));
            json2.put(itemName4, contractRepo.findWeekAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId4).orElse(0));
            list.add(json2);

            json3.put("name", "셋째주");
            json3.put(itemName1, contractRepo.findWeekAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId1).orElse(0));
            json3.put(itemName2, contractRepo.findWeekAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId2).orElse(0));
            json3.put(itemName3, contractRepo.findWeekAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId3).orElse(0));
            json3.put(itemName4, contractRepo.findWeekAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId4).orElse(0));
            list.add(json3);

            json4.put("name", "넷째주");
            json4.put(itemName1, contractRepo.findWeekAmountByWeekDate(fourthWeekFromDate, toDate, itemId1).orElse(0));
            json4.put(itemName2, contractRepo.findWeekAmountByWeekDate(fourthWeekFromDate, toDate, itemId2).orElse(0));
            json4.put(itemName3, contractRepo.findWeekAmountByWeekDate(fourthWeekFromDate, toDate, itemId3).orElse(0));
            json4.put(itemName4, contractRepo.findWeekAmountByWeekDate(fourthWeekFromDate, toDate, itemId4).orElse(0));
            list.add(json4);
        }
        if (size == 3) {
            itemId1 = responses.get(0).getItemId();
            itemName1 = responses.get(0).getItemName();
            itemId2 = responses.get(1).getItemId();
            itemName2 = responses.get(1).getItemName();
            itemId3 = responses.get(2).getItemId();
            itemName3 = responses.get(2).getItemName();
            json1.put("name", "첫째주");
            json1.put(itemName1, contractRepo.findWeekAmountByWeekDate(fromDate, firstWeekToDate, itemId1).orElse(0));
            json1.put(itemName2, contractRepo.findWeekAmountByWeekDate(fromDate, firstWeekToDate, itemId2).orElse(0));
            json1.put(itemName3, contractRepo.findWeekAmountByWeekDate(fromDate, firstWeekToDate, itemId3).orElse(0));
            list.add(json1);

            json2.put("name", "둘째주");
            json2.put(itemName1, contractRepo.findWeekAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId1).orElse(0));
            json2.put(itemName2, contractRepo.findWeekAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId2).orElse(0));
            json2.put(itemName3, contractRepo.findWeekAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId3).orElse(0));
            list.add(json2);

            json3.put("name", "셋째주");
            json3.put(itemName1, contractRepo.findWeekAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId1).orElse(0));
            json3.put(itemName2, contractRepo.findWeekAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId2).orElse(0));
            json3.put(itemName3, contractRepo.findWeekAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId3).orElse(0));
            list.add(json3);

            json4.put("name", "넷째주");
            json4.put(itemName1, contractRepo.findWeekAmountByWeekDate(fourthWeekFromDate, toDate, itemId1).orElse(0));
            json4.put(itemName2, contractRepo.findWeekAmountByWeekDate(fourthWeekFromDate, toDate, itemId2).orElse(0));
            json4.put(itemName3, contractRepo.findWeekAmountByWeekDate(fourthWeekFromDate, toDate, itemId3).orElse(0));
            list.add(json4);
        }
        if (size == 2) {
            itemId1 = responses.get(0).getItemId();
            itemName1 = responses.get(0).getItemName();
            itemId2 = responses.get(1).getItemId();
            itemName2 = responses.get(1).getItemName();
            json1.put("name", "첫째주");
            json1.put(itemName1, contractRepo.findWeekAmountByWeekDate(fromDate, firstWeekToDate, itemId1).orElse(0));
            json1.put(itemName2, contractRepo.findWeekAmountByWeekDate(fromDate, firstWeekToDate, itemId2).orElse(0));
            list.add(json1);

            json2.put("name", "둘째주");
            json2.put(itemName1, contractRepo.findWeekAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId1).orElse(0));
            json2.put(itemName2, contractRepo.findWeekAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId2).orElse(0));
            list.add(json2);

            json3.put("name", "셋째주");
            json3.put(itemName1, contractRepo.findWeekAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId1).orElse(0));
            json3.put(itemName2, contractRepo.findWeekAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId2).orElse(0));
            list.add(json3);

            json4.put("name", "넷째주");
            json4.put(itemName1, contractRepo.findWeekAmountByWeekDate(fourthWeekFromDate, toDate, itemId1).orElse(0));
            json4.put(itemName2, contractRepo.findWeekAmountByWeekDate(fourthWeekFromDate, toDate, itemId2).orElse(0));
            list.add(json4);
        }
        if (size == 1) {
            itemId1 = responses.get(0).getItemId();
            itemName1 = responses.get(0).getItemName();
            json1.put("name", "첫째주");
            json1.put(itemName1, contractRepo.findWeekAmountByWeekDate(fromDate, firstWeekToDate, itemId1).orElse(0));
            list.add(json1);

            json2.put("name", "둘째주");
            json2.put(itemName1, contractRepo.findWeekAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId1).orElse(0));
            list.add(json2);

            json3.put("name", "셋째주");
            json3.put(itemName1, contractRepo.findWeekAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId1).orElse(0));
            list.add(json3);

            json4.put("name", "넷째주");
            json4.put(itemName1, contractRepo.findWeekAmountByWeekDate(fourthWeekFromDate, toDate, itemId1).orElse(0));
            list.add(json4);
        }
        return list;
    }

    // 매출관련현황 - 제품 생산
    @Override
    public List<ObjectNode> getProductSaleRelatedStatus() {
        LocalDate fromDate = localDateHelper.getNowMonthStartDate();
        LocalDate toDate = localDateHelper.getNowMonthEndDate();

        LocalDate firstWeekToDate = fromDate.plusWeeks(1);
        LocalDate secondWeekFromDate = firstWeekToDate.plusDays(1);
        LocalDate secondWeekToDate = secondWeekFromDate.plusWeeks(1);
        LocalDate thirdWeekFromDate = secondWeekToDate.plusDays(1);
        LocalDate thirdWeekToDate = thirdWeekFromDate.plusWeeks(1);
        LocalDate fourthWeekFromDate = thirdWeekToDate.plusDays(1);

        List<SalesRelatedStatusResponse> responses = lotMasterRepo.findSalesRelatedStatusResponseByProductItems(fromDate, toDate);

        Long itemId1;
        String itemName1;
        Long itemId2;
        String itemName2;
        Long itemId3;
        String itemName3;
        Long itemId4;
        String itemName4;
        Long itemId5;
        String itemName5;

        int size = responses.size();

        ObjectMapper objectMapper1 = new ObjectMapper();
        ObjectNode json1 = objectMapper1.createObjectNode();

        ObjectMapper objectMapper2 = new ObjectMapper();
        ObjectNode json2 = objectMapper2.createObjectNode();

        ObjectMapper objectMapper3 = new ObjectMapper();
        ObjectNode json3 = objectMapper3.createObjectNode();

        ObjectMapper objectMapper4 = new ObjectMapper();
        ObjectNode json4 = objectMapper4.createObjectNode();

        List<ObjectNode> list = new ArrayList<>();

        if (size == 5) {
            itemId1 = responses.get(0).getItemId();
            itemName1 = responses.get(0).getItemName();
            itemId2 = responses.get(1).getItemId();
            itemName2 = responses.get(1).getItemName();
            itemId3 = responses.get(2).getItemId();
            itemName3 = responses.get(2).getItemName();
            itemId4 = responses.get(3).getItemId();
            itemName4 = responses.get(3).getItemName();
            itemId5 = responses.get(4).getItemId();
            itemName5 = responses.get(4).getItemName();
            json1.put("name", "첫째주");
            json1.put(itemName1, lotMasterRepo.findCreatedAmountByWeekDate(fromDate, firstWeekToDate, itemId1).orElse(0));
            json1.put(itemName2, lotMasterRepo.findCreatedAmountByWeekDate(fromDate, firstWeekToDate, itemId2).orElse(0));
            json1.put(itemName3, lotMasterRepo.findCreatedAmountByWeekDate(fromDate, firstWeekToDate, itemId3).orElse(0));
            json1.put(itemName4, lotMasterRepo.findCreatedAmountByWeekDate(fromDate, firstWeekToDate, itemId4).orElse(0));
            json1.put(itemName5, lotMasterRepo.findCreatedAmountByWeekDate(fromDate, firstWeekToDate, itemId5).orElse(0));
            list.add(json1);

            json2.put("name", "둘째주");
            json2.put(itemName1, lotMasterRepo.findCreatedAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId1).orElse(0));
            json2.put(itemName2, lotMasterRepo.findCreatedAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId2).orElse(0));
            json2.put(itemName3, lotMasterRepo.findCreatedAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId3).orElse(0));
            json2.put(itemName4, lotMasterRepo.findCreatedAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId4).orElse(0));
            json2.put(itemName5, lotMasterRepo.findCreatedAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId5).orElse(0));
            list.add(json2);

            json3.put("name", "셋째주");
            json3.put(itemName1, lotMasterRepo.findCreatedAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId1).orElse(0));
            json3.put(itemName2, lotMasterRepo.findCreatedAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId2).orElse(0));
            json3.put(itemName3, lotMasterRepo.findCreatedAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId3).orElse(0));
            json3.put(itemName4, lotMasterRepo.findCreatedAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId4).orElse(0));
            json3.put(itemName5, lotMasterRepo.findCreatedAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId5).orElse(0));
            list.add(json3);

            json4.put("name", "넷째주");
            json4.put(itemName1, lotMasterRepo.findCreatedAmountByWeekDate(fourthWeekFromDate, toDate, itemId1).orElse(0));
            json4.put(itemName2, lotMasterRepo.findCreatedAmountByWeekDate(fourthWeekFromDate, toDate, itemId2).orElse(0));
            json4.put(itemName3, lotMasterRepo.findCreatedAmountByWeekDate(fourthWeekFromDate, toDate, itemId3).orElse(0));
            json4.put(itemName4, lotMasterRepo.findCreatedAmountByWeekDate(fourthWeekFromDate, toDate, itemId4).orElse(0));
            json4.put(itemName5, lotMasterRepo.findCreatedAmountByWeekDate(fourthWeekFromDate, toDate, itemId5).orElse(0));
            list.add(json4);
        }
        if (size == 4) {
            itemId1 = responses.get(0).getItemId();
            itemName1 = responses.get(0).getItemName();
            itemId2 = responses.get(1).getItemId();
            itemName2 = responses.get(1).getItemName();
            itemId3 = responses.get(2).getItemId();
            itemName3 = responses.get(2).getItemName();
            itemId4 = responses.get(3).getItemId();
            itemName4 = responses.get(3).getItemName();
            json1.put("name", "첫째주");
            json1.put(itemName1, lotMasterRepo.findCreatedAmountByWeekDate(fromDate, firstWeekToDate, itemId1).orElse(0));
            json1.put(itemName2, lotMasterRepo.findCreatedAmountByWeekDate(fromDate, firstWeekToDate, itemId2).orElse(0));
            json1.put(itemName3, lotMasterRepo.findCreatedAmountByWeekDate(fromDate, firstWeekToDate, itemId3).orElse(0));
            json1.put(itemName4, lotMasterRepo.findCreatedAmountByWeekDate(fromDate, firstWeekToDate, itemId4).orElse(0));
            list.add(json1);

            json2.put("name", "둘째주");
            json2.put(itemName1, lotMasterRepo.findCreatedAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId1).orElse(0));
            json2.put(itemName2, lotMasterRepo.findCreatedAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId2).orElse(0));
            json2.put(itemName3, lotMasterRepo.findCreatedAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId3).orElse(0));
            json2.put(itemName4, lotMasterRepo.findCreatedAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId4).orElse(0));
            list.add(json2);

            json3.put("name", "셋째주");
            json3.put(itemName1, lotMasterRepo.findCreatedAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId1).orElse(0));
            json3.put(itemName2, lotMasterRepo.findCreatedAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId2).orElse(0));
            json3.put(itemName3, lotMasterRepo.findCreatedAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId3).orElse(0));
            json3.put(itemName4, lotMasterRepo.findCreatedAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId4).orElse(0));
            list.add(json3);

            json4.put("name", "넷째주");
            json4.put(itemName1, lotMasterRepo.findCreatedAmountByWeekDate(fourthWeekFromDate, toDate, itemId1).orElse(0));
            json4.put(itemName2, lotMasterRepo.findCreatedAmountByWeekDate(fourthWeekFromDate, toDate, itemId2).orElse(0));
            json4.put(itemName3, lotMasterRepo.findCreatedAmountByWeekDate(fourthWeekFromDate, toDate, itemId3).orElse(0));
            json4.put(itemName4, lotMasterRepo.findCreatedAmountByWeekDate(fourthWeekFromDate, toDate, itemId4).orElse(0));
            list.add(json4);
        }
        if (size == 3) {
            itemId1 = responses.get(0).getItemId();
            itemName1 = responses.get(0).getItemName();
            itemId2 = responses.get(1).getItemId();
            itemName2 = responses.get(1).getItemName();
            itemId3 = responses.get(2).getItemId();
            itemName3 = responses.get(2).getItemName();
            json1.put("name", "첫째주");
            json1.put(itemName1, lotMasterRepo.findCreatedAmountByWeekDate(fromDate, firstWeekToDate, itemId1).orElse(0));
            json1.put(itemName2, lotMasterRepo.findCreatedAmountByWeekDate(fromDate, firstWeekToDate, itemId2).orElse(0));
            json1.put(itemName3, lotMasterRepo.findCreatedAmountByWeekDate(fromDate, firstWeekToDate, itemId3).orElse(0));
            list.add(json1);

            json2.put("name", "둘째주");
            json2.put(itemName1, lotMasterRepo.findCreatedAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId1).orElse(0));
            json2.put(itemName2, lotMasterRepo.findCreatedAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId2).orElse(0));
            json2.put(itemName3, lotMasterRepo.findCreatedAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId3).orElse(0));
            list.add(json2);

            json3.put("name", "셋째주");
            json3.put(itemName1, lotMasterRepo.findCreatedAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId1).orElse(0));
            json3.put(itemName2, lotMasterRepo.findCreatedAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId2).orElse(0));
            json3.put(itemName3, lotMasterRepo.findCreatedAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId3).orElse(0));
            list.add(json3);

            json4.put("name", "넷째주");
            json4.put(itemName1, lotMasterRepo.findCreatedAmountByWeekDate(fourthWeekFromDate, toDate, itemId1).orElse(0));
            json4.put(itemName2, lotMasterRepo.findCreatedAmountByWeekDate(fourthWeekFromDate, toDate, itemId2).orElse(0));
            json4.put(itemName3, lotMasterRepo.findCreatedAmountByWeekDate(fourthWeekFromDate, toDate, itemId3).orElse(0));
            list.add(json4);
        }
        if (size == 2) {
            itemId1 = responses.get(0).getItemId();
            itemName1 = responses.get(0).getItemName();
            itemId2 = responses.get(1).getItemId();
            itemName2 = responses.get(1).getItemName();
            json1.put("name", "첫째주");
            json1.put(itemName1, lotMasterRepo.findCreatedAmountByWeekDate(fromDate, firstWeekToDate, itemId1).orElse(0));
            json1.put(itemName2, lotMasterRepo.findCreatedAmountByWeekDate(fromDate, firstWeekToDate, itemId2).orElse(0));
            list.add(json1);

            json2.put("name", "둘째주");
            json2.put(itemName1, lotMasterRepo.findCreatedAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId1).orElse(0));
            json2.put(itemName2, lotMasterRepo.findCreatedAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId2).orElse(0));
            list.add(json2);

            json3.put("name", "셋째주");
            json3.put(itemName1, lotMasterRepo.findCreatedAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId1).orElse(0));
            json3.put(itemName2, lotMasterRepo.findCreatedAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId2).orElse(0));
            list.add(json3);

            json4.put("name", "넷째주");
            json4.put(itemName1, lotMasterRepo.findCreatedAmountByWeekDate(fourthWeekFromDate, toDate, itemId1).orElse(0));
            json4.put(itemName2, lotMasterRepo.findCreatedAmountByWeekDate(fourthWeekFromDate, toDate, itemId2).orElse(0));
            list.add(json4);
        }
        if (size == 1) {
            itemId1 = responses.get(0).getItemId();
            itemName1 = responses.get(0).getItemName();
            json1.put("name", "첫째주");
            json1.put(itemName1, lotMasterRepo.findCreatedAmountByWeekDate(fromDate, firstWeekToDate, itemId1).orElse(0));
            list.add(json1);

            json2.put("name", "둘째주");
            json2.put(itemName1, lotMasterRepo.findCreatedAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId1).orElse(0));
            list.add(json2);

            json3.put("name", "셋째주");
            json3.put(itemName1, lotMasterRepo.findCreatedAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId1).orElse(0));
            list.add(json3);

            json4.put("name", "넷째주");
            json4.put(itemName1, lotMasterRepo.findCreatedAmountByWeekDate(fourthWeekFromDate, toDate, itemId1).orElse(0));
            list.add(json4);
        }
        return list;
    }

    // 매출관련현황 - 제품출고
    @Override
    public List<ObjectNode> getShipmentSaleRelatedStatus() {
        LocalDate fromDate = localDateHelper.getNowMonthStartDate();
        LocalDate toDate = localDateHelper.getNowMonthEndDate();

        LocalDate firstWeekToDate = fromDate.plusWeeks(1);
        LocalDate secondWeekFromDate = firstWeekToDate.plusDays(1);
        LocalDate secondWeekToDate = secondWeekFromDate.plusWeeks(1);
        LocalDate thirdWeekFromDate = secondWeekToDate.plusDays(1);
        LocalDate thirdWeekToDate = thirdWeekFromDate.plusWeeks(1);
        LocalDate fourthWeekFromDate = thirdWeekToDate.plusDays(1);

        List<SalesRelatedStatusResponse> responses = shipmentRepo.findSalesRelatedStatusResponseByShipmentItems(fromDate, toDate);

        Long itemId1;
        String itemName1;
        Long itemId2;
        String itemName2;
        Long itemId3;
        String itemName3;
        Long itemId4;
        String itemName4;
        Long itemId5;
        String itemName5;

        int size = responses.size();

        ObjectMapper objectMapper1 = new ObjectMapper();
        ObjectNode json1 = objectMapper1.createObjectNode();

        ObjectMapper objectMapper2 = new ObjectMapper();
        ObjectNode json2 = objectMapper2.createObjectNode();

        ObjectMapper objectMapper3 = new ObjectMapper();
        ObjectNode json3 = objectMapper3.createObjectNode();

        ObjectMapper objectMapper4 = new ObjectMapper();
        ObjectNode json4 = objectMapper4.createObjectNode();

        List<ObjectNode> list = new ArrayList<>();
        
        if (size == 5) {
            itemId1 = responses.get(0).getItemId();
            itemName1 = responses.get(0).getItemName();
            itemId2 = responses.get(1).getItemId();
            itemName2 = responses.get(1).getItemName();
            itemId3 = responses.get(2).getItemId();
            itemName3 = responses.get(2).getItemName();
            itemId4 = responses.get(3).getItemId();
            itemName4 = responses.get(3).getItemName();
            itemId5 = responses.get(4).getItemId();
            itemName5 = responses.get(4).getItemName();
            json1.put("name", "첫째주");
            json1.put(itemName1, shipmentRepo.findShipmentAmountByWeekDate(fromDate, firstWeekToDate, itemId1).orElse(0));
            json1.put(itemName2, shipmentRepo.findShipmentAmountByWeekDate(fromDate, firstWeekToDate, itemId2).orElse(0));
            json1.put(itemName3, shipmentRepo.findShipmentAmountByWeekDate(fromDate, firstWeekToDate, itemId3).orElse(0));
            json1.put(itemName4, shipmentRepo.findShipmentAmountByWeekDate(fromDate, firstWeekToDate, itemId4).orElse(0));
            json1.put(itemName5, shipmentRepo.findShipmentAmountByWeekDate(fromDate, firstWeekToDate, itemId5).orElse(0));
            list.add(json1);

            json2.put("name", "둘째주");
            json2.put(itemName1, shipmentRepo.findShipmentAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId1).orElse(0));
            json2.put(itemName2, shipmentRepo.findShipmentAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId2).orElse(0));
            json2.put(itemName3, shipmentRepo.findShipmentAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId3).orElse(0));
            json2.put(itemName4, shipmentRepo.findShipmentAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId4).orElse(0));
            json2.put(itemName5, shipmentRepo.findShipmentAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId5).orElse(0));
            list.add(json2);

            json3.put("name", "셋째주");
            json3.put(itemName1, shipmentRepo.findShipmentAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId1).orElse(0));
            json3.put(itemName2, shipmentRepo.findShipmentAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId2).orElse(0));
            json3.put(itemName3, shipmentRepo.findShipmentAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId3).orElse(0));
            json3.put(itemName4, shipmentRepo.findShipmentAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId4).orElse(0));
            json3.put(itemName5, shipmentRepo.findShipmentAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId5).orElse(0));
            list.add(json3);

            json4.put("name", "넷째주");
            json4.put(itemName1, shipmentRepo.findShipmentAmountByWeekDate(fourthWeekFromDate, toDate, itemId1).orElse(0));
            json4.put(itemName2, shipmentRepo.findShipmentAmountByWeekDate(fourthWeekFromDate, toDate, itemId2).orElse(0));
            json4.put(itemName3, shipmentRepo.findShipmentAmountByWeekDate(fourthWeekFromDate, toDate, itemId3).orElse(0));
            json4.put(itemName4, shipmentRepo.findShipmentAmountByWeekDate(fourthWeekFromDate, toDate, itemId4).orElse(0));
            json4.put(itemName5, shipmentRepo.findShipmentAmountByWeekDate(fourthWeekFromDate, toDate, itemId5).orElse(0));
            list.add(json4);
        }
        if (size == 4) {
            itemId1 = responses.get(0).getItemId();
            itemName1 = responses.get(0).getItemName();
            itemId2 = responses.get(1).getItemId();
            itemName2 = responses.get(1).getItemName();
            itemId3 = responses.get(2).getItemId();
            itemName3 = responses.get(2).getItemName();
            itemId4 = responses.get(3).getItemId();
            itemName4 = responses.get(3).getItemName();
            json1.put("name", "첫째주");
            json1.put(itemName1, shipmentRepo.findShipmentAmountByWeekDate(fromDate, firstWeekToDate, itemId1).orElse(0));
            json1.put(itemName2, shipmentRepo.findShipmentAmountByWeekDate(fromDate, firstWeekToDate, itemId2).orElse(0));
            json1.put(itemName3, shipmentRepo.findShipmentAmountByWeekDate(fromDate, firstWeekToDate, itemId3).orElse(0));
            json1.put(itemName4, shipmentRepo.findShipmentAmountByWeekDate(fromDate, firstWeekToDate, itemId4).orElse(0));
            list.add(json1);

            json2.put("name", "둘째주");
            json2.put(itemName1, shipmentRepo.findShipmentAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId1).orElse(0));
            json2.put(itemName2, shipmentRepo.findShipmentAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId2).orElse(0));
            json2.put(itemName3, shipmentRepo.findShipmentAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId3).orElse(0));
            json2.put(itemName4, shipmentRepo.findShipmentAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId4).orElse(0));
            list.add(json2);

            json3.put("name", "셋째주");
            json3.put(itemName1, shipmentRepo.findShipmentAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId1).orElse(0));
            json3.put(itemName2, shipmentRepo.findShipmentAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId2).orElse(0));
            json3.put(itemName3, shipmentRepo.findShipmentAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId3).orElse(0));
            json3.put(itemName4, shipmentRepo.findShipmentAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId4).orElse(0));
            list.add(json3);

            json4.put("name", "넷째주");
            json4.put(itemName1, shipmentRepo.findShipmentAmountByWeekDate(fourthWeekFromDate, toDate, itemId1).orElse(0));
            json4.put(itemName2, shipmentRepo.findShipmentAmountByWeekDate(fourthWeekFromDate, toDate, itemId2).orElse(0));
            json4.put(itemName3, shipmentRepo.findShipmentAmountByWeekDate(fourthWeekFromDate, toDate, itemId3).orElse(0));
            json4.put(itemName4, shipmentRepo.findShipmentAmountByWeekDate(fourthWeekFromDate, toDate, itemId4).orElse(0));
            list.add(json4);
        }
        if (size == 3) {
            itemId1 = responses.get(0).getItemId();
            itemName1 = responses.get(0).getItemName();
            itemId2 = responses.get(1).getItemId();
            itemName2 = responses.get(1).getItemName();
            itemId3 = responses.get(2).getItemId();
            itemName3 = responses.get(2).getItemName();
            json1.put("name", "첫째주");
            json1.put(itemName1, shipmentRepo.findShipmentAmountByWeekDate(fromDate, firstWeekToDate, itemId1).orElse(0));
            json1.put(itemName2, shipmentRepo.findShipmentAmountByWeekDate(fromDate, firstWeekToDate, itemId2).orElse(0));
            json1.put(itemName3, shipmentRepo.findShipmentAmountByWeekDate(fromDate, firstWeekToDate, itemId3).orElse(0));
            list.add(json1);

            json2.put("name", "둘째주");
            json2.put(itemName1, shipmentRepo.findShipmentAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId1).orElse(0));
            json2.put(itemName2, shipmentRepo.findShipmentAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId2).orElse(0));
            json2.put(itemName3, shipmentRepo.findShipmentAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId3).orElse(0));
            list.add(json2);

            json3.put("name", "셋째주");
            json3.put(itemName1, shipmentRepo.findShipmentAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId1).orElse(0));
            json3.put(itemName2, shipmentRepo.findShipmentAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId2).orElse(0));
            json3.put(itemName3, shipmentRepo.findShipmentAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId3).orElse(0));
            list.add(json3);

            json4.put("name", "넷째주");
            json4.put(itemName1, shipmentRepo.findShipmentAmountByWeekDate(fourthWeekFromDate, toDate, itemId1).orElse(0));
            json4.put(itemName2, shipmentRepo.findShipmentAmountByWeekDate(fourthWeekFromDate, toDate, itemId2).orElse(0));
            json4.put(itemName3, shipmentRepo.findShipmentAmountByWeekDate(fourthWeekFromDate, toDate, itemId3).orElse(0));
            list.add(json4);
        }
        if (size == 2) {
            itemId1 = responses.get(0).getItemId();
            itemName1 = responses.get(0).getItemName();
            itemId2 = responses.get(1).getItemId();
            itemName2 = responses.get(1).getItemName();
            json1.put("name", "첫째주");
            json1.put(itemName1, shipmentRepo.findShipmentAmountByWeekDate(fromDate, firstWeekToDate, itemId1).orElse(0));
            json1.put(itemName2, shipmentRepo.findShipmentAmountByWeekDate(fromDate, firstWeekToDate, itemId2).orElse(0));
            list.add(json1);

            json2.put("name", "둘째주");
            json2.put(itemName1, shipmentRepo.findShipmentAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId1).orElse(0));
            json2.put(itemName2, shipmentRepo.findShipmentAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId2).orElse(0));
            list.add(json2);

            json3.put("name", "셋째주");
            json3.put(itemName1, shipmentRepo.findShipmentAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId1).orElse(0));
            json3.put(itemName2, shipmentRepo.findShipmentAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId2).orElse(0));
            list.add(json3);

            json4.put("name", "넷째주");
            json4.put(itemName1, shipmentRepo.findShipmentAmountByWeekDate(fourthWeekFromDate, toDate, itemId1).orElse(0));
            json4.put(itemName2, shipmentRepo.findShipmentAmountByWeekDate(fourthWeekFromDate, toDate, itemId2).orElse(0));
            list.add(json4);
        }
        if (size == 1) {
            itemId1 = responses.get(0).getItemId();
            itemName1 = responses.get(0).getItemName();
            json1.put("name", "첫째주");
            json1.put(itemName1, shipmentRepo.findShipmentAmountByWeekDate(fromDate, firstWeekToDate, itemId1).orElse(0));
            list.add(json1);

            json2.put("name", "둘째주");
            json2.put(itemName1, shipmentRepo.findShipmentAmountByWeekDate(secondWeekFromDate, secondWeekToDate, itemId1).orElse(0));
            list.add(json2);

            json3.put("name", "셋째주");
            json3.put(itemName1, shipmentRepo.findShipmentAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, itemId1).orElse(0));
            list.add(json3);

            json4.put("name", "넷째주");
            json4.put(itemName1, shipmentRepo.findShipmentAmountByWeekDate(fourthWeekFromDate, toDate, itemId1).orElse(0));
            list.add(json4);
        }
        return list;
    }
}
