package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientRepositoryCustom {
    // 거래처 유형, 거래처 코드, 거래처 명
    Page<Client> findByTypeAndCodeAndName(Long type, String clientCode, String name, Pageable pageable);
}
