package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.MaterialStockInspectRequestRequest;
import com.mes.mesBackend.dto.response.MaterialStockInspectRequestResponse;
import com.mes.mesBackend.dto.response.ReceiptAndPaymentResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.AmountHelper;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.*;
import com.mes.mesBackend.service.MaterialWarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialWarehouseServiceImpl implements MaterialWarehouseService {
    @Autowired
    ItemLogRepository itemLogRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    WareHouseRepository wareHouseRepository;
    @Autowired
    ItemAccountRepository itemAccountRepository;
    @Autowired
    MaterialStockInspectRequestRepository materialStockInspectRequestRepository;
    @Autowired
    AmountHelper amountHelper;

    //수불부 조회
    public List<ReceiptAndPaymentResponse> getReceiptAndPaymentList(Long warehouseId, Long itemAccountId, LocalDate fromDate, LocalDate toDate){
        return itemLogRepository.findAllConditionResponse(warehouseId, itemAccountId, fromDate, toDate, false);
    }

    //재고실사의뢰 등록
    public MaterialStockInspectRequestResponse createMaterialStockInspect(MaterialStockInspectRequestRequest request) throws NotFoundException{
        MaterialStockInspectRequest dbStockInspect = modelMapper.toEntity(request, MaterialStockInspectRequest.class);
        ItemAccount itemAccount = itemAccountRepository.findByIdAndDeleteYnFalse(request.getItemAccountId())
                .orElseThrow(() -> new NotFoundException("itemAccount does not exist. input id: " + request.getWarehouseId()));
        WareHouse wareHouse = wareHouseRepository.findByIdAndDeleteYnFalse(request.getWarehouseId())
                .orElseThrow(() -> new NotFoundException("warehouse does not exist. input id: " + request.getWarehouseId()));
        dbStockInspect.setWareHouse(wareHouse);
        dbStockInspect.setItemAccount(itemAccount);
        materialStockInspectRequestRepository.save(dbStockInspect);
        System.out.println(dbStockInspect.getId());
        return materialStockInspectRequestRepository.findByIdAndDeleteYn(dbStockInspect.getId());
    }
    //재고실사의뢰 조회
    public List<MaterialStockInspectRequestResponse> getMaterialStockInspectList(LocalDate fromDate, LocalDate toDate){
        return materialStockInspectRequestRepository.findAllByCondition(fromDate, toDate);
    }
    //재고실사의뢰 단건조회
    public MaterialStockInspectRequestResponse getMaterialStockInspect(Long id) throws NotFoundException{
        return materialStockInspectRequestRepository.findByIdAndDeleteYn(id);
    }
    //재고실사의뢰 수정
    public MaterialStockInspectRequestResponse modifyMaterialStockInspect(Long id, MaterialStockInspectRequestRequest request) throws NotFoundException{
        MaterialStockInspectRequest dbStockRequest = materialStockInspectRequestRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("stockInspect does not exist. input id: " + id));
        WareHouse wareHouse = wareHouseRepository.findByIdAndDeleteYnFalse(request.getWarehouseId())
                .orElseThrow(() -> new NotFoundException("warehouse does not exist. input id: " + request.getWarehouseId()));
        ItemAccount itemAccount = itemAccountRepository.findByIdAndDeleteYnFalse(request.getItemAccountId())
                .orElseThrow(() -> new NotFoundException("itemAccount does not exist. input id: " + request.getItemAccountId()));
        dbStockRequest.update(request.getNote(), wareHouse, itemAccount);
        materialStockInspectRequestRepository.save(dbStockRequest);
        return materialStockInspectRequestRepository.findByIdAndDeleteYn(id());
    }
    //재고실사의뢰 삭제
    public void deleteMaterialStockInspect(Long id) throws NotFoundException{
        MaterialStockInspectRequest dbStockRequest = materialStockInspectRequestRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("stockInspect does not exist. input id: " + id));
        dbStockRequest.delete(id);
    }
}
