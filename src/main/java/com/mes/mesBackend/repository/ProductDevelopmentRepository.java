package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.ProductDevelopment;
import com.mes.mesBackend.repository.custom.ProductDevelopmentRepositoryCustom;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDevelopmentRepository extends JpaCustomRepository<ProductDevelopment, Long>, ProductDevelopmentRepositoryCustom {
}
