package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.CountryCodeRequest;
import com.mes.mesBackend.dto.response.CountryCodeResponse;
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

@RestController
@RequestMapping(value = "/country-code")
@Api(tags = "country-code")
@RequiredArgsConstructor
public class CountryCodeController {

    @Autowired
    CountryCodeService countryCodeService;

    @PostMapping
    @ResponseBody()
    @ApiOperation(value = "국가코드 생성")
    public ResponseEntity<CountryCodeResponse> createCountryCode(
            @RequestBody CountryCodeRequest countryCodeRequest
    ) {
        try {
            return new ResponseEntity<>(countryCodeService.createCountryCode(countryCodeRequest), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "국가코드 조회")
    public ResponseEntity<CountryCodeResponse> getCountryCode(@PathVariable(value = "id") Long id) {
        try {
            return new ResponseEntity<>(countryCodeService.getCountryCode(id), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @ResponseBody()
    @ApiOperation(value = "국가코드 리스트 조회")
    public ResponseEntity<Page<CountryCodeResponse>> getCountryCodes(Pageable pageable) {
        try {
            return new ResponseEntity<>(countryCodeService.getCountryCodes(pageable), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "국가코드 수정")
    public ResponseEntity<CountryCodeResponse> updateCountryCode(
            @PathVariable(value = "id") Long id,
            @RequestBody CountryCodeRequest countryCodeRequest
    ) {
        try {
            return new ResponseEntity<>(countryCodeService.updateCountryCode(id, countryCodeRequest), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "국가코드 삭제")
    public ResponseEntity<Void> deleteCountryCode(@PathVariable(value = "id") Long id) {
        try {
            countryCodeService.deleteCountryCode(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
