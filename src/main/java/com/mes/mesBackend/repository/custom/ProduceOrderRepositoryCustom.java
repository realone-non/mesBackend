package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.ProduceOrderDetailResponse;
import com.mes.mesBackend.entity.ProduceOrder;
import com.mes.mesBackend.entity.enumeration.InstructionStatus;

import java.time.LocalDate;
import java.util.List;

public interface ProduceOrderRepositoryCustom {
    // 제조 오더 리스트 조회, 검색조건 : 품목그룹 id, 품명|품번, 지시상태, 제조오더번호, 수주번호, 착수예정일 fromDate~toDate, 자재납기일자(보류)
    List<ProduceOrder> findAllByCondition(Long itemGroupId,
                                          String itemNoAndName,
                                          InstructionStatus instructionStatus,
                                          String produceOrderNo,
                                          String contractNo,
                                          LocalDate fromDate,
                                          LocalDate toDate);

    // 제조 오더 디테일 리스트 조회
    List<ProduceOrderDetailResponse> findAllProduceOrderDetail(Long produceOrderId);
}
