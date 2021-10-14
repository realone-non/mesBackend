package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.DetailNavigation;
import com.mes.mesBackend.entity.SubNavigation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailNavigationRepository extends JpaRepository<DetailNavigation, Long> {
    List<DetailNavigation> findAllBySubNavigationAndUseYnTrueOrderByOrdersAsc(SubNavigation subNavigation);
    DetailNavigation findByIdAndUseYnTrue(Long id);
}