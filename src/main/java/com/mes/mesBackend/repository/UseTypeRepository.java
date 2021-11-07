package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.UseType;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UseTypeRepository extends JpaCustomRepository<UseType, Long> {
}