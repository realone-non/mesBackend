package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.entity.Shipment;

import java.time.LocalDate;
import java.util.List;

public interface ShipmentRepositoryCustom {
    // 출하 리스트 조회 검색조건 : 거래처 명, 출하기간, 화폐 id, 담당자 명
    List<Shipment> findAllCondition(String clientName, LocalDate fromDate, LocalDate toDate, Long currencyId, String userName);
}
