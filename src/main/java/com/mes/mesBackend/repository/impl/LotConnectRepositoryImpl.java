package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.entity.LotConnect;
import com.mes.mesBackend.entity.QItem;
import com.mes.mesBackend.entity.QLotConnect;
import com.mes.mesBackend.entity.QLotMaster;
import com.mes.mesBackend.repository.custom.LotConnectRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class LotConnectRepositoryImpl implements LotConnectRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    final QLotConnect lotConnect = QLotConnect.lotConnect;
    final QLotMaster lotMaster = QLotMaster.lotMaster;
    final QItem item = QItem.item;

    // 부모 lotMaster 랑 같고, 자식 lotMaster 의 item 이 파라미터 itemId 와 같고, 구분값이 EXHAUST 인것 조회
    @Override
    public List<LotConnect> findLotConnectsByItemIdOfChildLotMasterEqAndDivisionExhaust(Long itemIdOfChildLotMaster) {
        return jpaQueryFactory
                .select(lotConnect)
                .from(lotConnect)
                .leftJoin(lotMaster).on(lotMaster.id.eq(lotConnect.childLot.id))
                .leftJoin(item).on(item.id.eq(lotMaster.item.id))
                .where(
                        item.id.eq(itemIdOfChildLotMaster)
                )
                .fetch();
    }
}
