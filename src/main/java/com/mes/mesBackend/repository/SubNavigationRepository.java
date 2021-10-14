package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.MainNavigation;
import com.mes.mesBackend.entity.SubNavigation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubNavigationRepository extends JpaRepository<SubNavigation, Long> {
    SubNavigation findByIdAndUseYnTrue(Long id);
    List<SubNavigation> findAllByMainNavigationAndUseYnTrueOrderByOrdersAsc(MainNavigation mainNavigation);
}