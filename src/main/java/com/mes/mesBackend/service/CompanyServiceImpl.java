package com.mes.mesBackend.service;

import com.mes.mesBackend.entity.CompanyVo;
import com.mes.mesBackend.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class CompanyServiceImpl implements  CompanyService{

    @Autowired
    private CompanyRepository companyRepository;

    public List<CompanyVo> FindAll() {
        return (List<CompanyVo>) companyRepository.findAll();
    }

    public CompanyVo Find(UUID id){
        return companyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }

    public CompanyVo Update(UUID id, CompanyVo company){
        CompanyVo findCompany = Find(id);
        findCompany.setCellphoneNumber(company.getCellphoneNumber());
        findCompany.setUseYn(company.isUseYn());
        return companyRepository.save(findCompany);
    }

    public void Delete(UUID id){ companyRepository.deleteById(id); }

    public CompanyVo Save(CompanyVo company){ return companyRepository.save(company); }
}
