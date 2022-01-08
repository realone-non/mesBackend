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
                                bomItem.amount.as("amount"),
                                material.outputRequestAmount.as("outputRequestAmount"),
                                material.outputAmount.as("outputAmount"),
                                request.id.as("requestId")
                        )
                )
                .from(material)
                .leftJoin(request).on(request.id.eq(material.outSourcingProductionRequest.id))
                .leftJoin(bomItem).on(bomItem.id.eq(material.bomItemDetail.id))
                .leftJoin(item).on(item.id.eq(bomItem.item.id))
                .where(
                        material.outSourcingProductionRequest.id.eq(prodId),
                        material.useYn.eq(true),
                        material.deleteYn.eq(false)
                )
                .fetch();
    }

    //외주생산 원재료 출고 대상 정보 단일 조회
    public Optional<OutsourcingMaterialReleaseResponse> findByMaterialId(Long id){
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                Projections.fields(
                                        OutsourcingMaterialReleaseResponse.class,
                                        material.id.as("id"),
                                        item.itemNo.as("itemNo"),
                                        item.itemName.as("itemName"),
                                        bomItem.amount.as("amount"),
                                        material.outputRequestAmount.as("outputRequestAmount"),
                                        material.outputAmount.as("outputAmount"),
                                        request.id.as("requestId")
                                )
                        )
                        .from(material)
                        .leftJoin(request).on(request.id.eq(material.outSourcingProductionRequest.id))
                        .leftJoin(bomItem).on(bomItem.id.eq(material.bomItemDetail.id))
                        .leftJoin(item).on(item.id.eq(bomItem.item.id))
                        .where(
                                material.id.eq(id),
                                material.useYn.eq(true),
                                material.deleteYn.eq(false)
                        )
                        .fetchOne()
        );
    }
}
