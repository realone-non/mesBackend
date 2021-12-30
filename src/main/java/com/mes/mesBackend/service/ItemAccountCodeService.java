package com.mes.mesBackend.service;

import com.mes.mesBackend.entity.ItemAccountCode;
import com.mes.mesBackend.exception.NotFoundException;

// 품목계정코드
public interface ItemAccountCodeService {
    // 품목계정코드 조회 및 예외
    ItemAccountCode getItemAccountCodeOrThrow(Long id) throws NotFoundException;
}
