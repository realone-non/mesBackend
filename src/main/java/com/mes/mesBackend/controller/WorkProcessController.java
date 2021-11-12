package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.WorkProcessRequest;
import com.mes.mesBackend.dto.response.WorkProcessResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.WorkProcessService;
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

// 3-3-2. 작업 공정 등록
@RestController
@RequestMapping("/work-processes")
@Api(tags = "work-process")
@RequiredArgsConstructor
public class WorkProcessController {

    @Autowired
    WorkProcessService workProcessService;

    // 작업공정 생성
    @PostMapping
    @ResponseBody()
    @ApiOperation(value = "작업공정 생성")
    public ResponseEntity<WorkProcessResponse> createWorkProcess(
            @RequestBody @Valid WorkProcessRequest workProcessRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(workProcessService.createWorkProcess(workProcessRequest), HttpStatus.OK);
    }

    // 작업공정 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "작업공정 단일 조회")
    public ResponseEntity<WorkProcessResponse> getWorkProcess(
            @PathVariable Long id
    ) throws NotFoundException {
        return new ResponseEntity<>(workProcessService.getWorkProcess(id), HttpStatus.OK);
    }

    // 작업공정 페이징 조회
    @GetMapping
    @ResponseBody()
    @ApiOperation(value = "작업공정 페이징 조회")
    public ResponseEntity<Page<WorkProcessResponse>> getWorkProcesses(@PageableDefault Pageable pageable) {
        return new ResponseEntity<>(workProcessService.getWorkProcesses(pageable), HttpStatus.OK);
    }

    // 작업공정 수정
    @PatchMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "작업공정 수정")
    public ResponseEntity<WorkProcessResponse> updateWorkProcess(
            @PathVariable Long id,
            @RequestBody @Valid WorkProcessRequest workProcessRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(workProcessService.updateWorkProcess(id, workProcessRequest), HttpStatus.OK);
    }

    // 작업공정 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "작업공정 삭제")
    public ResponseEntity<Void> deleteWorkProcess(
            @PathVariable Long id
    ) throws NotFoundException {
        workProcessService.deleteWorkProcess(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
