package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.CodeRequest;
import com.mes.mesBackend.dto.response.CodeResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.ItemGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 3-2-2. 그룹코드 등록
@RestController
@RequestMapping("/item-group-codes")
@Api(tags = "item-group-code")
@RequiredArgsConstructor
public class ItemGroupCodeController {
    @Autowired
    ItemGroupService itemGroupCodeService;

    // 그룹코드 생성
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "그룹코드 생성")
    public ResponseEntity<CodeResponse> createItemGroupCode(
            @RequestBody CodeRequest codeRequest
    ) {
        return new ResponseEntity<>(itemGroupCodeService.createItemGroupCode(codeRequest), HttpStatus.OK);
    }

    // 그룹코드 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "그룹코드 조회")
    public ResponseEntity<CodeResponse> getItemGroupCode(
            @PathVariable Long id
    ) throws NotFoundException {
        return new ResponseEntity<>(itemGroupCodeService.getItemGroupCode(id), HttpStatus.OK);
    }

    // 그룹코드 리스트 조회
    @GetMapping
    @ResponseBody()
    @ApiOperation(value = "그룹코드 리스트 조회")
    public ResponseEntity<List<CodeResponse>> getItemGroupCodes() {
        return new ResponseEntity<>(itemGroupCodeService.getItemGroupCodes(), HttpStatus.OK);
    }

    // 그룹코드 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "그룹코드 삭제")
    public ResponseEntity deleteItemGroupCode(
            @PathVariable Long id
    ) throws NotFoundException, BadRequestException {
        itemGroupCodeService.deleteItemGroupCode(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
