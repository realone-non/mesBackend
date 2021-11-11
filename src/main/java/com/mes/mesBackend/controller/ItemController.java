package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.ItemFileRequest;
import com.mes.mesBackend.dto.request.ItemRequest;
import com.mes.mesBackend.dto.response.ItemFileResponse;
import com.mes.mesBackend.dto.response.ItemResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

// 3-2-1. 품목 등록
@RestController
@RequestMapping(value = "/items")
@Api(tags = "item")
@RequiredArgsConstructor
public class ItemController {
    @Autowired
    ItemService itemService;

    // 품목 생성
    @PostMapping
    @ResponseBody
    @ApiOperation(
            value = "품목 생성",
            notes = "not Null field:\nitemNo, itemName, itemAccount, unit, uhp, validDay, " +
            "lotType, inputTest, outputTest, wasteProductLot, developStatus, stockControl, " +
                    "inputUnitPrice, storageLocation, manufacturerPartNo, manufacturer, useYn " +
                    "inputTestType, outputTestType:\n" +
                    "자동검사: AUTOMATIC_TEST, 수동검사: MANUAL_TEST, 검사없음: NO_TEST\n" +
                    "developStatus:\n" +
                    "미개발: BEFORE, 개발중: PROCEEDING, 개발완료: COMPLETION"
    )
    public ResponseEntity<ItemResponse> createItem(
            @Valid @RequestBody ItemRequest itemRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(itemService.createItem(itemRequest), HttpStatus.OK);
    }

    // 품목 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "품목 조회")
    public ResponseEntity<ItemResponse> getItem(@PathVariable(value = "id") Long id) throws NotFoundException {
        return new ResponseEntity<>(itemService.getItem(id), HttpStatus.OK);
    }

    // 품목 페이징 조회
    @GetMapping
    @ResponseBody()
    @ApiOperation(value = "품목 페이징 조회", notes = "검색조건: 품목그룹, 품목계정, 품번, 품명, 검색어")
    public ResponseEntity<Page<ItemResponse>> getItems(
            @RequestParam(required = false) Long itemGroupId,
            @RequestParam(required = false) Long itemAccountId,
            @RequestParam(required = false) String itemNo,
            @RequestParam(required = false) String itemName,
            @RequestParam(required = false) String searchWord,
            @PageableDefault Pageable pageable
    ) {
        return new ResponseEntity<>(itemService.getItems(itemGroupId, itemAccountId, itemNo, itemName, searchWord, pageable), HttpStatus.OK);
    }

    // 품목 수정
    @PatchMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "품목 수정")
    public ResponseEntity<ItemResponse> updateItem(
            @PathVariable(value = "id") Long id,
            @RequestBody @Valid ItemRequest itemRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(itemService.updateItem(id, itemRequest), HttpStatus.OK);
    }

    // 품목 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "품목 삭제")
    public ResponseEntity<Void> deleteItem(@PathVariable(value = "id") Long id) throws NotFoundException {
        itemService.deleteItem(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 파일 정보 생성
    @PostMapping("/{item-id}/item-files")
    @ResponseBody
    @ApiOperation(value = "품목 파일 정보 생성")
    public ResponseEntity<ItemFileResponse> createItemFileInfo(
            @PathVariable(value = "item-id") Long itemId,
            @RequestBody @Valid ItemFileRequest itemFileRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(itemService.createItemFileInfo(itemId, itemFileRequest), HttpStatus.OK);
    }

    // 파일 생성
    @PutMapping("/{item-id}/item-files/{item-file-id}")
    @ResponseBody
    @ApiOperation(value = "품목 파일 생성")
    public ResponseEntity<ItemFileResponse> createItemFile(
            @PathVariable(value = "item-id") Long itemId,
            @PathVariable(value = "item-file-id") Long itemFileId,
            @RequestPart MultipartFile file
    ) throws NotFoundException, IOException, BadRequestException {
        return new ResponseEntity<>(itemService.createFile(itemId, itemFileId, file), HttpStatus.OK);
    }

    // 파일 리스트 조회
    @GetMapping("/{item-id}/item-files")
    @ResponseBody
    @ApiOperation(value = "품목 파일 리스트 조회")
    public ResponseEntity<List<ItemFileResponse>> getItemFiles(
            @PathVariable(value = "item-id") Long itemId
    ) throws NotFoundException {
        return new ResponseEntity<>(itemService.getItemFiles(itemId), HttpStatus.OK);
    }

    // 품목 파일 정보 수정
    @PatchMapping("/{item-id}/item-files/{item-file-id}")
    @ResponseBody
    @ApiOperation(value = "품목 파일 정보 수정")
    public ResponseEntity<ItemFileResponse> updateItemFileInfo(
            @PathVariable(value = "item-id") Long itemId,
            @PathVariable(value = "item-file-id") Long itemFileId,
            @RequestBody @Valid ItemFileRequest itemFileRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(itemService.updateItemFileInfo(itemId, itemFileId, itemFileRequest), HttpStatus.OK);
    }

    // 파일 삭제
    @DeleteMapping("/{item-id}/item-files/{item-file-id}")
    @ResponseBody
    @ApiOperation(value = "품목 파일 삭제")
    public ResponseEntity deleteItemFile(
            @PathVariable(value = "item-id") Long itemId,
            @PathVariable(value = "item-file-id") Long itemFileId
    ) throws NotFoundException {
        itemService.deleteItemFile(itemId, itemFileId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
