package com.mes.mesBackend.repository;


import com.mes.mesBackend.entity.ClientType;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientTypeRepository extends JpaCustomRepository<ClientType, Long> {
}