package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.ProductDevelopmentState;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDevelopmentStateRepository extends JpaCustomRepository<ProductDevelopmentState, Long> {
}
