package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.WareHouseRequest;
import com.mes.mesBackend.dto.response.WareHouseResponse;
import com.mes.mesBackend.entity.WareHouse;
import com.mes.mesBackend.entity.WareHouseType;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.WareHouseRepository;
import com.mes.mesBackend.service.WareHouseService;
import com.mes.mesBackend.service.WareHouseTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WareHouseServiceImpl implements WareHouseService {
    private final WareHouseRepository wareHouseRepository;
    private final WareHouseTypeService wareHouseTypeService;
    private final ModelMapper mapper;

    // 생성
    @Override
    public WareHouseResponse createWareHouse(WareHouseRequest wareHouseRequest) throws NotFoundException, BadRequestException {
        WareHouseType wareHouseType = wareHouseTypeService.getWareHouseTypeOrThrow(wareHouseRequest.getWareHouseType());

        // 공정용 창고가 2개 이상 등록되면 안됨
        if (wareHouseRepository.existsByWorkProcessYnTrueAndDeleteYnFalse()) throw new BadRequestException("공정용 창고는 한개만 등록 가능합니다.");

        WareHouse wareHouse = mapper.toEntity(wareHouseRequest, WareHouse.class);
        wareHouse.addJoin(wareHouseType);
        wareHouseRepository.save(wareHouse);
        return mapper.toResponse(wareHouse, WareHouseResponse.class);
    }

    // 조회
    @Override
    public WareHouseResponse getWareHouse(Long id) throws NotFoundException {
        WareHouse wareHouse = getWareHouseOrThrow(id);
        return mapper.toResponse(wareHouse, WareHouseResponse.class);
    }

    // 전체 리스트 조회
    @Override
    public List<WareHouseResponse> getWareHouses() {
        List<WareHouse> findWareHouses = wareHouseRepository.findAllByDeleteYnFalse();
        return mapper.toListResponses(findWareHouses, WareHouseResponse.class);
    }

    // 페이징조회
//    @Override
//    public Page<WareHouseResponse> getWareHouses(Pageable pageable) {
//        Page<WareHouse> findWareHouses = wareHouseRepository.findAllByDeleteYnFalse(pageable);
//        return mapper.toPageResponses(findWareHouses, WareHouseResponse.class);
//    }

    // 수정
    @Override
    public WareHouseResponse updateWareHouse(Long id, WareHouseRequest wareHouseRequest) throws NotFoundException {
        WareHouse findWareHouse = getWareHouseOrThrow(id);
        WareHouseType newWareHouseType = wareHouseTypeService.getWareHouseTypeOrThrow(wareHouseRequest.getWareHouseType());
        WareHouse newWareHouse = mapper.toEntity(wareHouseRequest, WareHouse.class);
        findWareHouse.put(newWareHouse, newWareHouseType);
        wareHouseRepository.save(findWareHouse);
        return mapper.toResponse(findWareHouse, WareHouseResponse.class);
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
        return wareHouseRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("wareHouse does not exists. input id: " + id));
    }
}
