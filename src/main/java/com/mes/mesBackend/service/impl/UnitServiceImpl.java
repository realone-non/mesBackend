package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.UnitRequest;
import com.mes.mesBackend.dto.response.UnitResponse;
import com.mes.mesBackend.entity.ModifiedLog;
import com.mes.mesBackend.entity.Unit;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.ModifiedLogHelper;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.UnitRepository;
import com.mes.mesBackend.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mes.mesBackend.entity.enumeration.ModifiedDivision.UNIT;

@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService {
    private final UnitRepository unitRepository;
    private final ModelMapper modelMapper;
    private final ModifiedLogHelper modifiedLogHelper;

    // 생성
    @Override
    public UnitResponse createUnit(UnitRequest unitRequest) throws BadRequestException {
        checkBaseScale(unitRequest.getBaseScale());
        Unit unit = modelMapper.toEntity(unitRequest, Unit.class);
        unitRepository.save(unit);
        return modelMapper.toResponse(unit, UnitResponse.class);
    }

    // 단일조회
    @Override
    public UnitResponse getUnit(Long id) throws NotFoundException {
        Unit unit = getUnitOrThrow(id);
        return modelMapper.toResponse(unit, UnitResponse.class);
    }

    // 전체조회
    @Override
    public List<UnitResponse> getUnits() {
        List<Unit> units = unitRepository.findAllByDeleteYnFalseOrderByCreatedDateDesc();
        List<UnitResponse> response = modelMapper.toListResponses(units, UnitResponse.class);
        for (UnitResponse r : response) {
            ModifiedLog modifiedLog = modifiedLogHelper.getModifiedLog(UNIT, r.getId());
            if (modifiedLog != null) r.modifiedLog(modifiedLog);
        }
        return response;
    }

    // 페이징조회
//    @Override
//    public Page<UnitResponse> getUnits(Pageable pageable) {
//        Page<Unit> units = unitRepository.findAllByDeleteYnFalse(pageable);
//        return modelMapper.toPageResponses(units, UnitResponse.class );
//    }

    // 수정
    @Override
    public UnitResponse updateUnit(Long id, UnitRequest unitRequest, String userCode) throws NotFoundException {
        Unit newUnit = modelMapper.toEntity(unitRequest, Unit.class);
        Unit findUnit = getUnitOrThrow(id);
        findUnit.putUnit(newUnit);
        unitRepository.save(findUnit);
        modifiedLogHelper.createModifiedLog(userCode, UNIT, findUnit);        // 업데이트 로그 관리
        return modelMapper.toResponse(findUnit, UnitResponse.class);
    }

    // 삭제
    @Override
    public void deleteUnit(Long id) throws NotFoundException {
        Unit unit = getUnitOrThrow(id);
        unit.delete();
        unitRepository.save(unit);
    }

    // 예외처리 단일조회
    @Override
    public Unit getUnitOrThrow(Long id) throws NotFoundException {
        return unitRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("unit does not exists. input id: " + id));
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
