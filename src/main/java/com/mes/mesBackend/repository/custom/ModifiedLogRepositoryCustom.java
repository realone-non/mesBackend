package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.entity.ModifiedLog;
import com.mes.mesBackend.entity.enumeration.ModifiedDivision;

import java.util.Optional;

public interface ModifiedLogRepositoryCustom {
    Optional<ModifiedLog> findByModifiedDivisionId(Long divisionId, ModifiedDivision modifiedDivision);
}
