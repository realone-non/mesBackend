package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.CodeMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CodeMasterRepositoryCustom {
    // 주코드, 코드명
    Page<CodeMaster> findByMainCodeAndCodeName(String mainCode, String codeName, Pageable pageable);
}
