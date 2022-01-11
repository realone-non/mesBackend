package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.RepairCodeRequest;
import com.mes.mesBackend.dto.response.RepairCodeResponse;
import com.mes.mesBackend.entity.RepairCode;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.RepairCodeRepository;
import com.mes.mesBackend.service.RepairCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// 수리코드
@Service
@RequiredArgsConstructor
public class RepairCodeServiceImpl implements RepairCodeService {
    private final RepairCodeRepository repairCodeRepository;
    private final ModelMapper mapper;

    // 수리코드 생성
    @Override
    public RepairCodeResponse createRepairCode(RepairCodeRequest repairCodeRequest) {
        RepairCode repairCode = mapper.toEntity(repairCodeRequest, RepairCode.class);
        repairCodeRepository.save(repairCode);
        return mapper.toResponse(repairCode, RepairCodeResponse.class);
    }

    // 수리코드 단일 조회
    @Override
    public RepairCodeResponse getRepairCode(Long id) throws NotFoundException {
        RepairCode repairCode = getRepairCodeOrThrow(id);
        return mapper.toResponse(repairCode, RepairCodeResponse.class);
    }

    // 수리코드 리스트 조회
    @Override
    public List<RepairCodeResponse> getRepairCodes() {
        List<RepairCode> repairCodes = repairCodeRepository.findAllByDeleteYnFalse();
        return mapper.toListResponses(repairCodes, RepairCodeResponse.class);
    }

    // 수리코드 수정
    @Override
    public RepairCodeResponse updateRepairCode(Long id, RepairCodeRequest repairCodeRequest) throws NotFoundException {
        RepairCode findRepairCode = getRepairCodeOrThrow(id);
        RepairCode newRepairCode = mapper.toEntity(repairCodeRequest, RepairCode.class);
        findRepairCode.update(newRepairCode);
        repairCodeRepository.save(findRepairCode);
        return mapper.toResponse(findRepairCode, RepairCodeResponse.class);
    }

    // 수리코드 삭제
    @Override
    public void deleteRepairCode(Long id) throws NotFoundException {
        RepairCode repairCode = getRepairCodeOrThrow(id);
        repairCode.delete();
        repairCodeRepository.save(repairCode);
    }

    // 수리코드 조회 및 예외
    @Override
    public RepairCode getRepairCodeOrThrow(Long id) throws NotFoundException {
        return repairCodeRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("repairCode does not exist. input id: " + id));
    }
}
