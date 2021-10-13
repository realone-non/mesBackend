package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.DepartmentRequest;
import com.mes.mesBackend.dto.response.DepartmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentService {

    // 부서 생성
    DepartmentResponse createDepartment(DepartmentRequest departmentRequest);

    // 부서 조회
    DepartmentResponse getDepartment(Long id);

    // 부서 전체 조회
    Page<DepartmentResponse> getDepartments(Pageable pageable);

    // 부서 수정
    DepartmentResponse updateDepartment(Long id, DepartmentRequest departmentRequest);

    // 부서 삭제
    void deleteDepartment(Long id);
}
