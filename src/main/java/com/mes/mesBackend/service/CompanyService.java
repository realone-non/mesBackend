package com.mes.mesBackend.service;

import com.mes.mesBackend.entity.CompanyVo;

import java.util.List;
import java.util.Optional;

public interface CompanyService {
    List<CompanyVo> FindAll();
    Optional<CompanyVo> Find(String id);
    void Update(CompanyVo company);
    void Delete(String id);
    CompanyVo Save(CompanyVo company);
}
