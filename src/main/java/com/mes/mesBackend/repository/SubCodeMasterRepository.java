package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.SubCodeMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCodeMasterRepository extends JpaRepository<SubCodeMaster, String> {
}