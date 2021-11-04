package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.WorkLineRequest;
import com.mes.mesBackend.dto.response.WorkLineResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.WorkLineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 3-3-3. 작업라인 등록
@RestController
@RequestMapping("/work-lines")
@Api(tags = "work-line")
@RequiredArgsConstructor
public class WorkLineController {
    @Autowired
    WorkLineService workLineService;

    // 작업라인 생성
    @PostMapping
    @ResponseBody()
    @ApiOperation(value = "작업라인 생성")
    public ResponseEntity<WorkLineResponse> createWorkLine(
            @RequestBody WorkLineRequest workLineRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(workLineService.createWorkLine(workLineRequest), HttpStatus.OK);
    }

    // 작업라인 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "작업라인 단일 조회")
    public ResponseEntity<WorkLineResponse> getWorkLine(
            @PathVariable Long id
    ) throws NotFoundException {
        return new ResponseEntity<>(workLineService.getWorkLine(id), HttpStatus.OK);
    }

    // 작업라인 페이징 조회
    @GetMapping
    @ResponseBody()
    @ApiOperation(value = "작업라인 페이징 조회")
    public ResponseEntity<Page<WorkLineResponse>> getWorkLines(Pageable pageable) {
        return new ResponseEntity<>(workLineService.getWorkLines(pageable), HttpStatus.OK);
    }

    // 작업라인 수정
    @PutMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "작업라인 수정")
    public ResponseEntity<WorkLineResponse> updateWorkLine(
            @PathVariable Long id,
            @RequestBody WorkLineRequest workLineRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(workLineService.updateWorkLine(id, workLineRequest), HttpStatus.OK);
    }

    // 작업라인 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "작업라인 삭제")
    public ResponseEntity<Void> deleteWorkLine(
            @PathVariable Long id
    ) throws NotFoundException {
        workLineService.deleteWorkLine(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
