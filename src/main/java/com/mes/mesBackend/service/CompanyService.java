package com.mes.mesBackend.service;

import com.mes.mesBackend.entity.CompanyVo;

import java.util.List;
import java.util.UUID;

public interface CompanyService {
    List<CompanyVo> FindAll();
    CompanyVo Find(UUID id);
    CompanyVo Update(UUID id, CompanyVo company);
    void Delete(UUID id);
    CompanyVo Save(CompanyVo company);
}
