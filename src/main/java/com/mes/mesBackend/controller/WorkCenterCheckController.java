package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.WorkCenterCheckDetailRequest;
import com.mes.mesBackend.dto.response.WorkCenterCheckDetailResponse;
import com.mes.mesBackend.dto.response.WorkCenterCheckResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.WorkCenterCheckService;
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
import java.util.List;

// 3-3-4. 작업장별 점검 항목 등록
@RestController
@RequestMapping("/work-center-checks")
@Api(tags = "work-center-check")
@RequiredArgsConstructor
public class WorkCenterCheckController {

    @Autowired
    WorkCenterCheckService workCenterCheckService;

    // 작업장별 점검유형 생성
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "작업장별 점검유형 생성")
    public ResponseEntity<WorkCenterCheckResponse> createWorkCenterCheck(
            @RequestParam Long workCenterId,
            @RequestParam Long checkTypeId
    ) throws NotFoundException {
        return new ResponseEntity<>(workCenterCheckService.createWorkCenterCheck(workCenterId, checkTypeId), HttpStatus.OK);
    }

    // 작업장별 점검유형 단일 조회
    @GetMapping("/{work-center-check-id}")
    @ResponseBody()
    @ApiOperation(value = "작업장별 점검유형 단일 조회")
    public ResponseEntity<WorkCenterCheckResponse> getWorkCenterCheck(
            @PathVariable(value = "work-center-check-id") Long workCenterCheckId
    ) throws NotFoundException {
        return new ResponseEntity<>(workCenterCheckService.getWorkCenterCheck(workCenterCheckId), HttpStatus.OK);
    }

    // 작업장별 점검유형 페이징 조회/ 검색: 작업장, 점검유형
    @GetMapping
    @ResponseBody
    @ApiOperation(value = "작업장별 점검유형 페이징 조회", notes = "검색조건: 작업장, 점검유형")
    public ResponseEntity<Page<WorkCenterCheckResponse>> getWorkCenterChecks(
            @RequestParam(required = false) Long workCenterId,
            @RequestParam(required = false) Long checkTypeId,
            @PageableDefault Pageable pageable
    ) throws NotFoundException {
        return new ResponseEntity<>(workCenterCheckService.getWorkCenterChecks(workCenterId, checkTypeId, pageable), HttpStatus.OK);
    }

    // 작업장별 점검유형 수정
    @PatchMapping("/{work-center-check-id}")
    @ResponseBody
    @ApiOperation(
            value = "작업장별 점검유형 수정",
            notes = "checkTypeId: 작업유형 id / workCenterId: 작업장 id")
    public ResponseEntity<WorkCenterCheckResponse> updateWorkCenterCheck(
            @PathVariable(value = "work-center-check-id") Long workCenterCheckId,
            @RequestParam Long workCenterId,
            @RequestParam Long checkTypeId
    ) throws NotFoundException {
        return new ResponseEntity<>(workCenterCheckService.updateWorkCenterCheck(workCenterCheckId, workCenterId, checkTypeId), HttpStatus.OK);
    }

    // 작업장별 점검유형 삭제
    @DeleteMapping("/{work-center-check-id}")
    @ResponseBody
    @ApiOperation(value = "작업장별 점검유형 삭제")
    public ResponseEntity deleteWorkCenterCheck(@PathVariable(value = "work-center-check-id") Long id) throws NotFoundException {
        workCenterCheckService.deleteWorkCenterCheck(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 작업장별 점검유형 세부 생성
    @PostMapping("/{work-center-check-id}/work-center-check-details")
    @ResponseBody
    @ApiOperation(value = "작업장별 점검유형 세부 생성", notes = "lsl, usl 소수점 3자리까지 기입가능")
    public ResponseEntity<WorkCenterCheckDetailResponse> createWorkCenterCheckDetail(
            @PathVariable(value = "work-center-check-id") Long workCenterCheckId,
            @RequestBody @Valid WorkCenterCheckDetailRequest workCenterCheckDetailRequest
    ) throws NotFoundException, BadRequestException {
        return new ResponseEntity<>(workCenterCheckService.createWorkCenterCheckDetail(workCenterCheckId,workCenterCheckDetailRequest), HttpStatus.OK);
    }

//    // 작업장별 점검유형 세부 단일 조회 api
//    @GetMapping("/{work-center-check-id}/work-center-check-details/{work-center-check-detail-id}")
//    @ResponseBody
//    @ApiOperation(value = "작업장별 점검유형 세부 단일 조회")
//    public ResponseEntity<WorkCenterCheckDetailResponse> getWorkCenterCheckDetail(
//            @PathVariable(value = "work-center-check-id") Long workCenterCheckId,
//            @PathVariable(value = "work-center-check-detail-id") Long workCenterCheckDetailId
//    ) throws NotFoundException {
//        return new ResponseEntity<>(workCenterCheckService.getWorkCenterCheckDetail(workCenterCheckId, workCenterCheckDetailId), HttpStatus.OK);
//    }

    // 작업장별 점검유형 세부 리스트 조회
    @GetMapping("/{work-center-check-id}/work-center-check-details")
    @ResponseBody
    @ApiOperation(value = "작업장별 점검유형 세부 리스트 조회", notes = "작업장별 점검유형에 해당하는 세부내역 전체조회")
    public ResponseEntity<List<WorkCenterCheckDetailResponse>> getWorkCenterCheckDetails(
            @PathVariable(value = "work-center-check-id") Long workCenterCheckId
    ) throws NotFoundException {
        return new ResponseEntity<>(workCenterCheckService.getWorkCenterCheckDetails(workCenterCheckId), HttpStatus.OK);
    }

    // 작업장별 점검유형 세부 수정
    @PatchMapping("/{work-center-check-id}/work-center-check-details/{work-center-check-detail-id}")
    @ResponseBody
    @ApiOperation(value = "작업장별 점검유형 세부 수정")
    public ResponseEntity<WorkCenterCheckDetailResponse> updateWorkCenterCheckDetail(
            @PathVariable(value = "work-center-check-id") Long workCenterCheckId,
            @PathVariable(value = "work-center-check-detail-id") Long workCenterCheckDetailId,
            @RequestBody @Valid WorkCenterCheckDetailRequest workCenterCheckDetailRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(workCenterCheckService.updateWorkCenterCheckDetail(workCenterCheckId, workCenterCheckDetailId, workCenterCheckDetailRequest), HttpStatus.OK);
    }

    // 작업장별 점검유형 세부 삭제
    @DeleteMapping("/{work-center-check-id}/work-center-check-details/{work-center-check-detail-id}")
    @ResponseBody
    @ApiOperation(value = "작업장별 점검유형 세부 삭제")
    public ResponseEntity deleteWorkCenterCheckDetail(
            @PathVariable(value = "work-center-check-id") Long workCenterCheckId,
            @PathVariable(value = "work-center-check-detail-id") Long workCenterCheckDetailId
    ) throws NotFoundException {
        workCenterCheckService.deleteWorkCenterCheckDetail(workCenterCheckId, workCenterCheckDetailId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
