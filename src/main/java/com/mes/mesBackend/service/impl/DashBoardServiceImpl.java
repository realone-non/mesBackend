package com.mes.mesBackend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mes.mesBackend.dto.response.ItemInventoryStatusResponse;
import com.mes.mesBackend.dto.response.ItemResponse;
import com.mes.mesBackend.dto.response.OperationStatusResponse;
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
import static com.mes.mesBackend.service.impl.DashBoardServiceImpl.SaleRelatedStatusDivision.CONTRACT;
import static com.mes.mesBackend.service.impl.DashBoardServiceImpl.SaleRelatedStatusDivision.PRODUCT;

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

    enum SaleRelatedStatusDivision {
        CONTRACT, PRODUCT, SHIPMENT
    }

    // 품목 별 기간 수량
    private Integer getWeekAmountByWeekDate(LocalDate fromDate, LocalDate toDate, Long itemId, SaleRelatedStatusDivision division) {
        if (division.equals(CONTRACT)) return contractRepo.findWeekAmountByWeekDate(fromDate, toDate, itemId).orElse(0);
        else if (division.equals(PRODUCT)) return lotMasterRepo.findCreatedAmountByWeekDate(fromDate, toDate, itemId).orElse(0);
        else return shipmentRepo.findShipmentAmountByWeekDate(fromDate, toDate, itemId).orElse(0);
    }

    // 품목 조회된거가지고
    private ObjectNode getWeekObjectNode(
            String name,
            int size,
            LocalDate fromDate,
            LocalDate toDate,
            List<ItemResponse.noAndName> responses,
            SaleRelatedStatusDivision division
    ) {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("name", name);
        for (int i = 0; i < size; i++) {
            if (responses.get(i) != null) {
                objectNode.put(responses.get(i).getItemName(), getWeekAmountByWeekDate(fromDate, toDate, responses.get(i).getId(), division));
            }
        }
        return objectNode;
    }

    // 매출관련현황 - 수주
    @Override
    public List<ObjectNode> getContractSaleRelatedStatus() {
        LocalDate fromDate = localDateHelper.getNowMonthStartDate();
        LocalDate toDate = localDateHelper.getNowMonthEndDate();

        List<ItemResponse.noAndName> responses = contractRepo.findSalesRelatedStatusResponseByContractItems(fromDate, toDate);

        LocalDate firstWeekToDate = fromDate.plusWeeks(1);
        LocalDate secondWeekFromDate = firstWeekToDate.plusDays(1);
        LocalDate secondWeekToDate = secondWeekFromDate.plusWeeks(1);
        LocalDate thirdWeekFromDate = secondWeekToDate.plusDays(1);
        LocalDate thirdWeekToDate = thirdWeekFromDate.plusWeeks(1);
        LocalDate fourthWeekFromDate = thirdWeekToDate.plusDays(1);

        int size = responses.size();
        List<ObjectNode> array = new ArrayList<>();
        array.add(getWeekObjectNode("첫째주", size, fromDate, firstWeekToDate, responses, CONTRACT));
        array.add(getWeekObjectNode("둘째주", size, secondWeekFromDate, secondWeekToDate, responses, CONTRACT));
        array.add(getWeekObjectNode("셋째주", size, thirdWeekFromDate, thirdWeekToDate, responses, CONTRACT));
        array.add(getWeekObjectNode("넷째주", size, fourthWeekFromDate, toDate, responses, CONTRACT));

        return array;
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

        List<ItemResponse.noAndName> responses = lotMasterRepo.findSalesRelatedStatusResponseByProductItems(fromDate, toDate);
        int size = responses.size();

        List<ObjectNode> array = new ArrayList<>();
        array.add(getWeekObjectNode("첫째주", size, fromDate, firstWeekToDate, responses, PRODUCT));
        array.add(getWeekObjectNode("둘째주", size, secondWeekFromDate, secondWeekToDate, responses, PRODUCT));
        array.add(getWeekObjectNode("셋째주", size, thirdWeekFromDate, thirdWeekToDate, responses, PRODUCT));
        array.add(getWeekObjectNode("넷째주", size, fourthWeekFromDate, toDate, responses, PRODUCT));

        return array;
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

        List<ItemResponse.noAndName> responses = shipmentRepo.findSalesRelatedStatusResponseByShipmentItems(fromDate, toDate);
        int size = responses.size();

        List<ObjectNode> array = new ArrayList<>();
        array.add(getWeekObjectNode("첫째주", size, fromDate, firstWeekToDate, responses, SaleRelatedStatusDivision.SHIPMENT));
        array.add(getWeekObjectNode("둘째주", size, secondWeekFromDate, secondWeekToDate, responses, SaleRelatedStatusDivision.SHIPMENT));
        array.add(getWeekObjectNode("셋째주", size, thirdWeekFromDate, thirdWeekToDate, responses, SaleRelatedStatusDivision.SHIPMENT));
        array.add(getWeekObjectNode("넷째주", size, fourthWeekFromDate, toDate, responses, SaleRelatedStatusDivision.SHIPMENT));

        return array;
    }
}
