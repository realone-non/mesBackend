package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.WorkPlace;
import com.mes.mesBackend.entity.WorkPlaceBusinessType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface WorkPlaceMappedRepository extends JpaRepository<WorkPlaceBusinessType, Long> {
    @Transactional
    void deleteAllByWorkPlace(WorkPlace workPlace);
}