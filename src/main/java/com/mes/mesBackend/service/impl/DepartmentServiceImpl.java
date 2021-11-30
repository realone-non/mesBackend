package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.DepartmentRequest;
import com.mes.mesBackend.dto.response.DepartmentResponse;
import com.mes.mesBackend.entity.Department;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.DepartmentRepository;
import com.mes.mesBackend.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    ModelMapper modelMapper;

    public Department getDepartmentOrThrow(Long id) throws NotFoundException {
        return departmentRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("department does not exists. input id : " + id));
    }

    // 부서 생성
    public DepartmentResponse createDepartment(DepartmentRequest departmentRequest) {
        Department department = modelMapper.toEntity(departmentRequest, Department.class);
        departmentRepository.save(department);
        return modelMapper.toResponse(department, DepartmentResponse.class);
    }

    // 부서 조회
    public DepartmentResponse getDepartment(Long id) throws NotFoundException {
        Department department = getDepartmentOrThrow(id);
        return modelMapper.toResponse(department, DepartmentResponse.class);
    }

    // 부서 전체 조회
    public List<DepartmentResponse> getDepartments() {
        List<Department> departments = departmentRepository.findAllByDeleteYnFalse();
        return modelMapper.toListResponses(departments, DepartmentResponse.class);
    }
//    public Page<DepartmentResponse> getDepartments(Pageable pageable) {
//        Page<Department> departments = departmentRepository.findAllByDeleteYnFalse(pageable);
//        return modelMapper.toPageResponses(departments, DepartmentResponse.class);
//    }

    // 부서 수정
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest departmentRequest) throws NotFoundException {
        Department newDepartment = modelMapper.toEntity(departmentRequest, Department.class);
        Department findDepartment = getDepartmentOrThrow(id);
        findDepartment.put(newDepartment);
        departmentRepository.save(findDepartment);
        return modelMapper.toResponse(findDepartment, DepartmentResponse.class);
    }

    // 부서 삭제
    public void deleteDepartment(Long id) throws NotFoundException {
        Department department = getDepartmentOrThrow(id);
        department.delete();
        departmentRepository.save(department);
    }
}
