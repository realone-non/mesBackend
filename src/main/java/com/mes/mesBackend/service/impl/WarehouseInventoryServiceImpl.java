package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.WarehouseInventoryResponse;
import com.mes.mesBackend.entity.Item;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.repository.ItemLogRepository;
import com.mes.mesBackend.repository.ItemRepository;
import com.mes.mesBackend.service.WarehouseInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//창고 재고 조회
@Service
@RequiredArgsConstructor
public class WarehouseInventoryServiceImpl implements WarehouseInventoryService {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemLogRepository itemLogRepository;

    public List<WarehouseInventoryResponse> getWarehouseInvetoryList(Long warehouseId, Long itemId, Long itemAccountId, Long itemGroupId) throws NotFoundException {
        List<WarehouseInventoryResponse> responseList = new ArrayList<>();
        if(itemId != null){
            //responseList = itemLogRepository.findByItemIdAndWarehouseIdGroupBy(itemId, warehouseId);
        }
        else{
            List<Item> itemList = itemRepository.findAllByCondition(itemGroupId, itemAccountId, null, null);
            for (Item item:itemList) {
                //List<WarehouseInventoryResponse> response = itemLogRepository.findByItemIdAndWarehouseIdGroupBy(item.getId(), warehouseId);
                //responseList.addAll(response);
            }
        }

        return responseList;
    }
}
