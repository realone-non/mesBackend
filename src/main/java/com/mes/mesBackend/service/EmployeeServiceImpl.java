package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.EmployeeRequest;
import com.mes.mesBackend.dto.response.EmployeeResponse;
import com.mes.mesBackend.entity.Department;
import com.mes.mesBackend.entity.Employee;
import com.mes.mesBackend.helper.Mapper;
import com.mes.mesBackend.repository.DepartmentRepository;
import com.mes.mesBackend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired EmployeeRepository employeeRepository;
    @Autowired DepartmentRepository departmentRepository;
    @Autowired DepartmentService departmentService;
    @Autowired Mapper mapper;

    // 직원(작업자) 생성
    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) {
        Department department = departmentRepository.findByIdAndDeleteYnFalse(employeeRequest.getDeptId());
        Employee employee = mapper.toEntity(employeeRequest, Employee.class);
        employee.setDepartment(department);
        Employee saveEmp = employeeRepository.save(employee);
        return mapper.toResponse(saveEmp, EmployeeResponse.class);
    }

    // 직원(작업자) 단일 조회
    public EmployeeResponse getEmployee(Long id) {
        Employee employee = employeeRepository.findByIdAndDeleteYnFalse(id);
        return mapper.toResponse(employee, EmployeeResponse.class);
    }

    // 직원(작업자) 페이징 조회
    public Page<EmployeeResponse> getEmployees(Pageable pageable) {
        Page<Employee> employees = employeeRepository.findAllByDeleteYnFalse(pageable);
        return mapper.toPageResponses(employees, EmployeeResponse.class);
    }

    // 직원(작업자) 수정
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest employeeRequest) {
        Department newDepartment = departmentRepository.findByIdAndDeleteYnFalse(employeeRequest.getDeptId());
        Employee newEmp = mapper.toEntity(employeeRequest, Employee.class);
        Employee findEmp = employeeRepository.findByIdAndDeleteYnFalse(id);
        findEmp.put(newEmp, newDepartment);
        Employee saveEmp = employeeRepository.save(findEmp);
        return mapper.toResponse(saveEmp, EmployeeResponse.class);
    }

    // 직원(작업자) 삭제
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findByIdAndDeleteYnFalse(id);
        employee.setDeleteYn(true);
    }
}
