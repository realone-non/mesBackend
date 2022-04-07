package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.DevelopmentResponse;
import com.mes.mesBackend.dto.response.DevelopmentStateReponse;
import com.mes.mesBackend.entity.QDevelopment;
import com.mes.mesBackend.entity.QDevelopmentState;
import com.mes.mesBackend.entity.QItem;
import com.mes.mesBackend.entity.QUser;
import com.mes.mesBackend.entity.enumeration.DevelopmentChildrenStatusType;
import com.mes.mesBackend.entity.enumeration.DevelopmentStatusType;
import com.mes.mesBackend.repository.custom.DevelopmentRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class DevelopmentRepositoryImpl implements DevelopmentRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    final QDevelopment development = QDevelopment.development;
    final QDevelopmentState developmentState = QDevelopmentState.developmentState;
    final QUser user = QUser.user;

    //개발품목 전체 조회
    @Transactional(readOnly = true)
    public List<DevelopmentResponse> findDevelopByCondition(
            Long userId,
            LocalDate fromDate,
            LocalDate toDate,
            String itemNoOrItemName,
            DevelopmentStatusType statusType,
            DevelopmentChildrenStatusType childrenStatusType
            ){
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                DevelopmentResponse.class,
                                development.id.as("id"),
                                development.itemNo.as("itemNo"),
                                development.itemNo.as("itemName"),
                                development.businessName.as("businessName"),
                                development.startDate.as("startDate"),
                                development.endDate.as("endDate"),
                                development.deliverAmount.as("deliverAmount"),
                                development.processState.coalesce("").as("processState"),
                                development.user.id.as("userId"),
                                development.user.korName.as("userName"),
                                development.fileUrl.as("fileUrl")
                                )
                            )
                .from(development)
                .leftJoin(developmentState).on(developmentState.development.id.eq(development.id))
                .innerJoin(user).on(user.id.eq(development.user.id))
                .where(
                        isUserIdEq(userId),
                        dateNull(fromDate, toDate),
                        isItemNoOrItemNameToItemNoOrItemName(itemNoOrItemName),
                        statusEq(statusType),
                        childrenStatusEq(childrenStatusType),
                        development.deleteYn.eq(false),
                        development.useYn.eq(true)
                )
                .groupBy(development.id)
                .orderBy(development.createdDate.desc())
                .fetch();
    }

    //개발품목 단건 조회
    @Transactional(readOnly = true)
    public DevelopmentResponse findDevelopByIdAndDeleteYnFalse(Long id){
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                DevelopmentResponse.class,
                                development.id.as("id"),
                                development.itemNo.as("itemNo"),
                                development.itemNo.as("itemName"),
                                development.businessName.as("businessName"),
                                development.startDate.as("startDate"),
                                development.endDate.as("endDate"),
                                development.deliverAmount.as("deliverAmount"),
                                development.processState.coalesce("").as("processState"),
                                development.user.id.as("userId"),
                                development.user.korName.as("userName"),
                                development.fileUrl.as("fileUrl")
                        )
                )
                .from(development)
                .leftJoin(developmentState).on(developmentState.development.id.eq(development.id))
                .innerJoin(user).on(user.id.eq(development.user.id))
                .where(
                        development.id.eq(id),
                        development.deleteYn.eq(false),
                        development.useYn.eq(true)
                )
                .fetchOne();
    }

    //개발품목 상세 전체 조회
    @Transactional(readOnly = true)
    public List<DevelopmentStateReponse> findAllStateByCondition(
            Long developId,
            DevelopmentStatusType developmentStatus,
            DevelopmentChildrenStatusType childrenStatusType
    ){
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                DevelopmentStateReponse.class,
                                developmentState.id.as("id"),
                                developmentState.fileUrl.as("fileUrl"),
                                developmentState.createdDate.as("addDate"),
                                developmentState.user.id.as("userId"),
                                developmentState.user.korName.as("userName"),
                                developmentState.approveDate.as("approveDate"),
                                developmentState.developmentStatus.as("status"),
                                developmentState.developmentChildrenStatus.as("childrenStatus"),
                                developmentState.ver.as("ver"),
                                developmentState.changeContents.as("changeContents"),
                                developmentState.meetingType.as("meetingType"),
                                developmentState.orders.as("orders")
                        )
                )
                .from(developmentState)
                .innerJoin(development).on(development.id.eq(developmentState.development.id))
                .innerJoin(user).on(user.id.eq(developmentState.user.id))
                .where(
                        developmentState.development.id.eq(developId),
                        statusEq(developmentStatus),
                        childrenStatusEq(childrenStatusType),
                        developmentState.deleteYn.eq(false)
                )
                .fetch();
    }

    //개발품목 상세 단건 조회
    @Transactional(readOnly = true)
    public DevelopmentStateReponse findByIdAndDeleteYn(Long developId, Long stateId){
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                DevelopmentStateReponse.class,
                                developmentState.id.as("id"),
                                developmentState.fileUrl.as("fileUrl"),
                                developmentState.createdDate.as("addDate"),
                                developmentState.user.id.as("userId"),
                                developmentState.user.korName.as("userName"),
                                developmentState.approveDate.as("approveDate"),
                                developmentState.developmentStatus.as("status"),
                                developmentState.developmentChildrenStatus.as("childrenStatus"),
                                developmentState.ver.as("ver"),
                                developmentState.changeContents.as("changeContents"),
                                developmentState.meetingType.as("meetingType"),
                                developmentState.orders.as("orders")
                        )
                )
                .from(developmentState)
                .innerJoin(development).on(development.id.eq(developmentState.development.id))
                .innerJoin(user).on(user.id.eq(developmentState.user.id))
                .where(
                        developmentState.development.id.eq(developId),
                        developmentState.id.eq(stateId),
                        developmentState.deleteYn.eq(false)
                )
                .fetchOne();
    }

    // 이름 포함 검색
    private BooleanExpression isUserIdEq(Long userId) {
        return userId != null ? user.id.eq(userId) : null;
    }

    //날짜 검색
    private  BooleanExpression dateNull(LocalDate fromDate, LocalDate toDate){
        return fromDate != null ? development.startDate.between(fromDate, toDate) : null;
    }

    //품번 검색
    private BooleanExpression itemNoContaining(String itemNo){
        return itemNo != null ? development.itemNo.contains(itemNo) : null;
    }

    //품명 검색
    private BooleanExpression itemNameContaining(String itemName){
        return itemName != null ? development.itemName.contains(itemName) : null;
    }

    // 품목|품명으로 검색
    private BooleanExpression isItemNoOrItemNameToItemNoOrItemName(String itemNoOrItemName) {
        return itemNoOrItemName != null ? development.itemNo.contains(itemNoOrItemName)
                .or(development.itemName.contains(itemNoOrItemName)) : null;
    }

    //진행상태(상위)
    private BooleanExpression statusEq(DevelopmentStatusType status){
        return status != null ? developmentState.developmentStatus.eq(status) : null;
    }

    //진행상태(하위)
    private BooleanExpression childrenStatusEq(DevelopmentChildrenStatusType childrenStatus){
        return childrenStatus != null ? developmentState.developmentChildrenStatus.eq(childrenStatus) : null;
    }
}
