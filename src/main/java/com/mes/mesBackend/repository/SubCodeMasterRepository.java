package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.CodeMaster;
import com.mes.mesBackend.entity.SubCodeMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCodeMasterRepository extends JpaRepository<SubCodeMaster, Long> {
    SubCodeMaster findByIdAndDeleteYnFalse(Long id);
    List<SubCodeMaster> findSubCodeMasterByCodeMasterAndDeleteYnFalse(CodeMaster codeMaster);
}