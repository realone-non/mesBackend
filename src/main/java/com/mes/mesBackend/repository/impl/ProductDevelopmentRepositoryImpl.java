package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.DevelopmentResponse;
import com.mes.mesBackend.dto.response.DevelopmentStateReponse;
import com.mes.mesBackend.entity.QProductDevelopment;
import com.mes.mesBackend.entity.QProductDevelopmentState;
import com.mes.mesBackend.entity.QUser;
import com.mes.mesBackend.entity.enumeration.DevelopmentChildrenStatusType;
import com.mes.mesBackend.entity.enumeration.DevelopmentStatusType;
import com.mes.mesBackend.repository.custom.ProductDevelopmentRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class ProductDevelopmentRepositoryImpl implements ProductDevelopmentRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    final QProductDevelopment productDevelopment = QProductDevelopment.productDevelopment;
    final QProductDevelopmentState productDevelopmentState = QProductDevelopmentState.productDevelopmentState;
    final QUser user = QUser.user;

    //개발품목 전체 조회
    @Transactional(readOnly = true)
    public List<DevelopmentResponse> findDevelopByCondition(
            Long userId,
            LocalDate fromDate,
            LocalDate toDate,
            String itemNo,
            String itemName,
            DevelopmentStatusType statusType,
            DevelopmentChildrenStatusType childrenStatusType
            ){
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                DevelopmentResponse.class,
                                productDevelopment.id.as("id"),
                                productDevelopment.itemNo.as("itemNo"),
                                productDevelopment.itemNo.as("itemName"),
                                productDevelopment.businessName.as("businessName"),
                                productDevelopment.startDate.as("startDate"),
                                productDevelopment.endDate.as("endDate"),
                                productDevelopment.deliverAmount.as("deliverAmount"),
                                productDevelopmentState.developmentStatus.stringValue().concat("-").concat(productDevelopmentState.developmentChildrenStatus.stringValue()),
                                productDevelopment.user.id.as("userId"),
                                productDevelopment.user.korName.as("userName"),
                                productDevelopment.fileUrl.as("fileUrl")
                                )
                            )
                .from(productDevelopmentState)
                .innerJoin(productDevelopment).on(productDevelopment.id.eq(productDevelopmentState.productDevelopment.id))
                .innerJoin(user).on(user.id.eq(productDevelopment.user.id))
                .where(
//                        isUserIdEq(userId),
//                        dateNull(fromDate, toDate),
//                        itemNoContaining(itemNo),
//                        itemNameContaining(itemName),
//                        statusEq(statusType),
//                        childrenStatusEq(childrenStatusType)
                )
                .fetch();
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
                                productDevelopmentState.id.as("id"),
                                productDevelopmentState.fileUrl.as("fileUrl"),
                                productDevelopmentState.createdDate.as("addDate"),
                                productDevelopmentState.user.id.as("userId"),
                                productDevelopmentState.user.korName.as("userName"),
                                productDevelopmentState.approveDate.as("approveDate"),
                                productDevelopmentState.developmentStatus.as("status"),
                                productDevelopmentState.developmentChildrenStatus.as("childrenStatus"),
                                productDevelopmentState.ver.as("ver"),
                                productDevelopmentState.changeContents.as("changeContents"),
                                productDevelopmentState.meetingType.as("meetingType")
                        )
                )
                .from(productDevelopmentState)
//                .innerJoin(productDevelopment).on(productDevelopment.id.eq(productDevelopmentState.productDevelopment.id))
                .innerJoin(user).on(user.id.eq(productDevelopmentState.user.id))
                .where(
//                        productDevelopmentState.productDevelopment.id.eq(developId)
//                        productDevelopmentState.id.eq(developId),
//                        statusEq(developmentStatus),
//                        childrenStatusEq(childrenStatusType)
                )
                .fetch();
    }

    // 이름 포함 검색
    private BooleanExpression isUserIdEq(Long userId) {
        return userId != null ? user.id.eq(userId) : null;
    }

    //날짜 검색
    private  BooleanExpression dateNull(LocalDate fromDate, LocalDate toDate){
        return fromDate != null ? productDevelopment.startDate.between(fromDate, toDate) : null;
    }

    //품번 검색
    private BooleanExpression itemNoContaining(String itemNo){
        return itemNo != null ? productDevelopment.itemNo.contains(itemNo) : null;
    }

    //품명 검색
    private BooleanExpression itemNameContaining(String itemName){
        return itemName != null ? productDevelopment.itemName.contains(itemName) : null;
    }

    //진행상태(상위)
    private BooleanExpression statusEq(DevelopmentStatusType status){
        return status != null ? productDevelopmentState.developmentStatus.eq(status) : null;
    }

    //진행상태(하위)
    private BooleanExpression childrenStatusEq(DevelopmentChildrenStatusType childrenStatus){
        return childrenStatus != null ? productDevelopmentState.developmentChildrenStatus.eq(childrenStatus) : null;
    }
}
