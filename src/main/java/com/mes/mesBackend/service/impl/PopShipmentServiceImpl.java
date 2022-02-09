package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.PopShipmentResponse;
import com.mes.mesBackend.entity.Shipment;
import com.mes.mesBackend.entity.ShipmentItem;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.repository.ShipmentRepository;
import com.mes.mesBackend.service.PopShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.mes.mesBackend.entity.enumeration.OrderState.COMPLETION;

@Service
@RequiredArgsConstructor
public class PopShipmentServiceImpl implements PopShipmentService {
    private final ShipmentRepository shipmentRepo;

    // 출하 정보 목록 조회
    @Override
    public List<PopShipmentResponse> getPopShipments(LocalDate fromDate, LocalDate toDate, String clientName, Boolean completionYn) {
        List<PopShipmentResponse> responses = shipmentRepo.findPopShipmentResponseByCondition(fromDate, toDate, clientName, completionYn);

        for (PopShipmentResponse res : responses) {
            List<ShipmentItem> shipmentItems = shipmentRepo.findShipmentItemByShipmentId(res.getShipmentId());

            List<String> itemNos = shipmentItems.stream().map(m -> m.getContractItem().getItem().getItemNo()).collect(Collectors.toList());
            List<String> itemNames = shipmentItems.stream().map(m -> m.getContractItem().getItem().getItemName()).collect(Collectors.toList());
            int stockAmount = shipmentRepo.findShipmentLotStockAmountByShipmentId(res.getShipmentId()).stream().mapToInt(Integer::intValue).sum();

            res.setItemName(itemNames);
            res.setItemNo(itemNos);
            res.setAmount(stockAmount);
        }
        return responses;
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
