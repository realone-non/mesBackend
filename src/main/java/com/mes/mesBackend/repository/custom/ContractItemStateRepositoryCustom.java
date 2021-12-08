package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.ContractItemStateResponse;
import com.mes.mesBackend.entity.enumeration.ContractType;
import com.mes.mesBackend.entity.enumeration.PeriodType;

import java.time.LocalDate;
import java.util.List;

public interface ContractItemStateRepositoryCustom {
    List<ContractItemStateResponse> findAllByCondition(
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
