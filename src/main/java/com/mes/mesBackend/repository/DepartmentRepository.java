package com.mes.mesBackend.repository;


import com.mes.mesBackend.entity.Department;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;

public interface DepartmentRepository extends JpaCustomRepository<Department, Long> {
}