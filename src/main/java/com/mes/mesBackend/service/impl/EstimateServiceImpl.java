package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.EstimateItemRequest;
import com.mes.mesBackend.dto.request.EstimatePiRequest;
import com.mes.mesBackend.dto.request.EstimateRequest;
import com.mes.mesBackend.dto.response.EstimateItemResponse;
import com.mes.mesBackend.dto.response.EstimatePiResponse;
import com.mes.mesBackend.dto.response.EstimateResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.EstimateItemDetailRepository;
import com.mes.mesBackend.repository.EstimateRepository;
import com.mes.mesBackend.repository.PiRepository;
import com.mes.mesBackend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.mes.mesBackend.helper.Constants.DATE_TIME_FORMAT;

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
    @Autowired
    ItemService itemService;
    @Autowired
    PiRepository piRepository;
    @Autowired
    UserService userService;

    // 견적 생성
    @Override
    public EstimateResponse createEstimate(EstimateRequest estimateRequest) throws NotFoundException {
        Client client = clientService.getClientOrThrow(estimateRequest.getClient());
        Currency currency = currencyService.getCurrencyOrThrow(estimateRequest.getCurrency());
        User user = userService.getUserOrThrow(estimateRequest.getUser());
        Estimate estimate = mapper.toEntity(estimateRequest, Estimate.class);
        String estimateNo = createEstimateNo();
        estimate.addMapping(client, currency, estimateNo, user);
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
    public List<EstimateResponse> getEstimates(
            String clientName,
            LocalDate fromDate,
            LocalDate toDate,
            Long currencyId,
            String chargeName
    ) {
        List<Estimate> estimates = estimateRepository.findAllByCondition(clientName, fromDate, toDate, currencyId, chargeName);
        return mapper.toListResponses(estimates, EstimateResponse.class);
    }
    // 견적 페이징 조회 검색조건: 거래처, 견적기간(fromDate~toDate), 화폐, 담당자
//    @Override
//    public Page<EstimateResponse> getEstimates(
//            String clientName,
//            LocalDate fromDate,
//            LocalDate toDate,
//            Long currencyId,
//            String chargeName,
//            Pageable pageable
//    ) {
//        Page<Estimate> estimates = estimateRepository.findAllByCondition(clientName, fromDate, toDate, currencyId, chargeName, pageable);
//        return mapper.toPageResponses(estimates, EstimateResponse.class);
//    }

    // 견적 수정
    @Override
    public EstimateResponse updateEstimate(Long estimateId, EstimateRequest estimateRequest) throws NotFoundException {
        Estimate findEstimate = getEstimateOrThrow(estimateId);
        Client newClient = clientService.getClientOrThrow(estimateRequest.getClient());
        Currency newCurrency = currencyService.getCurrencyOrThrow(estimateRequest.getCurrency());
        User newUser = userService.getUserOrThrow(estimateRequest.getUser());
        Estimate newEstimate = mapper.toEntity(estimateRequest, Estimate.class);
        findEstimate.update(newClient, newCurrency, newEstimate, newUser);
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
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
    }

    // ===================================== 견적 품목 정보 ======================================
    // 견적 품목 생성
    @Override
    public EstimateItemResponse createEstimateItem(Long estimateId, EstimateItemRequest estimateItemRequest) throws NotFoundException {
        Estimate estimate = getEstimateOrThrow(estimateId);
        Item item = itemService.getItemOrThrow(estimateItemRequest.getItem());

        EstimateItemDetail estimateItem = mapper.toEntity(estimateItemRequest, EstimateItemDetail.class);

        estimateItem.addJoin(estimate, item);
        estimateItemRepository.save(estimateItem);
        return mapper.toResponse(estimateItem, EstimateItemResponse.class);
    }

    // 견적 품목 단일 조회
    @Override
    public EstimateItemResponse getEstimateItem(Long estimateId, Long estimateItemId) throws NotFoundException {
        Estimate estimate = getEstimateOrThrow(estimateId);
        EstimateItemDetail estimateItem = getEstimateItemOrThrow(estimateItemId, estimate);
        return mapper.toResponse(estimateItem, EstimateItemResponse.class);
    }

    // 견적 품목 페이징 조회
    @Override
    public List<EstimateItemResponse> getEstimateItems(Long estimateId) throws NotFoundException {
        Estimate estimate = getEstimateOrThrow(estimateId);
        List<EstimateItemDetail> itemDetails = estimateItemRepository.findAllByEstimateAndDeleteYnFalse(estimate);
        return mapper.toListResponses(itemDetails, EstimateItemResponse.class);
    }

    // 견적 품목 수정
    @Override
    public EstimateItemResponse updateEstimateItem(Long estimateId, Long estimateItemId, EstimateItemRequest estimateItemRequest) throws NotFoundException {
        Estimate estimate = getEstimateOrThrow(estimateId);
        Item newItem = itemService.getItemOrThrow(estimateItemRequest.getItem());
        EstimateItemDetail findEstimateItem = getEstimateItemOrThrow(estimateItemId, estimate);
        EstimateItemDetail newEstimateItem = mapper.toEntity(estimateItemRequest, EstimateItemDetail.class);
        findEstimateItem.update(newItem, newEstimateItem);
        estimateItemRepository.save(findEstimateItem);
        return mapper.toResponse(findEstimateItem, EstimateItemResponse.class);
    }

    // 견적 품목 삭제
    @Override
    public void deleteEstimateItem(Long estimateId, Long estimateItemId) throws NotFoundException {
        Estimate estimate = getEstimateOrThrow(estimateId);
        EstimateItemDetail estimateItem = getEstimateItemOrThrow(estimateItemId, estimate);
        estimateItem.delete();
        estimateItemRepository.save(estimateItem);
    }

    // 견적품목 단일 조회 및 예외
    private EstimateItemDetail getEstimateItemOrThrow(Long estimateItemId, Estimate estimate) throws NotFoundException {
        return estimateItemRepository.findByIdAndEstimateAndDeleteYnFalse(estimateItemId, estimate)
                .orElseThrow(() -> new NotFoundException("estimate item does not exist. input estimate item id: " + estimateItemId));
    }

    // ===================================== 견적 P/I ======================================

    // 견적 P/I 생성
    @Override
    public EstimatePiResponse createEstimatePi(Long estimateId, EstimatePiRequest estimatePiRequest) throws NotFoundException, BadRequestException {
        Estimate estimate = getEstimateOrThrow(estimateId);
        String invoiceNo = createInvoiceNo();           // invoiceNo 생성
        checkExistsInvoiceNo(invoiceNo);        // invoiceNo 중복 EXCEPTION
        Pi pi = mapper.toEntity(estimatePiRequest, Pi.class);
        pi.setInvoiceNo(invoiceNo);
        piRepository.save(pi);
        estimate.addPi(pi);
        estimateRepository.save(estimate);
        return mapper.toResponse(pi, EstimatePiResponse.class);
    }


    // 견적의 P/I 조회
    @Override
    public EstimatePiResponse getEstimatePi(Long estimateId) throws NotFoundException {
        Pi pi = getEstimatePiOrThrow(estimateId);
        return mapper.toResponse(pi, EstimatePiResponse.class);
    }

    // 견적 P/I 수정
    @Override
    public EstimatePiResponse updateEstimatePi(Long estimateId, Long estimatePiId, EstimatePiRequest estimatePiRequest) throws NotFoundException, BadRequestException {
        Estimate estimate = getEstimateOrThrow(estimateId);
        if (estimate.getPi() == null || !estimate.getPi().getId().equals(estimatePiId)) {
            throw new BadRequestException("estimate pi does not exist. input estimate id: " + estimateId);
        }
        Pi findPi = getEstimatePiOrThrow(estimateId);
        Pi newPi = mapper.toEntity(estimatePiRequest, Pi.class);
        findPi.update(newPi);
        piRepository.save(findPi);
        return mapper.toResponse(findPi, EstimatePiResponse.class);
    }

    // 견적 P/I 삭제
    @Override
    public void deleteEstimatePi(Long estimateId, Long estimatePiId) throws NotFoundException, BadRequestException {
        Estimate estimate = getEstimateOrThrow(estimateId);
        if (!estimate.getPi().getId().equals(estimatePiId)) {
            throw new BadRequestException("pi does not exist. input estimate id: " + estimateId);
        }
        estimate.setPi(null);
        piRepository.deleteById(estimatePiId);
    }

    // Invoice No 생성
    public String createInvoiceNo() {
        String dateTimeFormat = DATE_TIME_FORMAT;
        return "EPI"+LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateTimeFormat));
    }

    // pi 단일 조회 및 예외
    private Pi getEstimatePiOrThrow(Long estimateId) throws NotFoundException {
        Estimate estimate = getEstimateOrThrow(estimateId);
        if (estimate.getPi() == null) {
            return null;
        }
        return piRepository.findById(estimate.getPi().getId())
                .orElseThrow(() -> new NotFoundException("estimate pi does not exist. input estimate id: " + estimateId));
    }

    // invoiceNo 중복 체크
    private void checkExistsInvoiceNo(String invoiceNo) throws BadRequestException {
        List<Pi> pis = piRepository.findAllByInvoiceNo(invoiceNo);
        if (!pis.isEmpty()) {
            throw new BadRequestException("exists by invoiceNo.");
        }
    }
}
