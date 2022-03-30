package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.PopRecycleRequest;
import com.mes.mesBackend.dto.response.PopRecycleCreateResponse;
import com.mes.mesBackend.dto.response.PopRecycleResponse;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;

import java.util.List;

public interface PopRecycleService {

    //재사용 목록 조회
    List<PopRecycleResponse> getRecycles(WorkProcessDivision workProcessDivision) throws NotFoundException;

    //재사용 등록
    PopRecycleCreateResponse createUseRecycle(PopRecycleRequest request) throws NotFoundException, BadRequestException;
}
