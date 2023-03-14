package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.UserDbLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserDbLogRepository extends JpaRepository<UserDbLog, Long> {
    List<UserDbLog> findUserDbLogsByRecptnRsltIsNullAndCreatedDateOrderByLogDtAsc(LocalDate createdDate);
}
