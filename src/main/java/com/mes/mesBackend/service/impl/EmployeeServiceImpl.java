package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.EmployeeRequest;
import com.mes.mesBackend.dto.response.EmployeeResponse;
import com.mes.mesBackend.entity.Department;
import com.mes.mesBackend.entity.Employee;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.Mapper;
import com.mes.mesBackend.repository.DepartmentRepository;
import com.mes.mesBackend.repository.EmployeeRepository;
import com.mes.mesBackend.service.DepartmentService;
import com.mes.mesBackend.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired EmployeeRepository employeeRepository;
    @Autowired DepartmentService departmentService;
    @Autowired Mapper mapper;


    private Employee findByIdAndDeleteYnFalse(Long id) throws NotFoundException {
        Employee findEmployee = employeeRepository.findByIdAndDeleteYnFalse(id);
        if (findEmployee == null) throw new NotFoundException("employee does not exists. input id: " + id);
        return findEmployee;
    }

    // 직원(작업자) 생성
    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) throws NotFoundException {
        Department department = departmentService.findByIdAndDeleteYnFalse(employeeRequest.getDeptId());
        Employee employee = mapper.toEntity(employeeRequest, Employee.class);
        employee.setDepartment(department);
        Employee saveEmp = employeeRepository.save(employee);
        return mapper.toResponse(saveEmp, EmployeeResponse.class);
    }

    // 직원(작업자) 단일 조회
    public EmployeeResponse getEmployee(Long id) throws NotFoundException {
        Employee employee = findByIdAndDeleteYnFalse(id);
        return mapper.toResponse(employee, EmployeeResponse.class);
    }

    // 직원(작업자) 페이징 조회
    public Page<EmployeeResponse> getEmployees(Pageable pageable) {
        Page<Employee> employees = employeeRepository.findAllByDeleteYnFalse(pageable);
        return mapper.toPageResponses(employees, EmployeeResponse.class);
    }

    // 직원(작업자) 수정
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest employeeRequest) throws NotFoundException {
        Department newDepartment = departmentService.findByIdAndDeleteYnFalse(employeeRequest.getDeptId());
        Employee newEmp = mapper.toEntity(employeeRequest, Employee.class);
        Employee findEmp = findByIdAndDeleteYnFalse(id);
        findEmp.put(newEmp, newDepartment);
        Employee saveEmp = employeeRepository.save(findEmp);
        return mapper.toResponse(saveEmp, EmployeeResponse.class);
    }

    // 직원(작업자) 삭제
    public void deleteEmployee(Long id) throws NotFoundException {
        Employee employee = findByIdAndDeleteYnFalse(id);
        employee.setDeleteYn(true);
    }
}
