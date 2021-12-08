package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.ContractItemStateResponse;
import com.mes.mesBackend.entity.enumeration.ContractType;
import com.mes.mesBackend.entity.enumeration.PeriodType;
import com.mes.mesBackend.repository.custom.ContractItemStateRepositoryCustom;
import com.mes.mesBackend.service.ContractItemStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// 4-3. 수주 상태 조회
@Service
public class ContractItemStateServiceImpl implements ContractItemStateService {
    // 검색조건: 거래처 명, 품번|품명, 수주번호, 담당자 명, 기간(수주일자), 수주유형

    @Autowired
    ContractItemStateRepositoryCustom contractItemStateRepositoryCustom;

    @Override
    public List<ContractItemStateResponse> getContractItemStates(
            String clientName,
            String itemNoAndItemName,
            String contractNo,
            String userName,
            PeriodType periodType,
            LocalDate fromDate,
            LocalDate toDate,
            ContractType contractType
    ) {
        return contractItemStateRepositoryCustom.findAllByCondition(clientName, itemNoAndItemName, contractNo, userName, periodType, fromDate, toDate, contractType);
    }
}
