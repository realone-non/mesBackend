package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.DevelopmentResponse;
import com.mes.mesBackend.dto.response.DevelopmentStateReponse;
import com.mes.mesBackend.entity.enumeration.DevelopmentChildrenStatusType;
import com.mes.mesBackend.entity.enumeration.DevelopmentStatusType;

import java.time.LocalDate;
import java.util.List;

public interface ProductDevelopmentRepositoryCustom {
    //개발품목 전체 조회
    List<DevelopmentResponse> findDevelopByCondition(
            Long userId,
            LocalDate fromDate,
            LocalDate toDate,
            String itemNo,
            String itemName,
            DevelopmentStatusType status,
            DevelopmentChildrenStatusType childrenStatus);

    //개발품목 진행 상태 조회
    List<DevelopmentStateReponse> findAllStateByCondition(
            Long developId,
            DevelopmentStatusType developmentStatus,
            DevelopmentChildrenStatusType childrenStatusType
    );
}
