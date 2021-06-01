package com.mes.mesBackend.controller;

import com.mes.mesBackend.entity.CompanyVo;
import com.mes.mesBackend.entity.UserVo;
import com.mes.mesBackend.repository.UserRepository;
import com.mes.mesBackend.service.CompanyService;
import com.mes.mesBackend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/company")
@Api(tags = {"거래처 컨트롤러"})
@RequiredArgsConstructor
public class CompanyController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CompanyService companyService;

    @RequestMapping(value = "/company", method = RequestMethod.GET)
    @ResponseBody()
    @ApiOperation(value = "거래처 목록")
    public ResponseEntity<List<CompanyVo>> GetCompanys(){
        return new ResponseEntity<List<CompanyVo>>(companyService.FindAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/company/{id}", method = RequestMethod.GET)
    @ResponseBody()
    @ApiOperation(value = "거래처 가져오기")
    public ResponseEntity<Optional<CompanyVo>> GetCompanyInfo(String id){
        return new ResponseEntity<Optional<CompanyVo>>(companyService.Find(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/company", method = RequestMethod.POST)
    @ResponseBody()
    @ApiOperation(value = "거래처 등록")
    public ResponseEntity<CompanyVo> SetCompany(CompanyVo company){
        return new ResponseEntity<CompanyVo>(companyService.Save(company), HttpStatus.OK);
    }

    @RequestMapping(value = "/company", method = RequestMethod.PUT)
    @ResponseBody()
    @ApiOperation(value = "거래처 수정")
    public ResponseEntity ModifyCompany(CompanyVo company){
        companyService.Update(company);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
