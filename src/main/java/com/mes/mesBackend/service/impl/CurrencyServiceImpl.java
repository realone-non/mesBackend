package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.CurrencyRequest;
import com.mes.mesBackend.dto.response.CurrencyResponse;
import com.mes.mesBackend.entity.Currency;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.CurrencyRepository;
import com.mes.mesBackend.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    @Autowired
    CurrencyRepository currencyRepository;
    @Autowired
    ModelMapper mapper;

    // 화폐 생성
    @Override
    public CurrencyResponse createCurrency(CurrencyRequest currencyRequest) {
        Currency currency = mapper.toEntity(currencyRequest, Currency.class);
        currencyRepository.save(currency);
        return mapper.toResponse(currency, CurrencyResponse.class);
    }
    // 화폐 단일 조회
    @Override
    public CurrencyResponse getCurrency(Long id) throws NotFoundException {
        Currency currency = getCurrencyOrThrow(id);
        return mapper.toResponse(currency, CurrencyResponse.class);
    }
    // 화폐 리스트 조회
    @Override
    public List<CurrencyResponse> getCurrencies() {
        List<Currency> currencies = currencyRepository.findAllByDeleteYnFalse();
        return mapper.toListResponses(currencies, CurrencyResponse.class);
    }
    // 화폐 수정
    @Override
    public CurrencyResponse updateCurrency(Long id, CurrencyRequest currencyRequest) throws NotFoundException {
        Currency newCurrency = mapper.toEntity(currencyRequest, Currency.class);
        Currency findCurrency = getCurrencyOrThrow(id);
        findCurrency.update(newCurrency);
        currencyRepository.save(findCurrency);
        return mapper.toResponse(findCurrency, CurrencyResponse.class);
    }
    // 화폐 삭제
    @Override
    public void deleteCurrency(Long id) throws NotFoundException {
        Currency currency = getCurrencyOrThrow(id);
        currency.delete();
        currencyRepository.save(currency);
    }
    // 화폐 단일 조회 및 예외
    public Currency getCurrencyOrThrow(Long id) throws NotFoundException {
        return currencyRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("currency does not exist. input id: " + id));
    }
}
