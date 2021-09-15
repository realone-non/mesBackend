package com.mes.mesBackend.service;

import com.mes.mesBackend.entity.CompanyVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CompanyService {
    Page<CompanyVo> getCompanies(Pageable pageable);
    CompanyVo getCompany(UUID id);
    CompanyVo updateCompany(UUID id, CompanyVo company);
    void deleteCompany(UUID id);
    CompanyVo createCompany(CompanyVo company);
}
