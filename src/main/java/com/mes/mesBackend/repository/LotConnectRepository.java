package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.LotConnect;
import com.mes.mesBackend.repository.custom.LotConnectRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotConnectRepository extends JpaRepository<LotConnect, Long>, LotConnectRepositoryCustom {
}
