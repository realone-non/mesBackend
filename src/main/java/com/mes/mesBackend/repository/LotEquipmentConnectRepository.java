package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.LotEquipmentConnect;
import com.mes.mesBackend.repository.custom.LotEquipmentConnectRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotEquipmentConnectRepository extends JpaRepository<LotEquipmentConnect, Long>, LotEquipmentConnectRepositoryCustom {
}
