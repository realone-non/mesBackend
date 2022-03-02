package com.mes.mesBackend.helper;

import com.mes.mesBackend.entity.ModifiedLog;
import com.mes.mesBackend.entity.enumeration.ModifiedDivision;
import com.mes.mesBackend.exception.NotFoundException;

// 업데이트 시 기록 생성
public interface ModifiedLogHelper {
    // 업데이트 시 로그 생성
    <T> void createModifiedLog(String userCode, ModifiedDivision modifiedDivision, T t) throws NotFoundException;
    // 조회
    ModifiedLog getModifiedLog(ModifiedDivision modifiedDivision, Long divisionId);
}
