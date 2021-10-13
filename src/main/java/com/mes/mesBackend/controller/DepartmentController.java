package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.DepartmentRequest;
import com.mes.mesBackend.dto.response.DepartmentResponse;
import com.mes.mesBackend.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 부서등록
@RestController
@RequestMapping(value = "/department")
@Api(tags = "department")
@RequiredArgsConstructor
public class DepartmentController {

    @Autowired DepartmentService departmentService;

    // 부서 생성
    @PostMapping
    @ResponseBody()
    @ApiOperation(value = "부서 생성")
    public ResponseEntity<DepartmentResponse> createDepartment(@RequestBody DepartmentRequest departmentRequest) {
        return new ResponseEntity<>(departmentService.createDepartment(departmentRequest), HttpStatus.OK);
    }

    // 부서 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "부서 조회")
    public ResponseEntity<DepartmentResponse> getDepartment(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(departmentService.getDepartment(id), HttpStatus.OK);
    }

    // 부서 페이징 조회
    @GetMapping
    @ResponseBody()
    @ApiOperation(value = "부서 페이징 조회")
    public ResponseEntity<Page<DepartmentResponse>> getDepartments(Pageable pageable) {
        return new ResponseEntity<>(departmentService.getDepartments(pageable), HttpStatus.OK);
    }

    // 부서 수정
    @PutMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "부서 수정")
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @PathVariable Long id,
            @RequestBody DepartmentRequest departmentRequest
    ) {
        return new ResponseEntity<>(departmentService.updateDepartment(id, departmentRequest), HttpStatus.OK);
    }

    // 부서 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "부서 삭제")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
