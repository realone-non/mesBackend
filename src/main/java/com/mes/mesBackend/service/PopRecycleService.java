package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.PopRecycleResponse;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;

import java.util.List;

public interface PopRecycleService {

    //재사용 목록 조회
    List<PopRecycleResponse> getRecycles(Long workProcessId);

    //재사용 유형 목록

}
