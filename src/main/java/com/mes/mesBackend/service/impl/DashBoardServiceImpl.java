package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.ItemInventoryStatusResponse;
import com.mes.mesBackend.dto.response.OperationStatusResponse;
import com.mes.mesBackend.dto.response.SalesRelatedStatusResponse;
import com.mes.mesBackend.dto.response.WorkProcessStatusResponse;
import com.mes.mesBackend.entity.enumeration.GoodsType;
import com.mes.mesBackend.helper.LocalDateHelper;
import com.mes.mesBackend.repository.ContractRepository;
import com.mes.mesBackend.repository.LotMasterRepository;
import com.mes.mesBackend.repository.ShipmentRepository;
import com.mes.mesBackend.repository.WorkOrderDetailRepository;
import com.mes.mesBackend.service.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private final LocalDateHelper localDateHelper;

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
        response.setMaterialMicingProductionAmount(workOrderDetailRepo.findProductionAmountByWorkProcessDivision(MATERIAL_MIXING));

        // 충진
        response.setFillingOngoingAmount(workOrderDetailRepo.findOrderStateCountByWorkProcessDivisionAndOrderState(FILLING, ONGOING).orElse(0L));
        response.setFillingCompletionAmount(workOrderDetailRepo.findOrderStateCountByWorkProcessDivisionAndOrderState(FILLING, COMPLETION).orElse(0L));
        response.setFillingProductionAmount(workOrderDetailRepo.findProductionAmountByWorkProcessDivision(FILLING));

        // 캡조립
        response.setCapAssemblyOngoingAmount(workOrderDetailRepo.findOrderStateCountByWorkProcessDivisionAndOrderState(CAP_ASSEMBLY, ONGOING).orElse(0L));
        response.setCapAssemblyCompletionAmount(workOrderDetailRepo.findOrderStateCountByWorkProcessDivisionAndOrderState(CAP_ASSEMBLY, COMPLETION).orElse(0L));
        response.setCapAssemblyProductionAmount(workOrderDetailRepo.findProductionAmountByWorkProcessDivision(CAP_ASSEMBLY));

        // 라벨링
        response.setLabelingOngoingAmount(workOrderDetailRepo.findOrderStateCountByWorkProcessDivisionAndOrderState(LABELING, ONGOING).orElse(0L));
        response.setLabelingCompletionAmount(workOrderDetailRepo.findOrderStateCountByWorkProcessDivisionAndOrderState(LABELING, COMPLETION).orElse(0L));
        response.setLabelingProductionAmount(workOrderDetailRepo.findProductionAmountByWorkProcessDivision(LABELING));

        // 포장
        response.setPackagingOngoingAmount(workOrderDetailRepo.findOrderStateCountByWorkProcessDivisionAndOrderState(PACKAGING, ONGOING).orElse(0L));
        response.setPackagingCompletionAmount(workOrderDetailRepo.findOrderStateCountByWorkProcessDivisionAndOrderState(PACKAGING, COMPLETION).orElse(0L));
        response.setPackagingProductionAmount(workOrderDetailRepo.findProductionAmountByWorkProcessDivision(PACKAGING));

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
    public List<SalesRelatedStatusResponse> getContractSaleRelatedStatus() {
        LocalDate fromDate = localDateHelper.getNowMonthStartDate();
        LocalDate toDate = localDateHelper.getNowMonthEndDate();

        List<SalesRelatedStatusResponse> responses = contractRepo.findSalesRelatedStatusResponseByContractItems(fromDate, toDate);
        for (SalesRelatedStatusResponse r : responses) {
            LocalDate firstWeekToDate = fromDate.plusWeeks(1);
            LocalDate secondWeekFromDate = firstWeekToDate.plusDays(1);
            LocalDate secondWeekToDate = secondWeekFromDate.plusWeeks(1);
            LocalDate thirdWeekFromDate = secondWeekToDate.plusDays(1);
            LocalDate thirdWeekToDate = thirdWeekFromDate.plusWeeks(1);
            LocalDate fourthWeekFromDate = thirdWeekToDate.plusDays(1);

            // 1 ~ 8
            int firstWeekAmount = contractRepo.findWeekAmountByWeekDate(fromDate, firstWeekToDate, r.getItemId()).orElse(0);
            r.setFirstWeekAmount(firstWeekAmount);

            // 9 ~ 16
            int secondWeekAmount = contractRepo.findWeekAmountByWeekDate(secondWeekFromDate, secondWeekToDate, r.getItemId()).orElse(0);
            r.setSecondWeekAmount(secondWeekAmount);

            // 17 ~ 24
            int thirdWeekAmount = contractRepo.findWeekAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, r.getItemId()).orElse(0);
            r.setThirdWeekAmount(thirdWeekAmount);

            // 25 ~ 말일
            int fourthWeekAmount = contractRepo.findWeekAmountByWeekDate(fourthWeekFromDate, toDate, r.getItemId()).orElse(0);
            r.setFourthWeekAmount(fourthWeekAmount);
        }
        return responses;
    }

    // 매출관련현황 - 제품 생산
    @Override
    public List<SalesRelatedStatusResponse> getProductSaleRelatedStatus() {
        LocalDate fromDate = localDateHelper.getNowMonthStartDate();
        LocalDate toDate = localDateHelper.getNowMonthEndDate();

        List<SalesRelatedStatusResponse> responses = lotMasterRepo.findSalesRelatedStatusResponseByProductItems(fromDate, toDate);
        for (SalesRelatedStatusResponse r : responses) {

            LocalDate firstWeekToDate = fromDate.plusWeeks(1);
            LocalDate secondWeekFromDate = firstWeekToDate.plusDays(1);
            LocalDate secondWeekToDate = secondWeekFromDate.plusWeeks(1);
            LocalDate thirdWeekFromDate = secondWeekToDate.plusDays(1);
            LocalDate thirdWeekToDate = thirdWeekFromDate.plusWeeks(1);
            LocalDate fourthWeekFromDate = thirdWeekToDate.plusDays(1);

            // 1 ~ 8
            int firstWeekAmount = lotMasterRepo.findCreatedAmountByWeekDate(fromDate, firstWeekToDate, r.getItemId()).orElse(0);
            r.setFirstWeekAmount(firstWeekAmount);

            // 9 ~ 16
            int secondWeekAmount = lotMasterRepo.findCreatedAmountByWeekDate(secondWeekFromDate, secondWeekToDate, r.getItemId()).orElse(0);
            r.setSecondWeekAmount(secondWeekAmount);

            // 17 ~ 24
            int thirdWeekAmount = lotMasterRepo.findCreatedAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, r.getItemId()).orElse(0);
            r.setThirdWeekAmount(thirdWeekAmount);

            // 25 ~ 말일
            int fourthWeekAmount = lotMasterRepo.findCreatedAmountByWeekDate(fourthWeekFromDate, toDate, r.getItemId()).orElse(0);
            r.setFourthWeekAmount(fourthWeekAmount);
        }
        return responses;
    }

    // 매출관련현황 - 제품출고
    @Override
    public List<SalesRelatedStatusResponse> getShipmentSaleRelatedStatus() {
        LocalDate fromDate = localDateHelper.getNowMonthStartDate();
        LocalDate toDate = localDateHelper.getNowMonthEndDate();

        List<SalesRelatedStatusResponse> responses = shipmentRepo.findSalesRelatedStatusResponseByShipmentItems(fromDate, toDate);

        for (SalesRelatedStatusResponse r : responses) {
            LocalDate firstWeekToDate = fromDate.plusWeeks(1);
            LocalDate secondWeekFromDate = firstWeekToDate.plusDays(1);
            LocalDate secondWeekToDate = secondWeekFromDate.plusWeeks(1);
            LocalDate thirdWeekFromDate = secondWeekToDate.plusDays(1);
            LocalDate thirdWeekToDate = thirdWeekFromDate.plusWeeks(1);
            LocalDate fourthWeekFromDate = thirdWeekToDate.plusDays(1);

            // 1 ~ 8
            int firstWeekAmount = shipmentRepo.findShipmentAmountByWeekDate(fromDate, firstWeekToDate, r.getItemId()).orElse(0);
            r.setFirstWeekAmount(firstWeekAmount);

            // 9 ~ 16
            int secondWeekAmount = shipmentRepo.findShipmentAmountByWeekDate(secondWeekFromDate, secondWeekToDate, r.getItemId()).orElse(0);
            r.setSecondWeekAmount(secondWeekAmount);

            // 17 ~ 24
            int thirdWeekAmount = shipmentRepo.findShipmentAmountByWeekDate(thirdWeekFromDate, thirdWeekToDate, r.getItemId()).orElse(0);
            r.setThirdWeekAmount(thirdWeekAmount);

            // 25 ~ 말일
            int fourthWeekAmount = shipmentRepo.findShipmentAmountByWeekDate(fourthWeekFromDate, toDate, r.getItemId()).orElse(0);
            r.setFourthWeekAmount(fourthWeekAmount);
        }
        return responses;
    }
}
