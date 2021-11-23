package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.EstimateItemRequest;
import com.mes.mesBackend.dto.request.EstimateRequest;
import com.mes.mesBackend.dto.response.EstimateItemResponse;
import com.mes.mesBackend.dto.response.EstimateResponse;
import com.mes.mesBackend.entity.Client;
import com.mes.mesBackend.entity.Currency;
import com.mes.mesBackend.entity.Estimate;
import com.mes.mesBackend.entity.EstimateItemDetail;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.EstimateItemDetailRepository;
import com.mes.mesBackend.repository.EstimateRepository;
import com.mes.mesBackend.service.ClientService;
import com.mes.mesBackend.service.CurrencyService;
import com.mes.mesBackend.service.EstimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EstimateServiceImpl implements EstimateService {
    @Autowired
    EstimateRepository estimateRepository;
    @Autowired
    ClientService clientService;
    @Autowired
    CurrencyService currencyService;
    @Autowired
    ModelMapper mapper;
    @Autowired
    EstimateItemDetailRepository estimateItemRepository;

    // 견적 생성
    @Override
    public EstimateResponse createEstimate(EstimateRequest estimateRequest) throws NotFoundException {
        Client client = clientService.getClientOrThrow(estimateRequest.getClient());
        Currency currency = currencyService.getCurrencyOrThrow(estimateRequest.getCurrency());
        Estimate estimate = mapper.toEntity(estimateRequest, Estimate.class);
        String estimateNo = createEstimateNo();
        estimate.addMapping(client, currency, estimateNo);
        estimateRepository.save(estimate);
        return mapper.toResponse(estimate, EstimateResponse.class);
    }

    // 견적 단일 조회
    @Override
    public EstimateResponse getEstimate(Long estimateId) throws NotFoundException {
        Estimate estimate = getEstimateOrThrow(estimateId);
        return mapper.toResponse(estimate, EstimateResponse.class);
    }

    // 견적 페이징 조회 검색조건: 거래처, 견적기간(fromDate~toDate), 화폐, 담당자
    @Override
    public Page<EstimateResponse> getEstimates(
            String clientName,
            LocalDate fromDate,
            LocalDate toDate,
            Long currencyId,
            String chargeName,
            Pageable pageable
    ) {
        Page<Estimate> estimates = estimateRepository.findAllByCondition(clientName, fromDate, toDate, currencyId, chargeName, pageable);
        return mapper.toPageResponses(estimates, EstimateResponse.class);
    }

    // 견적 수정
    @Override
    public EstimateResponse updateEstimate(Long estimateId, EstimateRequest estimateRequest) throws NotFoundException {
        Estimate findEstimate = getEstimateOrThrow(estimateId);
        Client newClient = clientService.getClientOrThrow(estimateRequest.getClient());
        Currency newCurrency = currencyService.getCurrencyOrThrow(estimateRequest.getCurrency());
        Estimate newEstimate = mapper.toEntity(estimateRequest, Estimate.class);
        findEstimate.update(newClient, newCurrency, newEstimate);
        estimateRepository.save(findEstimate);
        return mapper.toResponse(findEstimate, EstimateResponse.class);
    }

    // 견적 삭제
    @Override
    public void deleteEstimate(Long estimateId) throws NotFoundException {
        Estimate estimate = getEstimateOrThrow(estimateId);
        estimate.delete();
        estimateRepository.save(estimate);
    }

    // 견적 단일 조회 및 예외
    @Override
    public Estimate getEstimateOrThrow(Long id) throws NotFoundException {
        return estimateRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("estimate does not exist. input id: " + id));
    }

    // 견적번호 날짜형식으로 생성
    private String createEstimateNo() {
        String dateTimeFormat = "yyyyMMdd_HHmmss";
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateTimeFormat));
    }

    // ===================================== 견적 품목 정보 ======================================
    // 견적 품목 생성
    @Override
    public EstimateItemResponse createEstimateItem(Long estimateId, EstimateItemRequest estimateItemRequest) throws NotFoundException {
        Estimate estimate = getEstimateOrThrow(estimateId);

        EstimateItemDetail itemDetail = mapper.toEntity(estimateItemRequest, EstimateItemDetail.class);
        return null;
    }
    // 견적 품목 단일 조회
    @Override
    public EstimateItemResponse getEstimateItem(Long estimateId, Long estimateItemId) {
        return null;
    }
    // 견적 품목 페이징 조회
    @Override
    public List<EstimateItemResponse> getEstimateItems(Long estimateId) {
        return null;
    }
    // 견적 품목 수정
    @Override
    public EstimateItemResponse updateEstimateItem(Long estimateId, Long estimateItemId, EstimateItemRequest estimateItemRequest) {
        return null;
    }
    // 견적 품목 삭제
    @Override
    public void deleteEstimateItem(Long estimateId, Long estimateItemId) {

    }
    // 견적품목 단일 조회 및 예외
    private EstimateItemDetail getEstimateItemOrThrow(Long estimateItemId, Estimate estimate) throws NotFoundException {
        return estimateItemRepository.findByIdAndEstimateAndDeleteYnFalse(estimateItemId, estimate)
                .orElseThrow(() -> new NotFoundException("estimate item does not exist. input estimate item id: " + estimateItemId));
    }
}
