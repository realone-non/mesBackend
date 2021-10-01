package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.CodeMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeMasterRepository extends JpaRepository<CodeMaster, String> {
}