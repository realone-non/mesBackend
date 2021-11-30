package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.RoutingDetailRequest;
import com.mes.mesBackend.dto.request.RoutingRequest;
import com.mes.mesBackend.dto.response.RoutingDetailResponse;
import com.mes.mesBackend.dto.response.RoutingResponse;
import com.mes.mesBackend.entity.Routing;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

// 3-2-5. 라우팅 등록, 라우팅 디테일 정보
public interface RoutingService {
    // 라우팅 생성
    RoutingResponse createRouting(RoutingRequest routingRequest);
    // 라우팅 단일 조회
    RoutingResponse getRouting(Long id) throws NotFoundException;
    // 라우팅 전체 조회
    List<RoutingResponse> getRoutings();
    // 라우팅 페이징 조회
//    Page<RoutingResponse> getRoutings(Pageable pageable);
    // 라우팅 수정
    RoutingResponse updateRouting(Long id, RoutingRequest routingRequest) throws NotFoundException;
    // 라우팅 삭제
    void deleteRouting(Long id) throws NotFoundException;
    // 라우팅 단일 조회 및 예외
    Routing getRoutingOrThrow(Long id) throws NotFoundException;


    // 라우팅 디테일 생성
    RoutingDetailResponse createRoutingDetails(Long routingId, RoutingDetailRequest routingDetailRequest) throws NotFoundException;
    // 라우팅 디테일 리스트 조회
    List<RoutingDetailResponse> getRoutingDetails(Long routingId) throws NotFoundException;
    // 라우팅 디테일 수정
    RoutingDetailResponse updateRoutingDetail(Long routingId, Long routingDetailId, RoutingDetailRequest routingDetailRequest) throws NotFoundException;
    // 라우팅 디테일 삭제
    void deleteRoutingDetail(Long routingId, Long routingDetailId) throws NotFoundException;
}
