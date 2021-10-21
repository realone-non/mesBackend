package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.ClientTypeRequest;
import com.mes.mesBackend.dto.response.ClientTypeResponse;
import com.mes.mesBackend.entity.ClientType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientTypeService {

    // 거래처유형 생성
    ClientTypeResponse createClientType(ClientTypeRequest clientTypeRequest);

    // 거래처유형 조회
    ClientTypeResponse getClientType(Long id);

    // 거래처유형 전체 조회
    Page<ClientTypeResponse> getClientTypes(Pageable pageable);

    // 거래처유형 수정
    ClientTypeResponse updateClientType(Long id, ClientTypeRequest clientTypeRequest);

    // 거래처유형 삭제
    void deleteClientType(Long id);

    ClientType findClientTypeByIdAndDeleteYn(Long id);
}