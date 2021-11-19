package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.BadItem;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BadItemRepository extends JpaCustomRepository<BadItem, Long> {
}