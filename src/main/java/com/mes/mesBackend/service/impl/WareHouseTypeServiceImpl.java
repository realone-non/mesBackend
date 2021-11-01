package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.WareHouseTypeRequest;
import com.mes.mesBackend.dto.response.WareHouseTypeResponse;
import com.mes.mesBackend.entity.WareHouseType;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.WareHouseTypeRepository;
import com.mes.mesBackend.service.WareHouseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

// 창고유형
@Service
public class WareHouseTypeServiceImpl implements WareHouseTypeService {

    @Autowired
    ModelMapper mapper;

    @Autowired
    WareHouseTypeRepository wareHouseTypeRepository;

    // 생성
    @Override
    public WareHouseTypeResponse createWareHouseType(WareHouseTypeRequest wareHouseTypeRequest) {
        WareHouseType wareHouseType = mapper.toEntity(wareHouseTypeRequest, WareHouseType.class);
        WareHouseType saveWareHouseType = wareHouseTypeRepository.save(wareHouseType);
        return mapper.toResponse(saveWareHouseType, WareHouseTypeResponse.class);
    }

    // 단일조회
    @Override
    public WareHouseTypeResponse getWareHouseType(Long id) throws NotFoundException {
        WareHouseType findWareHouseType = getWareHouseTypeOrThrow(id);
        return mapper.toResponse(findWareHouseType, WareHouseTypeResponse.class);
    }

    // 페이징조회
    @Override
    public Page<WareHouseTypeResponse> getWareHouseTypes(Pageable pageable) {
        Page<WareHouseType> findWareHouseTypes = wareHouseTypeRepository.findAllByDeleteYnFalse(pageable);
        return mapper.toPageResponses(findWareHouseTypes, WareHouseTypeResponse.class);
    }

    // 수정
    @Override
    public WareHouseTypeResponse updateWareHouseType(Long id, WareHouseTypeRequest wareHouseTypeRequest) throws NotFoundException {
        WareHouseType findWareHouseType = getWareHouseTypeOrThrow(id);
        WareHouseType newWareHouseType = mapper.toEntity(wareHouseTypeRequest, WareHouseType.class);
        findWareHouseType.put(newWareHouseType);
        WareHouseType saveWareHouseType = wareHouseTypeRepository.save(findWareHouseType);
        return mapper.toResponse(saveWareHouseType, WareHouseTypeResponse.class);
    }

    // 삭제
    @Override
    public void deleteWareHouseType(Long id) throws NotFoundException {
        WareHouseType findWareHouseType = getWareHouseTypeOrThrow(id);
        findWareHouseType.delete();
        wareHouseTypeRepository.save(findWareHouseType);
    }


    @Override
    public WareHouseType getWareHouseTypeOrThrow(Long id) throws NotFoundException {
        WareHouseType findWareHouseType = wareHouseTypeRepository.findByIdAndDeleteYnFalse(id);
        if (findWareHouseType == null) throw new NotFoundException("wareHouseType does not exists. input id: " + id);
        return findWareHouseType;
    }
}
