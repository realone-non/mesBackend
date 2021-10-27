package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.DepartmentRequest;
import com.mes.mesBackend.dto.response.DepartmentResponse;
import com.mes.mesBackend.entity.Department;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.Mapper;
import com.mes.mesBackend.repository.DepartmentRepository;
import com.mes.mesBackend.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired DepartmentRepository departmentRepository;
    @Autowired Mapper mapper;

    public Department findByIdAndDeleteYnFalse(Long id) throws NotFoundException {
        Department findDepartment = departmentRepository.findByIdAndDeleteYnFalse(id);
        if (findDepartment == null) throw new NotFoundException("department does not exists. input id : " + id);
        return findDepartment;
    }

    //부서 생성
    public DepartmentResponse createDepartment(DepartmentRequest departmentRequest) {
        Department department = mapper.toEntity(departmentRequest, Department.class);
        Department saveDepartment = departmentRepository.save(department);
        return mapper.toResponse(saveDepartment, DepartmentResponse.class);
    }

    // 부서 조회
    public DepartmentResponse getDepartment(Long id) throws NotFoundException {
        Department department = findByIdAndDeleteYnFalse(id);
        return mapper.toResponse(department, DepartmentResponse.class);
    }

    // 부서 전체 조회
    public Page<DepartmentResponse> getDepartments(Pageable pageable) {
        Page<Department> departments = departmentRepository.findAllByDeleteYnFalse(pageable);
        return mapper.toPageResponses(departments, DepartmentResponse.class);
    }

    // 부서 수정
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest departmentRequest) throws NotFoundException {
        Department newDepartment = mapper.toEntity(departmentRequest, Department.class);
        Department findDepartment = findByIdAndDeleteYnFalse(id);
        findDepartment.put(newDepartment);
        Department updateDepartment = departmentRepository.save(findDepartment);
        return mapper.toResponse(updateDepartment, DepartmentResponse.class);
    }

    // 부서 삭제
    public void deleteDepartment(Long id) throws NotFoundException {
        Department department = findByIdAndDeleteYnFalse(id);
        department.setDeleteYn(true);
        departmentRepository.save(department);
    }
}
