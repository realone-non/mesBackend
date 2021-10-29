package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.EmployeeRequest;
import com.mes.mesBackend.dto.response.EmployeeResponse;
import com.mes.mesBackend.entity.Department;
import com.mes.mesBackend.entity.User;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
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
    @Autowired
    ModelMapper modelMapper;


    private User findByIdAndDeleteYnFalse(Long id) throws NotFoundException {
        User findUser = employeeRepository.findByIdAndDeleteYnFalse(id);
        if (findUser == null) throw new NotFoundException("employee does not exists. input id: " + id);
        return findUser;
    }

    // 직원(작업자) 생성
    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) throws NotFoundException {
        Department department = departmentService.findByIdAndDeleteYnFalse(employeeRequest.getDeptId());
        User user = modelMapper.toEntity(employeeRequest, User.class);
        user.setDepartment(department);
        User saveEmp = employeeRepository.save(user);
        return modelMapper.toResponse(saveEmp, EmployeeResponse.class);
    }

    // 직원(작업자) 단일 조회
    public EmployeeResponse getEmployee(Long id) throws NotFoundException {
        User user = findByIdAndDeleteYnFalse(id);
        return modelMapper.toResponse(user, EmployeeResponse.class);
    }

    // 직원(작업자) 페이징 조회
    public Page<EmployeeResponse> getEmployees(Pageable pageable) {
        Page<User> employees = employeeRepository.findAllByDeleteYnFalse(pageable);
        return modelMapper.toPageResponses(employees, EmployeeResponse.class);
    }

    // 직원(작업자) 수정
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest employeeRequest) throws NotFoundException {
        Department newDepartment = departmentService.findByIdAndDeleteYnFalse(employeeRequest.getDeptId());
        User newEmp = modelMapper.toEntity(employeeRequest, User.class);
        User findEmp = findByIdAndDeleteYnFalse(id);
        findEmp.put(newEmp, newDepartment);
        User saveEmp = employeeRepository.save(findEmp);
        return modelMapper.toResponse(saveEmp, EmployeeResponse.class);
    }

    // 직원(작업자) 삭제
    public void deleteEmployee(Long id) throws NotFoundException {
        User user = findByIdAndDeleteYnFalse(id);
        user.setDeleteYn(true);
    }
}
