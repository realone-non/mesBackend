package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.ContractItemStateResponse;
import com.mes.mesBackend.entity.enumeration.ContractType;
import com.mes.mesBackend.entity.enumeration.PeriodType;

import java.time.LocalDate;
import java.util.List;

// 4-3. 수주 상태 조회
public interface ContractItemStateService {
    // 검색조건: 거래처 명, 품번|품명, 수주번호, 담당자 명, 기간(수주일자), 수주유형
    List<ContractItemStateResponse> getContractItemStates(
            String clientName,
            String itemNoAndItemName,
            String contractNo,
            String userName,
            PeriodType periodType,
            LocalDate fromDate,
            LocalDate toDate,
            ContractType contractType
    );
}
