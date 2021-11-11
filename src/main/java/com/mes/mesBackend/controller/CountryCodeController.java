package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.CountryCodeRequest;
import com.mes.mesBackend.dto.response.CountryCodeResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.CountryCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/country-codes")
@Api(tags = "country-code")
@RequiredArgsConstructor
public class CountryCodeController {

    @Autowired
    CountryCodeService countryCodeService;

    // 국가코드 생성
    @PostMapping
    @ResponseBody()
    @ApiOperation(value = "국가코드 생성")
    public ResponseEntity<CountryCodeResponse> createCountryCode(
            @RequestBody @Valid CountryCodeRequest countryCodeRequest
    ) {
        return new ResponseEntity<>(countryCodeService.createCountryCode(countryCodeRequest), HttpStatus.OK);
    }

    // 국가코드 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "국가코드 조회")
    public ResponseEntity<CountryCodeResponse> getCountryCode(@PathVariable(value = "id") Long id) throws NotFoundException {
        return new ResponseEntity<>(countryCodeService.getCountryCode(id), HttpStatus.OK);
    }

//    국가코드 페이징 조회
    @GetMapping
    @ResponseBody()
    @ApiOperation(value = "국가코드 페이징 조회")
    public ResponseEntity<Page<CountryCodeResponse>> getCountryCodes(Pageable pageable) {
        return new ResponseEntity<>(countryCodeService.getCountryCodes(pageable), HttpStatus.OK);
    }

    // 국가코드 수정
    @PatchMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "국가코드 수정")
    public ResponseEntity<CountryCodeResponse> updateCountryCode(
            @PathVariable(value = "id") Long id,
            @RequestBody @Valid CountryCodeRequest countryCodeRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(countryCodeService.updateCountryCode(id, countryCodeRequest), HttpStatus.OK);
    }

    // 국가코드 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "국가코드 삭제")
    public ResponseEntity<Void> deleteCountryCode(@PathVariable(value = "id") Long id) throws NotFoundException {
        countryCodeService.deleteCountryCode(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
