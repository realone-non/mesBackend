package com.mes.mesBackend.helper;

import com.mes.mesBackend.entity.Item;
import com.mes.mesBackend.entity.ItemLog;
import com.mes.mesBackend.entity.WareHouse;
import com.mes.mesBackend.entity.enumeration.ItemLogType;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.repository.ItemLogRepository;
import com.mes.mesBackend.repository.ItemRepository;
import com.mes.mesBackend.repository.WareHouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AmountHelper {
    @Autowired
    ItemLogRepository itemLogRepository;
    @Autowired
    WareHouseRepository wareHouseRepository;
    @Autowired
    ItemRepository itemRepository;

    public void amountUpdate(Long itemId, Long fromWarehouseId, Long toWareHouseId, ItemLogType type, int amount, boolean isOut) throws NotFoundException{
        ItemLog fromItemLog = itemLogRepository.findByItemIdAndwareHouseIdAndOutsourcingYn(itemId, fromWarehouseId, isOut);
        ItemLog toItemLog = new ItemLog();
        LocalDate today = LocalDate.now();
        if(toWareHouseId != null){
             toItemLog = itemLogRepository.findByItemIdAndwareHouseIdAndOutsourcingYn(itemId, toWareHouseId, isOut);
        }
        Item item = itemRepository.findByIdAndDeleteYnFalse(itemId).orElseThrow(()-> new NotFoundException("itemInfo not in db:" + itemId));
        WareHouse fromWarehouse = wareHouseRepository.findByIdAndDeleteYnFalse(fromWarehouseId).orElseThrow(()-> new NotFoundException("fromwarehouseInfo not in db:" + fromWarehouseId));
        WareHouse toWarehouse = wareHouseRepository.findByIdAndDeleteYnFalse(fromWarehouseId).orElseThrow(()-> new NotFoundException("fromwarehouseInfo not in db:" + fromWarehouseId));

        if(fromItemLog != null && type != ItemLogType.MOVE_AMOUNT){
            fromItemLog.update(amount, type);
            itemLogRepository.save(fromItemLog);
        }
        else if(toItemLog != null && type.equals(ItemLogType.MOVE_AMOUNT)){
            toItemLog.update(amount, ItemLogType.STORE_AMOUNT);
            itemLogRepository.save(toItemLog);
            fromItemLog.update(amount, type);
            itemLogRepository.save(fromItemLog);
        }
        else if(toItemLog == null && type.equals(ItemLogType.MOVE_AMOUNT)){
            toItemLog.setItem(item);
            toItemLog.setWareHouse(toWarehouse);
            toItemLog.setStoreAmount(amount);
            toItemLog.setOutsourcingYn(isOut);
            toItemLog.setStockAmount(amount);
            toItemLog.setLogDate(today);
            itemLogRepository.save(toItemLog);
            fromItemLog.update(amount, type);
            itemLogRepository.save(fromItemLog);
        }
        else if(fromItemLog == null){
            fromItemLog = new ItemLog();
            fromItemLog.setItem(item);
            fromItemLog.setWareHouse(fromWarehouse);
            fromItemLog.setStoreAmount(amount);
            fromItemLog.setOutsourcingYn(isOut);
            fromItemLog.setStockAmount(amount);
            fromItemLog.update(amount, type);
            fromItemLog.setLogDate(today);
            System.out.println(LocalDate.now());
            itemLogRepository.save(fromItemLog);
        }
        else{
            fromItemLog.update(amount, type);
            itemLogRepository.save(fromItemLog);
        }
    }
}
