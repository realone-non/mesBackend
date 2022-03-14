package com.mes.mesBackend.helper;

import com.mes.mesBackend.dto.request.LotMasterRequest;
import com.mes.mesBackend.entity.Equipment;
import com.mes.mesBackend.entity.Item;
import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.entity.enumeration.LotMasterDivision;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;

public interface LotHelper {
    //LOT마스터 생성
    LotMaster createLotMaster(LotMasterRequest lotMasterRequest) throws NotFoundException, BadRequestException;
    // LOT NO 생성
    String createLotNo(Item item, Equipment equipment, LotMasterDivision lotMasterDivision) throws BadRequestException;
}
