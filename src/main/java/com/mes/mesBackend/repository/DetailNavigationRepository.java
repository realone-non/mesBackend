package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.DetailNavigation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailNavigationRepository extends JpaRepository<DetailNavigation, Long> {
    DetailNavigation findDetailNavigationsByUseYnTrue();
}