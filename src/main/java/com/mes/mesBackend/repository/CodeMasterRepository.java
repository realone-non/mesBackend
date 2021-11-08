package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.CodeMaster;
import com.mes.mesBackend.repository.custom.CodeMasterRepositoryCustom;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeMasterRepository extends JpaCustomRepository<CodeMaster, Long>, CodeMasterRepositoryCustom {
}