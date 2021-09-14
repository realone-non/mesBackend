package com.mes.mesBackend.controller;

import com.mes.mesBackend.entity.CompanyVo;
import com.mes.mesBackend.service.CompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/company")
@Api(tags = {"거래처 컨트롤러"})
@RequiredArgsConstructor
public class CompanyController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CompanyService companyService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody()
    @ApiOperation(value = "거래처 목록")
    public ResponseEntity<List<CompanyVo>> GetCompanys(){
        return new ResponseEntity<List<CompanyVo>>(companyService.FindAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "거래처 가져오기")
    public ResponseEntity<CompanyVo> GetCompanyInfo(@PathVariable(value = "id") UUID id) {
        return new ResponseEntity<>(companyService.Find(id), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody()
    @ApiOperation(value = "거래처 등록")
    public ResponseEntity<CompanyVo> SetCompany(CompanyVo company){
        return new ResponseEntity<CompanyVo>(companyService.Save(company), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "거래처 수정", notes = "테스트를 위해서 cellphoneNumber, useYN 컬럼만 변경하게끔 구현")
    public ResponseEntity<CompanyVo> ModifyCompany(@PathVariable(value = "id") UUID id, CompanyVo company){
        return new ResponseEntity<>(companyService.Update(id, company), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "거래처 삭제")
    public ResponseEntity DeleteCompany(@PathVariable(value = "id") UUID id) {
        companyService.Delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
