package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Holiday;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayRepository extends JpaCustomRepository<Holiday, Long> {
}
