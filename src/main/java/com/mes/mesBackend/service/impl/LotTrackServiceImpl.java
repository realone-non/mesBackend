package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.LotTrackingResponse;
import com.mes.mesBackend.repository.LotMasterRepository;
import com.mes.mesBackend.service.LotTrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// 7-2. LOT Tracking
@Service
@RequiredArgsConstructor
public class LotTrackServiceImpl implements LotTrackService {
    private final LotMasterRepository lotMasterRepo;


    @Override
    public List<LotTrackingResponse> getTrackings(String lotNo, Boolean trackingType, String itemNoAndItemName) {
        return null;
    }
}
