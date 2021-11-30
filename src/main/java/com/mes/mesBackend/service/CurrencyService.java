package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.CurrencyRequest;
import com.mes.mesBackend.dto.response.CurrencyResponse;
import com.mes.mesBackend.entity.Currency;
import com.mes.mesBackend.exception.NotFoundException;

import java.util.List;

public interface CurrencyService {
    // 화폐 생성
    CurrencyResponse createCurrency(CurrencyRequest currencyRequest);
    // 화폐 단일 조회
    CurrencyResponse getCurrency(Long id) throws NotFoundException;
    // 화폐 리스트 조회
    List<CurrencyResponse> getCurrencies();
    // 화폐 수정
    CurrencyResponse updateCurrency(Long id, CurrencyRequest currencyRequest) throws NotFoundException;
    // 화폐 삭제
    void deleteCurrency(Long id) throws NotFoundException;
    // 화폐 단일 조회 및 예외
    Currency getCurrencyOrThrow(Long id) throws NotFoundException;
}
