package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Routing;
import com.mes.mesBackend.entity.RoutingDetail;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoutingDetailsRepository extends JpaCustomRepository<RoutingDetail, Long> {
    List<RoutingDetail> findAllByRoutingAndDeleteYnFalse(Routing routing);
    RoutingDetail findByIdAndRoutingAndDeleteYnFalse(Long id, Routing routing);
}
