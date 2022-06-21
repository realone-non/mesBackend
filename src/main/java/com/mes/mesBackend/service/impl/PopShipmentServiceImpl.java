package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.LabelShipmentResponse;
import com.mes.mesBackend.dto.response.PopShipmentResponse;
import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.entity.Shipment;
import com.mes.mesBackend.entity.ShipmentItem;
import com.mes.mesBackend.entity.ShipmentLot;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.repository.LotMasterRepository;
import com.mes.mesBackend.repository.ShipmentLotRepository;
import com.mes.mesBackend.repository.ShipmentRepository;
import com.mes.mesBackend.service.PopShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.mes.mesBackend.entity.enumeration.OrderState.COMPLETION;

@Service
@RequiredArgsConstructor
public class PopShipmentServiceImpl implements PopShipmentService {
    private final ShipmentRepository shipmentRepo;
    private final LotMasterRepository lotMasterRepo;
    private final ShipmentLotRepository shipmentLotRepo;

    // 출하 정보 목록 조회
    @Override
    public List<PopShipmentResponse> getPopShipments(LocalDate fromDate, LocalDate toDate, String clientName, Boolean completionYn) {
        List<PopShipmentResponse> responses = shipmentRepo.findPopShipmentResponseByCondition(fromDate, toDate, clientName, completionYn);

        for (PopShipmentResponse res : responses) {
            // 출하에 해당하는 출하 품목 list
            List<ShipmentItem> shipmentItems = shipmentRepo.findShipmentItemByShipmentId(res.getShipmentId());
            List<String> itemNos = shipmentItems.stream().map(m -> m.getContractItem().getItem().getItemNo()).distinct().collect(Collectors.toList());
            List<String> itemNames = shipmentItems.stream().map(m -> m.getContractItem().getItem().getItemName()).distinct().collect(Collectors.toList());
            int shipmentAmount = shipmentRepo.findShipmentLotShipmentAmountByShipmentId(res.getShipmentId()).stream().mapToInt(Integer::intValue).sum();

            res.setItemName(itemNames);
            res.setItemNo(itemNos);
            res.setShipmentAmount(shipmentAmount);     // 출하 LOT 등록되어 있는 LotMaster 의 shipmentAmount sum
        }
        return responses;
    }

    // 라벨용 출하 정보 목록 조회
    @Override
    public List<LabelShipmentResponse> getLabelShipments(LocalDate fromDate, LocalDate toDate, String clientName, Boolean completionYn){
        List<PopShipmentResponse> responses = shipmentRepo.findPopShipmentResponseByCondition(fromDate, toDate, clientName, completionYn);
        List<LabelShipmentResponse> responseList = new ArrayList<>();

        for (PopShipmentResponse res : responses) {
            // 출하에 해당하는 출하 품목 list
            List<ShipmentItem> shipmentItems = shipmentRepo.findShipmentItemByShipmentId(res.getShipmentId());
            List<String> itemNos = shipmentItems.stream().map(m -> m.getContractItem().getItem().getItemNo()).distinct().collect(Collectors.toList());
            List<String> itemNames = shipmentItems.stream().map(m -> m.getContractItem().getItem().getItemName()).distinct().collect(Collectors.toList());
            int shipmentAmount = shipmentRepo.findShipmentLotShipmentAmountByShipmentId(res.getShipmentId()).stream().mapToInt(Integer::intValue).sum();
            List<LocalDateTime> createdDates = shipmentRepo.findShipmentLotCreatedDateByShipmentId(res.getShipmentId());

            //라벨프린터용 Response 작성
            LabelShipmentResponse labelShipmentResponse = new LabelShipmentResponse();
            labelShipmentResponse.setShipmentId(res.getShipmentId());
            labelShipmentResponse.setBarcodeNumber(res.getBarcodeNumber());
            labelShipmentResponse.setClientName(res.getClientName());
            labelShipmentResponse.setCreatedDate1(LocalDate.from(Collections.min(createdDates)));
            if(createdDates.size() > 1) {
                createdDates.remove(Collections.min(createdDates));
            }
            labelShipmentResponse.setCreatedDate2(LocalDate.from(Collections.min(createdDates)));
            labelShipmentResponse.setItemNo(itemNos);
            labelShipmentResponse.setItemName(itemNames);
            labelShipmentResponse.setShipmentAmount(shipmentAmount);
            labelShipmentResponse.setOrderState(res.getOrderState());
            labelShipmentResponse.setLabelPrintYn(res.isLabelPrintYn());

            responseList.add(labelShipmentResponse);
        }
        return responseList;
    }


    // 출하상태 COMPLETION 으로 변경
    @Override
    public void updateShipmentStateCompletion(Long shipmentId) throws NotFoundException {
        Shipment shipment = getShipmentOrThrow(shipmentId);
        shipment.setOrderState(COMPLETION);
        shipmentRepo.save(shipment);
    }

    // 출하 단일 조회 및 예외
    private Shipment getShipmentOrThrow(Long id) throws NotFoundException {
        return shipmentRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("shipment does not exist. input id: " + id));
    }
}
