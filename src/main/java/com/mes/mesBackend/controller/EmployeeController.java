package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.EmployeeRequest;
import com.mes.mesBackend.dto.response.EmployeeResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/employees")
@Api(tags = "employees")
@RequiredArgsConstructor
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    // 직원(작업자) 생성
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "직원(작업자) 생성")
    public ResponseEntity<EmployeeResponse> createEmployee(@RequestBody EmployeeRequest employeeRequest) throws NotFoundException {
        return new ResponseEntity<>(employeeService.createEmployee(employeeRequest), HttpStatus.OK);
    }

    // 직원(작업자) 단일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "직원(작업자) 단일 조회")
    public ResponseEntity<EmployeeResponse> getEmployee(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(employeeService.getEmployee(id), HttpStatus.OK);
    }

    // 직원(작업자) 페이징 조회
    @GetMapping
    @ResponseBody()
    @ApiOperation(value = "직원(작업자) 페이징 조회")
    public ResponseEntity<Page<EmployeeResponse>> getEmployees(Pageable pageable) {
        return new ResponseEntity<>(employeeService.getEmployees(pageable), HttpStatus.OK);
    }

    // 직원(작업자) 수정
    @PutMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "직원(작업자) 수정")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable(value = "id") Long id,
            @RequestBody EmployeeRequest employeeRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(employeeService.updateEmployee(id, employeeRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "직원(작업자) 삭제")
    public ResponseEntity deleteEmployee(@PathVariable(value = "id") Long id) throws NotFoundException {
        employeeService.deleteEmployee(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
