package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.entity.LotLog;

import java.util.List;
import java.util.Optional;

public interface LotLogRepositoryCustom {
    // 작업지시 id 로 createdDate 가 제일 최근인 workProcess 를 가져옴
    Optional<LotLog> findWorkProcessNameByWorkOrderId(Long workOrderId);
    // 작업지시에 해당하는 모든 불량수량 가져옴
    List<Integer> findBadItemAmountByWorkOrderId(Long workOrderId);
    // 작업지시에 해당하는 모든 생성수량 가져옴
    List<Integer> findCreatedAmountByWorkOrderId(Long workOrderId);
    // 작업지시에 해당하는 lotMaster Id 모두 가져옴
    List<Long> findLotMasterIdByWorkOrderId(Long workOrderId);
}
