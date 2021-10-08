package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.WorkPlaceMapped;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkPlaceMappedRepository extends JpaRepository<WorkPlaceMapped, Long> {
}