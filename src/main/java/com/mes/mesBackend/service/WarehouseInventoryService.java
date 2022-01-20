package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.WarehouseInventoryResponse;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//창고재고
public interface WarehouseInventoryService {
    //창고별 재고 조회 //검색 조건 품목그룹, 품목, 창고, 품목 계정
    List<WarehouseInventoryResponse> getWarehouseInvetoryList(Long warehouseId, Long itemId, Long itemAccountId, Long itemGroupId) throws NotFoundException;
}
