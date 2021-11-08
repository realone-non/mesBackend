package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.WorkPlace;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// 사업장 repository
@Repository
public interface WorkPlaceRepository extends JpaCustomRepository<WorkPlace, Long> {
}