package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.ItemFormRequest;
import com.mes.mesBackend.dto.response.ItemFormResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.ItemFormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 품목형태
@RestController
@RequestMapping(value = "/item-forms")
@Api(tags = "item-form")
public class ItemFormController {
    @Autowired
    ItemFormService itemFormService;

    // 품목형태 생성
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "품목형태 생성")
    public ResponseEntity<ItemFormResponse> createItemForm(
            @RequestBody ItemFormRequest itemFormRequest
    ) {
        return new ResponseEntity<>(itemFormService.createItemForm(itemFormRequest), HttpStatus.OK);
    }

    // 품목형태 단일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "품목형태 단일 조회")
    public ResponseEntity<ItemFormResponse> getItemForm(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(itemFormService.getItemForm(id), HttpStatus.OK);
    }

    // 품목형태 리스트 조회
    @GetMapping
    @ResponseBody
    @ApiOperation(value = "품목형태 리스트 조회")
    public ResponseEntity<List<ItemFormResponse>> getItemForms() {
        return new ResponseEntity<>(itemFormService.getItemForms(), HttpStatus.OK);
    }

    // 품목형태 수정
    @PutMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "품목형태 수정")
    public ResponseEntity<ItemFormResponse> updateItemForm(
            @PathVariable Long id,
            @RequestBody ItemFormRequest itemFormRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(itemFormService.updateItemForm(id, itemFormRequest), HttpStatus.OK);
    }

    // 품목형태 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "품목형태 삭제")
    public ResponseEntity deleteItemForm(
            @PathVariable Long id
    ) throws NotFoundException {
        itemFormService.deleteItemForm(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
