package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.RecycleResponse;

import java.util.List;
import java.util.Optional;

public interface RecycleRepositoryCustom {

    //재사용 유형 단일 조회
    Optional<RecycleResponse> findByIdAndDeleteYn(long id);

    //재사용 유형 리스트 조회
    List<RecycleResponse> findRecycles(Long workProcessId);
}
