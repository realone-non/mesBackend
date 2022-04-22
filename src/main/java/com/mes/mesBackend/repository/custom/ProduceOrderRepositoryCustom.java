package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.ProduceOrderDetailResponse;
import com.mes.mesBackend.dto.response.ProduceOrderResponse;
import com.mes.mesBackend.dto.response.ProductionPerformanceResponse;
import com.mes.mesBackend.entity.ProduceOrder;
import com.mes.mesBackend.entity.enumeration.OrderState;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProduceOrderRepositoryCustom {
    // 제조 오더 리스트 조회, 검색조건 : 품목그룹 id, 품명|품번, 지시상태, 제조오더번호, 수주번호, 착수예정일 fromDate~toDate, 자재납기일자(보류)
    List<ProduceOrderResponse> findAllByCondition(
            Long itemGroupId,
            String itemNoAndName,
            OrderState orderState,
            String produceOrderNo,
            String contractNo,
            LocalDate fromDate,
            LocalDate toDate
    );
    // 제조오더 response 단일 조회
    Optional<ProduceOrderResponse> findResponseByProduceOrderId(Long id);

    // 제조 오더 디테일 리스트 조회
    List<ProduceOrderDetailResponse> findAllProduceOrderDetail(Long itemId);

    //제조 오더 조회(Shortage)
    ProduceOrder findByIdforShortage(Long id);

    // 생산실적 조회
    List<ProductionPerformanceResponse> findProductionPerformanceResponseByCondition(
            LocalDate fromDate,
            LocalDate toDate,
            Long itemGroupId,
            String itemNoOrItemName
    );
}
