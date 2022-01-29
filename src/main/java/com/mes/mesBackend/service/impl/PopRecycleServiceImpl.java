package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.PopRecycleResponse;
import com.mes.mesBackend.repository.LotMasterRepository;
import com.mes.mesBackend.service.PopRecycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PopRecycleServiceImpl implements PopRecycleService {

    @Autowired
    LotMasterRepository lotMasterRepository;
    //재사용 목록 조회
    public List<PopRecycleResponse> getRecycles(Long workProcessId){
        return lotMasterRepository.findBadAmountByWorkProcess(workProcessId);
    }
}
