package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Routing;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutingRepository extends JpaCustomRepository<Routing, Long> {
}