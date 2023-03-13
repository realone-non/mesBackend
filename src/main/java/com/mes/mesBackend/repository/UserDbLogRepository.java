package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.UserDbLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDbLogRepository extends JpaRepository<UserDbLog, Long> {
}
