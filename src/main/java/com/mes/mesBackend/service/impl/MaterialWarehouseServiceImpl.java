package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.ReceiptAndPaymentResponse;
import com.mes.mesBackend.entity.ItemLog;
import com.mes.mesBackend.helper.AmountHelper;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.ItemLogRepository;
import com.mes.mesBackend.repository.ItemRepository;
import com.mes.mesBackend.repository.WareHouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MaterialWarehouseServiceImpl {
    @Autowired
    ItemLogRepository itemLogRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    WareHouseRepository wareHouseRepository;
    @Autowired
    AmountHelper amountHelper;

    public List<ReceiptAndPaymentResponse> getReceiptAndPaymentList(Long warehouseId, Long itemAccountId, LocalDate fromDate, LocalDate toDate){
        return itemLogRepository.findAllConditionResponse(warehouseId, itemAccountId, fromDate, toDate, false);
    }
}
