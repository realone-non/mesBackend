package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.WareHouseRequest;
import com.mes.mesBackend.dto.response.WareHouseResponse;
import com.mes.mesBackend.entity.WareHouse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

// 창고
public interface WareHouseService {
    // 생성
    WareHouseResponse createWareHouse(WareHouseRequest wareHouseRequest) throws NotFoundException, BadRequestException;

    // 단일
    WareHouseResponse getWareHouse(Long id) throws NotFoundException;

    // 전체 조회
    List<WareHouseResponse> getWareHouses();

    // 페이징조회
//    Page<WareHouseResponse> getWareHouses(Pageable pageable);

    // 수정
    WareHouseResponse updateWareHouse(Long id, WareHouseRequest wareHouseRequest) throws NotFoundException, BadRequestException;

    // 삭제
    void deleteWareHouse(Long id) throws NotFoundException;

    WareHouse getWareHouseOrThrow(Long id) throws NotFoundException;
}
