package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.WareHouseRequest;
import com.mes.mesBackend.dto.response.WareHouseResponse;
import com.mes.mesBackend.entity.WareHouse;
import com.mes.mesBackend.entity.WareHouseType;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.WareHouseRepository;
import com.mes.mesBackend.service.WareHouseService;
import com.mes.mesBackend.service.WareHouseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WareHouseServiceImpl implements WareHouseService {

    @Autowired
    WareHouseRepository wareHouseRepository;

    @Autowired
    WareHouseTypeService wareHouseTypeService;

    @Autowired
    ModelMapper mapper;

    // 생성
    @Override
    public WareHouseResponse createWareHouse(WareHouseRequest wareHouseRequest) throws NotFoundException {
        WareHouseType wareHouseType = wareHouseTypeService.getWareHouseTypeOrThrow(wareHouseRequest.getWareHouseType());
        WareHouse wareHouse = mapper.toEntity(wareHouseRequest, WareHouse.class);
        wareHouse.addWareHouseType(wareHouseType);
        WareHouse saveWareHouse = wareHouseRepository.save(wareHouse);
        return mapper.toResponse(saveWareHouse, WareHouseResponse.class);
    }

    // 조회
    @Override
    public WareHouseResponse getWareHouse(Long id) throws NotFoundException {
        WareHouse wareHouse = getWareHouseOrThrow(id);
        return mapper.toResponse(wareHouse, WareHouseResponse.class);
    }

    // 페이징조회
    @Override
    public Page<WareHouseResponse> getWareHouses(Pageable pageable) {
        Page<WareHouse> findWareHouses = wareHouseRepository.findAllByDeleteYnFalse(pageable);
        return mapper.toPageResponses(findWareHouses, WareHouseResponse.class);
    }

    // 수정
    @Override
    public WareHouseResponse updateWareHouse(Long id, WareHouseRequest wareHouseRequest) throws NotFoundException {
        WareHouse findWareHouse = getWareHouseOrThrow(id);
        WareHouseType newWareHouseType = wareHouseTypeService.getWareHouseTypeOrThrow(wareHouseRequest.getWareHouseType());
        WareHouse newWareHouse = mapper.toEntity(wareHouseRequest, WareHouse.class);
        findWareHouse.put(newWareHouse, newWareHouseType);
        WareHouse saveWareHouse = wareHouseRepository.save(findWareHouse);
        return mapper.toResponse(saveWareHouse, WareHouseResponse.class);
    }

    // 삭제
    @Override
    public void deleteWareHouse(Long id) throws NotFoundException {
        WareHouse findWareHouse = getWareHouseOrThrow(id);
        findWareHouse.delete();
        wareHouseRepository.save(findWareHouse);
    }

    @Override
    public WareHouse getWareHouseOrThrow(Long id) throws NotFoundException {
        WareHouse findWareHouse = wareHouseRepository.findByIdAndDeleteYnFalse(id);
        if (findWareHouse == null) throw new NotFoundException("wareHouse does not exists. input id: " + id);
        return findWareHouse;
    }
}
