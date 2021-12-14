package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.dto.response.ContractItemStateResponse;
import com.mes.mesBackend.entity.QContract;
import com.mes.mesBackend.entity.QContractItem;
import com.mes.mesBackend.entity.enumeration.ContractType;
import com.mes.mesBackend.entity.enumeration.PeriodType;
import com.mes.mesBackend.repository.custom.ContractItemStateRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ContractItemStateRepositoryImpl implements ContractItemStateRepositoryCustom {
    // 검색조건: 거래처 명, 품번|품명, 수주번호, 담당자 명, 기간(수주일자), 수주유형

    private final JPAQueryFactory jpaQueryFactory;

    final QContract contract = QContract.contract;
    final QContractItem contractItem = QContractItem.contractItem;

    @Override
    @Transactional(readOnly = true)
    public List<ContractItemStateResponse> findAllByCondition(
            String clientName,
            String itemNoAndItemName,
            String contractNo,
            String userName,
            PeriodType periodType,
            LocalDate fromDate,
            LocalDate toDate,
            ContractType contractType
    ) {
        return jpaQueryFactory
                .select(
                        Projections.fields(ContractItemStateResponse.class,
                                contract.contractDate.as("contractDate"),
                                contract.contractNo.as("jobNo"),
                                contract.contractNo.as("contractNo"),
                                contract.client.clientCode.as("clientNo"),
                                contract.client.clientName.as("clientName"),
                                contract.clientOrderNo.as("clientOrderNo"),
                                contract.user.korName.as("userName"),
                                contractItem.contractType.as("contractType"),
                                contract.outputWareHouse.wareHouseName.as("outputWareHouse"),
                                contractItem.item.itemNo.as("itemNo"),
                                contractItem.item.itemName.as("itemName"),
                                contractItem.item.standard.as("standard"),
                                contractItem.item.unit.unitCodeName.as("unit"),
                                contractItem.amount.as("amount"),
                                contract.currency.currency.as("currency"),
                                contractItem.item.inputUnitPrice.as("price"),
                                contractItem.item.inputUnitPrice.multiply(contractItem.amount).as("contractAmount"),
                                contractItem.item.inputUnitPrice.multiply(contractItem.amount).as("contractAmountWon"),
                                (contractItem.item.inputUnitPrice.multiply(contractItem.amount).doubleValue()).multiply(0.1).as("surtax"),
                                contract.periodDate.as("periodDate"),
                                contractItem.note.as("note")
                        ))
                .from(contractItem)
                .innerJoin(contractItem.contract, contract)
                .where(
                        isClientNameContain(clientName),
                        isItemNoOrItemNameToItemNoOrItemName(itemNoAndItemName),
                        isContractNoContain(contractNo),
                        isUserNameContain(userName),
                        isPeriodDateBetween(periodType, fromDate, toDate),
                        isContractDateBetween(periodType, fromDate, toDate),
                        isContractTypeEq(contractType),
                        isDeleteYnFalse()
                )
                .fetch();
    }

    // 검색조건: 거래처 명, 품번|품명, 수주번호, 담당자 명, 기간(수주일자), 수주유형
    // 거래처 명
    private BooleanExpression isClientNameContain(String clientName) {
        return clientName != null ? contract.client.clientName.contains(clientName) : null;
    }

    // 품번|품명
    private BooleanExpression isItemNoOrItemNameToItemNoOrItemName(String itemNoOrName) {
        return itemNoOrName != null ? contractItem.item.itemNo.contains(itemNoOrName)
                .or(contractItem.item.itemName.contains(itemNoOrName)) : null;
    }

    // 수주번호
    private BooleanExpression isContractNoContain(String contractNo) {
        return contractNo != null ? contract.contractNo.contains(contractNo) : null;
    }

    // 담당자 명
    private BooleanExpression isUserNameContain(String userName) {
        return userName != null ? contract.user.korName.contains(userName) : null;
    }

    // 기간
    // 수주: CONTRACT_DATE
    // 납기: PERIOD_DATE
    private BooleanExpression isPeriodDateBetween(PeriodType periodType, LocalDate fromDate, LocalDate toDate) {
        if (periodType == null || periodType.equals(PeriodType.CONTRACT_DATE)) {
            return null;
        }
        return contract.periodDate.between(fromDate, toDate);
    }

    private BooleanExpression isContractDateBetween(PeriodType periodType, LocalDate fromDate, LocalDate toDate) {
        if (periodType == null || periodType == PeriodType.PERIOD_DATE) {
            return null;
        }
        return contract.contractDate.between(fromDate, toDate);
    }

    // 수주유형
    private BooleanExpression isContractTypeEq(ContractType contractType) {
        return contractType != null ? contractItem.contractType.eq(contractType) : null;
    }

    private BooleanExpression isDeleteYnFalse() {
        return contract.deleteYn.isFalse().and(contractItem.deleteYn.isFalse());
    }
}
