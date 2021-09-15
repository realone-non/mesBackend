package com.mes.mesBackend.service;

import com.mes.mesBackend.entity.CompanyVo;
import com.mes.mesBackend.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class CompanyServiceImpl implements  CompanyService{

    @Autowired
    private CompanyRepository companyRepository;

    public Page<CompanyVo> getCompanies(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }

    public CompanyVo getCompany(UUID id){
        return companyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }

    public CompanyVo updateCompany(UUID id, CompanyVo company){
        CompanyVo findCompany = getCompany(id);
        findCompany.setCellphoneNumber(company.getCellphoneNumber());
        return companyRepository.save(findCompany);
    }

    public void deleteCompany(UUID id) {
        companyRepository.deleteById(id);
    }

    public CompanyVo createCompany(CompanyVo company) {
        return companyRepository.save(company);
    }
}
