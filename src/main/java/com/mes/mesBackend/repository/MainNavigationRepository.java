package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.MainNavigation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MainNavigationRepository extends JpaRepository<MainNavigation, Long> {
    List<MainNavigation> findMainNavigationsByUseYnTrue();
    MainNavigation findByIdAndUseYnTrue(Long id);
}