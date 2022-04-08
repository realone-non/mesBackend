package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.DeadlineResponse;
import com.mes.mesBackend.entity.Contract;
import com.mes.mesBackend.entity.Deadline;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.repository.ContractRepository;
import com.mes.mesBackend.service.DeadlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// 18-5. 마감일자
@Service
@RequiredArgsConstructor
public class DeadlineServiceImpl implements DeadlineService {
    private final ContractRepository contractRepo;

    // 마감일자 생성
    @Override
    public DeadlineResponse createDeadline(Long contractId, LocalDate deadlineDate) throws NotFoundException {
        Contract contract = getContractOrThrow(contractId);
        contract.setDeadlineDate(deadlineDate);
        contractRepo.save(contract);
        return getDeadline(contract.getId());
    }

    // 수주 단일 조회 및 예외
    private Contract getContractOrThrow(Long id) throws NotFoundException {
        return contractRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("contract does not exists. id: " + id));
    }

    // 마감일자 리스트 조회
    @Override
    public List<DeadlineResponse> getDeadlines() {
        return contractRepo.findDeadlineResponsesByNotDeadlineDate();
    }

    // 마감일자 삭제
    @Override
    public void deleteDeadline(Long contractId) throws NotFoundException, BadRequestException {
        Contract contract = getContractOrThrow(contractId);
        throwIfContractDeadlineNull(contract);
        contract.setDeadlineDate(null);
    }

    // 수주 마감일자 등록 안되어 있는지 여부
    private void throwIfContractDeadlineNull(Contract contract) throws BadRequestException {
        if (contract.getDeadlineDate() != null) {
            throw new BadRequestException("해당 수주는 마감일자가 등록되어 있지 않으므로 삭제가 불가능합니다.");
        }
    }

    // 마감일자 단일 조회
    private DeadlineResponse getDeadline(Long id) throws NotFoundException {
        return contractRepo.findDeadlineResponseByContractId(id)
                .orElseThrow(() -> new NotFoundException("contract does not exists. id; " + id));
    }
}
