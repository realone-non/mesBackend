package com.mes.mesBackend.repository;


import com.mes.mesBackend.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findByIdAndDeleteYnFalse(Long id);
    Page<Department> findAllByDeleteYnFalse(Pageable pageable);

}