package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.LotMasterRequest;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;

public interface LotMasterService {
    // lot 생성
    String createLotMaster(LotMasterRequest lotMasterRequest) throws NotFoundException, BadRequestException;
}
