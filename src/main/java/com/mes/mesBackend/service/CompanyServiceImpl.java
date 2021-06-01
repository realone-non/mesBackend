package com.mes.mesBackend.service;

import com.mes.mesBackend.entity.CompanyVo;
import com.mes.mesBackend.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements  CompanyService{

    @Autowired
    private CompanyRepository companyRepository;

    public List<CompanyVo> FindAll(){
        return (List<CompanyVo>) companyRepository.findAll();
    }

    public Optional<CompanyVo> Find(String id){
        return companyRepository.findByCompanyId(id);
    }

    public void Update(CompanyVo company){

    }

    public void Delete(String id){ companyRepository.deleteById(id); }
    public CompanyVo Save(CompanyVo company){ return companyRepository.save(company); }
}
