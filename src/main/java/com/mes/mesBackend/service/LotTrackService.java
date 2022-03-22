package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.LotTrackingResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;

import java.util.List;

// 7-2. LOT Tracking
public interface LotTrackService {
    // Lot Tracking
    // 검색조건: LOT 번호, 추적유형, 품명|품번
    List<LotTrackingResponse> getTrackings(String lotNo, boolean trackingType, String itemNoAndItemName) throws NotFoundException, BadRequestException;
}
