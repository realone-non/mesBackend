package com.mes.mesBackend.repository.impl;


import com.mes.mesBackend.dto.response.OutsourcingMaterialReleaseResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.repository.custom.OutsourcingProductionRawMaterialOutputInfoRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;


import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OutSourcingProductionRawMaterialOutputInfoRepositoryImpl implements OutsourcingProductionRawMaterialOutputInfoRepositoryCustom {
    // 검색조건: 외주처 명, 생산품목, 의뢰기간

    private final JPAQueryFactory jpaQueryFactory;
    final QOutSourcingProductionRawMaterialOutputInfo material = QOutSourcingProductionRawMaterialOutputInfo.outSourcingProductionRawMaterialOutputInfo;
    final QOutSourcingProductionRequest request = QOutSourcingProductionRequest.outSourcingProductionRequest;
    final QBomItemDetail bomItem = QBomItemDetail.bomItemDetail;
    final QItem item = QItem.item;

    //외주생산 원재료 출고 대상 정보 리스트 조회
    public List<OutsourcingMaterialReleaseResponse> findAllUseYn(Long prodId){
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                OutsourcingMaterialReleaseResponse.class,
                                material.id.as("id"),
                                item.id.as("itemId"),
                                item.itemNo.as("itemNo"),
                                item.itemName.as("itemName"),
                                material.outputRequestAmount.as("outputRequestAmount"),
                                material.outputAmount.as("outputAmount"),
                                request.id.as("requestId"),
                                request.item.id.as("requestItemId")
                        )
                )
                .from(material)
                .leftJoin(item).on(item.id.eq(material.item.id))
                .leftJoin(request).on(request.id.eq(material.outSourcingProductionRequest.id))
                .where(
                        request.id.eq(prodId),
                        material.deleteYn.isFalse()
                )
                .fetch();
    }

    //외주생산 원재료 출고 대상 정보 단일 조회
    public Optional<OutsourcingMaterialReleaseResponse> findByMaterialId(Long prodId, Long id){
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                Projections.fields(
                                        OutsourcingMaterialReleaseResponse.class,
                                        material.id.as("id"),
                                        item.id.as("itemId"),
                                        item.itemNo.as("itemNo"),
                                        item.itemName.as("itemName"),
                                        material.outputRequestAmount.as("outputRequestAmount"),
                                        material.outputAmount.as("outputAmount"),
                                        request.id.as("requestId"),
                                        request.item.id.as("requestItemId")
                                )
                        )
                        .from(material)
                        .leftJoin(item).on(item.id.eq(material.item.id))
                        .leftJoin(request).on(request.id.eq(material.outSourcingProductionRequest.id))
                        .where(
                                request.id.eq(prodId),
                                material.id.eq(id),
                                material.deleteYn.isFalse()
                        )
                        .fetchOne()
        );
    }

    // 외주생산의뢰에 해당하는 원재료출고대상 정보가 존재하는지 여부
    @Override
    public boolean existsByRequestId(Long requestId) {
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(material)
                .where(
                        material.outSourcingProductionRequest.id.eq(requestId),
                        material.deleteYn.isFalse()
                )
                .fetchFirst();

        return fetchOne != null;
    }
}
