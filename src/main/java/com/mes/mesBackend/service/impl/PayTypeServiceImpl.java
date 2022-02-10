package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.PayTypeResponse;
import com.mes.mesBackend.entity.PayType;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.PayTypeRepository;
import com.mes.mesBackend.service.PayTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PayTypeServiceImpl implements PayTypeService {
    private final PayTypeRepository payTypeRepository;
    private final ModelMapper mapper;

    // 결제조건 생성
    @Override
    public PayTypeResponse createPayType(String payTypeRequest) {
        PayType payType = new PayType();
        payType.setPayType(payTypeRequest);
        payTypeRepository.save(payType);
        return mapper.toResponse(payType, PayTypeResponse.class);
    }

    // 결제조건 단일조회
    @Override
    public PayTypeResponse getPayType(Long id) throws NotFoundException {
        PayType payType = getPayTypeOrThrow(id);
        return mapper.toResponse(payType, PayTypeResponse.class);
    }

    // 결제조건 전체 조회
    @Override
    public List<PayTypeResponse> getPayTypes() {
        List<PayType> payTypes = payTypeRepository.findAllByDeleteYnFalse();
        return mapper.toListResponses(payTypes, PayTypeResponse.class);
    }

    // 결제조건 수정
    @Override
    public PayTypeResponse updatePayType(Long id, String payTypeRequest) throws NotFoundException {
        PayType findPayType = getPayTypeOrThrow(id);
        findPayType.setPayType(payTypeRequest);
        payTypeRepository.save(findPayType);
        return mapper.toResponse(findPayType, PayTypeResponse.class);
    }

    // 결제조건 삭제
    @Override
    public void deletePayType(Long id) throws NotFoundException {
        PayType payType = getPayTypeOrThrow(id);
        payType.delete();
        payTypeRepository.save(payType);
    }

    // 결제조건 단일 조회 및 예외
    private PayType getPayTypeOrThrow(Long id) throws NotFoundException {
        return payTypeRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("payType does not exist. input id: " + id));
    }
}
