package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.entity.ItemAccountCode;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.repository.ItemAccountCodeRepository;
import com.mes.mesBackend.service.ItemAccountCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// 품목계정코드
@Service
@RequiredArgsConstructor
public class ItemAccountCodeServiceImpl implements ItemAccountCodeService {
    private final ItemAccountCodeRepository itemAccountCodeRepo;

    // 품목계정코드 단일 조회 및 예외
    @Override
    public ItemAccountCode getItemAccountCodeOrThrow(Long id) throws NotFoundException {
        return itemAccountCodeRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("itemAccountCode does not exist. input itemAccountCode id: " + id));
    }
}
