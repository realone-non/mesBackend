package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.RecycleRequest;
import com.mes.mesBackend.dto.response.RecycleResponse;
import com.mes.mesBackend.exception.NotFoundException;

import java.util.List;

//재사용 등록
public interface RecycleService {
    //재사용 생성
    RecycleResponse createRecycle(RecycleRequest request) throws NotFoundException;

    //재사용 조회
    RecycleResponse getRecycle(Long id) throws NotFoundException;

    //재사용 리스트 조회
    List<RecycleResponse> getRecycles(Long workProcessId);

    //재사용 수정
    RecycleResponse modifyRecycle(Long id, RecycleRequest request) throws NotFoundException;

    //재사용 삭제
    void deleteRecycle(Long id) throws NotFoundException;
}
