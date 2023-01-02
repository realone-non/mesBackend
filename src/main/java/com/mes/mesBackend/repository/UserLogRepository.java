package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.UserLog;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import main.java.com.mes.mesBackend.entity.UserLog;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLogRepository extends JpaCustomRepository<UserLog, Long> {
    public UserLog findTop1ByUseMethodOrderByCreatedDateDesc(String useMethod);
}
