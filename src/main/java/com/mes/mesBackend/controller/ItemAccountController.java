package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.ItemAccountRequest;
import com.mes.mesBackend.dto.response.ItemAccountResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.ItemAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 품목계정
@RestController
@RequestMapping(value = "/item-accounts")
@Api(tags = "item-account")
@RequiredArgsConstructor
public class ItemAccountController {
    @Autowired
    ItemAccountService itemAccountService;

    // 품목계정 생성
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "품목계정 생성")
    public ResponseEntity<ItemAccountResponse> createItemAccount(
            @RequestBody ItemAccountRequest itemAccountRequest
    ) {
        return new ResponseEntity<>(itemAccountService.createItemAccount(itemAccountRequest), HttpStatus.OK);
    }

    // 품목계정 단일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "품목계정 단일 조회")
    public ResponseEntity<ItemAccountResponse> getItemAccount(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(itemAccountService.getItemAccount(id), HttpStatus.OK);
    }

    // 품목계정 리스트 조회
    @GetMapping
    @ResponseBody
    @ApiOperation(value = "품목계정 리스트 조회")
    public ResponseEntity<List<ItemAccountResponse>> getItemAccounts() {
        return new ResponseEntity<>(itemAccountService.getItemAccounts(), HttpStatus.OK);
    }

    // 품목계정 수정
    @PutMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "품목계정 수정")
    public ResponseEntity<ItemAccountResponse> updateItemAccount(
            @PathVariable Long id,
            @RequestBody ItemAccountRequest itemAccountRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(itemAccountService.updateItemAccount(id, itemAccountRequest), HttpStatus.OK);
    }

    // 품목계정 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "품목계정 삭제")
    public ResponseEntity deleteItemAccount(
            @PathVariable Long id
    ) throws NotFoundException {
        itemAccountService.deleteItemAccount(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
