package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.CompanyVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyVo, UUID> {
}
