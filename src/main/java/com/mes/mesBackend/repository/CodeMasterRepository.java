package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.CodeMaster;
import com.mes.mesBackend.repository.custom.CodeMasterRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeMasterRepository extends JpaRepository<CodeMaster, Long>, CodeMasterRepositoryCustom {
    CodeMaster findByIdAndDeleteYnFalse(Long id);
}