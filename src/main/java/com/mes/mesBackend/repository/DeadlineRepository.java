package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Deadline;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeadlineRepository extends JpaCustomRepository<Deadline, Long> {
}
