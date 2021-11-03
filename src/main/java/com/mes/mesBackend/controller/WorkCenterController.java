package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.WorkCenterRequest;
import com.mes.mesBackend.dto.response.WorkCenterResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.WorkCenterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 3-3-1. 작업장 등록
@RestController
@RequestMapping("/work-centers")
@Api(tags = "work-center")
@RequiredArgsConstructor
public class WorkCenterController {

    @Autowired
    WorkCenterService workCenterService;

    // 작업장 생성
    @PostMapping
    @ResponseBody()
    @ApiOperation(value = "작업장 생성")
    public ResponseEntity<WorkCenterResponse> createWorkCenter(
            @RequestBody WorkCenterRequest workCenterRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(workCenterService.createWorkCenter(workCenterRequest), HttpStatus.OK);
    }

    // 작업장 단일 조회
    @GetMapping("/{work-center-id}")
    @ResponseBody()
    @ApiOperation(value = "작업장 단일 조회")
    public ResponseEntity<WorkCenterResponse> getWorkCenter(
            @RequestParam Long workCenterCodeId,
            @PathVariable(value = "work-center-id") Long workCenterId
    ) throws NotFoundException, BadRequestException {
        return new ResponseEntity<>(workCenterService.getWorkCenter(workCenterCodeId, workCenterId), HttpStatus.OK);
    }

    // 작업장 페이징 조회
    @GetMapping
    @ResponseBody()
    @ApiOperation(value = "작업장 페이징 조회")
    public ResponseEntity<Page<WorkCenterResponse>> getWorkCenters(Pageable pageable) {
        return new ResponseEntity<>(workCenterService.getWorkCenters(pageable), HttpStatus.OK);
    }

    // 작업장 수정
    @PutMapping("/{work-center-id}")
    @ResponseBody()
    @ApiOperation(value = "작업장 수정")
    public ResponseEntity<WorkCenterResponse> updateWorkCenter(
            @RequestParam Long workCenterCodeId,
            @PathVariable(value = "work-center-id") Long workCenterId,
            @RequestBody WorkCenterRequest workCenterRequest
    ) throws NotFoundException, BadRequestException {
        return new ResponseEntity<>(workCenterService.updateWorkCenter(workCenterCodeId, workCenterId, workCenterRequest), HttpStatus.OK);
    }

    // 작업장 삭제
    @DeleteMapping("/{work-center-id}")
    @ResponseBody()
    @ApiOperation(value = "작업장 삭제")
    public ResponseEntity<Void> deleteWorkCenter(
            @RequestParam Long workCenterCodeId,
            @PathVariable(value = "work-center-id") Long workCenterId
    ) throws NotFoundException, BadRequestException {
        workCenterService.deleteWorkCenter(workCenterCodeId, workCenterId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
