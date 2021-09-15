package com.mes.mesBackend.controller;

import com.mes.mesBackend.entity.CompanyVo;
import com.mes.mesBackend.service.CompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping(value = "/companies")
@Api(tags = {"company"})
@RequiredArgsConstructor
public class CompanyController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CompanyService companyService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody()
    @ApiOperation(value = "거래처 목록 조회")
    public ResponseEntity<Page<CompanyVo>> getCompanies(Pageable pageable){
        return new ResponseEntity<>(companyService.getCompanies(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "거래처 조회")
    public ResponseEntity<CompanyVo> getCompany(@PathVariable(value = "id") UUID id) {
        return new ResponseEntity<>(companyService.getCompany(id), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody()
    @ApiOperation(value = "거래처 생성", notes = "id는 빈값으로 넘겨야함")
    public ResponseEntity<CompanyVo> createCompany(@RequestBody CompanyVo company){
        return new ResponseEntity<>(companyService.createCompany(company), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "거래처 수정", notes = "cellphoneNumber만 변경 가능 / id는 빈값으로 넘겨야함")
    public ResponseEntity<CompanyVo> updateCompany(@PathVariable(value = "id") UUID id, @RequestBody CompanyVo company){
        return new ResponseEntity<>(companyService.updateCompany(id, company), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "거래처 삭제")
    public ResponseEntity deleteCompany(@PathVariable(value = "id") UUID id) {
        companyService.deleteCompany(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
