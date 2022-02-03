package com.mes.mesBackend.helper;

import com.mes.mesBackend.dto.request.LotMasterRequest;
import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;

public interface LotHelper {
    //LOT마스터 생성
    LotMaster createLotMaster(LotMasterRequest lotMasterRequest) throws NotFoundException, BadRequestException;

    String createLotNo(Long itemId, Long equipmentId, Long deleteId) throws BadRequestException;
}
