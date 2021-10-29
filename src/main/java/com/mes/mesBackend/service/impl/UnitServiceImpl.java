package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.UnitRequest;
import com.mes.mesBackend.dto.response.UnitResponse;
import com.mes.mesBackend.entity.Unit;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.UnitRepository;
import com.mes.mesBackend.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UnitServiceImpl implements UnitService {
    @Autowired
    UnitRepository unitRepository;

    @Autowired
    ModelMapper modelMapper;


    // 생성
    @Override
    public UnitResponse createUnit(UnitRequest unitRequest) throws BadRequestException {
        checkBaseScale(unitRequest.getBaseScale());
        Unit unit = modelMapper.toEntity(unitRequest, Unit.class);
        Unit saveUnit = unitRepository.save(unit);
        return modelMapper.toResponse(saveUnit, UnitResponse.class);
    }

    // 단일조회
    @Override
    public UnitResponse getUnit(Long id) throws NotFoundException {
        Unit unit = findUnitOrThrow(id);
        return modelMapper.toResponse(unit, UnitResponse.class);
    }

    // 페이징조회
    @Override
    public Page<UnitResponse> getUnits(Pageable pageable) {
        Page<Unit> units = unitRepository.findAllByDeleteYnFalse(pageable);
        return modelMapper.toPageResponses(units, UnitResponse.class );
    }

    // 수정
    @Override
    public UnitResponse updateUnit(Long id, UnitRequest unitRequest) throws NotFoundException {
        Unit newUnit = modelMapper.toEntity(unitRequest, Unit.class);
        Unit findUnit = findUnitOrThrow(id);
        findUnit.putUnit(newUnit);
        Unit saveUnit = unitRepository.save(findUnit);
        return modelMapper.toResponse(saveUnit, UnitResponse.class);
    }

    // 삭제
    @Override
    public void deleteUnit(Long id) throws NotFoundException {
        Unit unit = findUnitOrThrow(id);
        unit.setDeleteYn(true);
        unitRepository.save(unit);
    }

    // 예외처리 단일조회
    @Override
    public Unit findUnitOrThrow(Long id) throws NotFoundException {
        Unit findUnit = unitRepository.findByIdAndDeleteYnFalse(id);
        if (findUnit == null) throw new NotFoundException("unit does not exists. input id: " + id);
        return findUnit;
    }

    // baseScale 소수점 자리수 검증
    private void checkBaseScale(float baseScale) throws BadRequestException {
        String baseScaleToString = Float.toString(baseScale);
        int indexOf = baseScaleToString.lastIndexOf(".");
        String substring = baseScaleToString.substring(indexOf);
        int length = substring.length() - 1;
        if (length > 3) throw new BadRequestException("baseScale decimal point is 3 decimal places.");
    }
}
