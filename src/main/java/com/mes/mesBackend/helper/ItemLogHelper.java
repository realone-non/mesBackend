package com.mes.mesBackend.helper;

import com.mes.mesBackend.dto.response.MaterialStockReponse;
import com.mes.mesBackend.entity.Item;
import com.mes.mesBackend.entity.ItemLog;
import com.mes.mesBackend.entity.WareHouse;
import com.mes.mesBackend.repository.ItemLogRepository;
import com.mes.mesBackend.repository.ItemRepository;
import com.mes.mesBackend.repository.LotMasterRepository;
import com.mes.mesBackend.repository.WareHouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ItemLogHelper {
    @Autowired
    LotMasterRepository lotMasterRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemLogRepository itemLogRepository;
    @Autowired
    WareHouseRepository wareHouseRepository;

    @Scheduled(cron = "0 0 23 * * ?")
    public void getItemLog(){
        List<Item> itemList = itemRepository.findAllByCondition(null, null, null, null, null);
        for (Item item:itemList) {
            List<MaterialStockReponse> stockList = lotMasterRepository.findStockAmountByItemId(item.getId(), null);
            for (MaterialStockReponse response:stockList) {
                ItemLog itemLog = new ItemLog();
                WareHouse wareHouse = wareHouseRepository.findByIdAndDeleteYnFalse(response.getWarehouseId())
                        .orElseThrow(() -> new IllegalArgumentException("not found data"));
                ItemLog todayLog = itemLogRepository.findByItemIdAndWareHouseAndBeforeDay(item.getId(), wareHouse.getId(), LocalDate.now());
                if(todayLog != null){
                    continue;
                }
                itemLog.setWareHouse(wareHouse);
                itemLog.setStockAmount(response.getAmount());
                itemLog.setLogDate(LocalDate.now());
                LocalDate beforeDay = LocalDate.now().minusDays(1);
                ItemLog beforeDayLog = itemLogRepository.findByItemIdAndWareHouseAndBeforeDay(item.getId(), wareHouse.getId(), beforeDay);
                if(beforeDayLog != null){
                    itemLog.setBeforeDayStockAmount(beforeDayLog.getStockAmount());
                }
                itemLogRepository.save(itemLog);
            }
        }
    }
}
