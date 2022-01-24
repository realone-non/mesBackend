package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.ShipmentReturnRequest;
import com.mes.mesBackend.dto.response.ShipmentReturnResponse;
import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.entity.ShipmentLot;
import com.mes.mesBackend.entity.ShipmentReturn;
import com.mes.mesBackend.entity.WareHouse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.*;
import com.mes.mesBackend.service.ShipmentReturnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// 4-6. 출하반품 등록
@Service
@RequiredArgsConstructor
public class ShipmentReturnServiceImpl implements ShipmentReturnService {
    private final ShipmentReturnRepository shipmentReturnRepo;
    private final ShipmentLotRepository shipmentLotRepo;
    private final LotMasterRepository lotMasterRepo;
    private final ModelMapper mapper;
    private final WareHouseRepository wareHouseRepository;

    // 출하반품 생성
    @Override
    public ShipmentReturnResponse createShipmentReturn(ShipmentReturnRequest shipmentReturnRequest) throws NotFoundException, BadRequestException {
        ShipmentLot shipmentLot = getShipmentLotOrThrow(shipmentReturnRequest.getShipmentLotId());
        LotMaster lotMaster = shipmentLot.getLotMaster();
        int returnAmount = shipmentReturnRequest.getReturnAmount();

        // 입력받은 반품수량이 lotMaster 의 출하수량보다 크면 예외
//        int findReturnAmountSum = shipmentReturnRepo.findReturnAmountsByShipmentLotId(shipmentLot.getId()).stream().mapToInt(Integer::intValue).sum();
        int stockAmount = lotMaster.getStockAmount();
        throwIfReturnAmountGreaterThanLotMasterShipment(returnAmount + stockAmount, lotMaster.getShipmentAmount());

        WareHouse wareHouse = getWareHouseOrThrow(shipmentReturnRequest.getInputWarehouseId());
        ShipmentReturn shipmentReturn = mapper.toEntity(shipmentReturnRequest, ShipmentReturn.class);
        shipmentReturn.setWareHouse(wareHouse);
        shipmentReturn.setShipmentLot(shipmentLot);
        shipmentReturnRepo.save(shipmentReturn);
        lotMaster.setStockAmount(lotMaster.getStockAmount() + returnAmount);     // LotMaster 재고수량 변경
        lotMasterRepo.save(lotMaster);
        return getShipmentReturnResponseOrThrow(shipmentReturn.getId());
    }

    // 출하반품 리스트 검색 조회, 검색조건: 거래처 id, 품번|품명, 반품기간 fromDate~toDate
    @Override
    public List<ShipmentReturnResponse> getShipmentReturns(Long clientId, String itemNoAndItemName, LocalDate fromDate, LocalDate toDate) {
        return shipmentReturnRepo.findShipmentReturnResponsesByCondition(clientId, itemNoAndItemName, fromDate, toDate);
    }

    // 출하반품 수정
    @Override
    public ShipmentReturnResponse updateShipmentReturn(Long id, ShipmentReturnRequest shipmentReturnRequest) throws NotFoundException, BadRequestException {
        ShipmentReturn findShipmentReturn = getShipmentReturnOrThrow(id);

        int newReturnAmount = shipmentReturnRequest.getReturnAmount();
        LotMaster lotMaster = findShipmentReturn.getShipmentLot().getLotMaster();

        // 입력받은 반품수량이 lotMaster 의 출하수량보다 크면 예외
//        int findReturnAmountSum = shipmentReturnRepo.findReturnAmountsByShipmentLotId(findShipmentReturn.getShipmentLot().getId()).stream().mapToInt(Integer::intValue).sum();
        int stockAmount = lotMaster.getStockAmount();
        int updateBeforeAmount = stockAmount - findShipmentReturn.getReturnAmount();

        throwIfReturnAmountGreaterThanLotMasterShipment(updateBeforeAmount + newReturnAmount, lotMaster.getShipmentAmount());

        WareHouse newWarehouse = getWareHouseOrThrow(shipmentReturnRequest.getInputWarehouseId());
        ShipmentReturn newShipmentReturn = mapper.toEntity(shipmentReturnRequest, ShipmentReturn.class);

        findShipmentReturn.update(newShipmentReturn, newWarehouse);
        lotMaster.setStockAmount(updateBeforeAmount + newReturnAmount);
        lotMasterRepo.save(lotMaster);
        shipmentReturnRepo.save(findShipmentReturn);
        return getShipmentReturnResponseOrThrow(id);
    }

    // 출하반품 삭제
    @Override
    public void deleteShipmentReturn(Long id) throws NotFoundException {
        ShipmentReturn shipmentReturn = getShipmentReturnOrThrow(id);
        shipmentReturn.delete();
        LotMaster lotMaster = shipmentReturn.getShipmentLot().getLotMaster();
        lotMaster.setStockAmount(lotMaster.getStockAmount() - shipmentReturn.getReturnAmount());
        shipmentReturnRepo.save(shipmentReturn);
        lotMasterRepo.save(lotMaster);
    }

    // 출하 반품 단일 조회 및 예외
    private ShipmentReturn getShipmentReturnOrThrow(Long id) throws NotFoundException {
        return shipmentReturnRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("shipmentReturn does not exist. id: " + id));
    }

    // 출하 반품 Response 단일 조회 및 예외
    private ShipmentReturnResponse getShipmentReturnResponseOrThrow(Long id) throws NotFoundException {
        return shipmentReturnRepo.findShipmentReturnResponseByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("shipmentReturn does not exist. id: " + id));
    }

    // 출하 LOT 단일 조회 및 예외
    private ShipmentLot getShipmentLotOrThrow(Long id) throws NotFoundException {
        return shipmentLotRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("shipmentLot does not exist. id: " + id));
    }

    // 창고 단일 조회 및 예외
    private WareHouse getWareHouseOrThrow(Long id) throws NotFoundException {
        return wareHouseRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("wareHouse does not exists. input id: " + id));
    }

    // 입력받은 반품수량이 lotMaster 의 출하수량보다 크면 예외
    private void throwIfReturnAmountGreaterThanLotMasterShipment(int returnAmount, int shipmentAmount) throws BadRequestException {
        if (returnAmount > shipmentAmount) {
            throw new BadRequestException("입력한 반품수량은 출하된 출하수량보다 클 수 없습니다. 출하수량: " + shipmentAmount);
        }
    }
}
