package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.LotMasterRequest;
import com.mes.mesBackend.dto.response.LotMasterResponse;
import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.entity.enumeration.EnrollmentType;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;

import java.util.List;

public interface LotMasterService {
    // lot 생성(반환값 String)
    String createLotMaster(LotMasterRequest lotMasterRequest) throws NotFoundException, BadRequestException;

    // LOT 마스터 조회, 검색조건: 품목그룹 id, LOT 번호, 품번|품명, 창고 id, 등록유형, 재고유무, LOT 유형, 검사중여부, 유효여부
    List<LotMasterResponse> getLotMasters(
            Long itemGroupId,
            String lotNo,
            String itemNoAndItemName,
            Long wareHouseId,
            EnrollmentType enrollmentType,
            Boolean stockYn,
            Long lotTypeId,
            Boolean testingYn
    );
    // lot master 단일 조회 및 예외
    LotMaster getLotMasterOrThrow(Long id) throws NotFoundException;

    //테스트용 당일 재고 생성
    void getItemStock();
}
