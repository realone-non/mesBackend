package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.FactoryRequest;
import com.mes.mesBackend.dto.response.FactoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FactoryService {

    // 공장 생성
    FactoryResponse createFactory(FactoryRequest factoryRequest);

    // 공장 조회
    FactoryResponse getFactory(Long id);

    // 공장 전체 조회
    Page<FactoryResponse> getFactories(Pageable pageable);

    // 공장 수정
    FactoryResponse updateFactory(Long id, FactoryRequest factoryRequest);

    // 공장 삭제
    void deleteFactory(Long id);
}
