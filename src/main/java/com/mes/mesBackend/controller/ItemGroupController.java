package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.ItemGroupRequest;
import com.mes.mesBackend.dto.response.ItemGroupResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.ItemGroupService;
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

import javax.validation.Valid;

// 3-2-2. 품목그룹 등록
@RestController
@RequestMapping("/item-groups")
@Api(tags = "item-group")
@RequiredArgsConstructor
public class ItemGroupController {
    @Autowired
    ItemGroupService itemGroupService;

    // 품목그룹 생성
    @PostMapping
    @ResponseBody()
    @ApiOperation(value = "품목그룹 생성")
    public ResponseEntity<ItemGroupResponse> createItemGroup(
            @RequestBody @Valid ItemGroupRequest itemGroupRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(itemGroupService.createItemGroup(itemGroupRequest), HttpStatus.OK);
    }

    // 품목그룹 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "품목그룹 단일 조회")
    public ResponseEntity<ItemGroupResponse> getItemGroup(
            @PathVariable Long id
    ) throws NotFoundException {
        return new ResponseEntity<>(itemGroupService.getItemGroup(id), HttpStatus.OK);
    }

    // 품목그룹 페이징 조회
    @GetMapping
    @ResponseBody()
    @ApiOperation(value = "품목그룹 페이징 조회")
    public ResponseEntity<Page<ItemGroupResponse>> getItemGroups(@PageableDefault Pageable pageable) {
        return new ResponseEntity<>(itemGroupService.getItemGroups(pageable), HttpStatus.OK);
    }

    // 품목그룹 수정
    @PatchMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "품목그룹 수정")
    public ResponseEntity<ItemGroupResponse> updateItemGroup(
            @PathVariable Long id,
            @RequestBody @Valid ItemGroupRequest itemGroupRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(itemGroupService.updateItemGroup(id, itemGroupRequest), HttpStatus.OK);
    }

    // 품목그룹 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "품목그룹 삭제")
    public ResponseEntity<Void> deleteItemGroup(
            @PathVariable Long id
    ) throws NotFoundException {
        itemGroupService.deleteItemGroup(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    
}
