package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.ItemGroup;
import com.mes.mesBackend.entity.ItemGroupCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// 품목그룹
@Repository
public interface ItemGroupRepository extends JpaRepository<ItemGroup, Long> {
    ItemGroup findByIdAndDeleteYnFalse(Long id);
    Page<ItemGroup> findAllByDeleteYnFalse(Pageable pageable);
    boolean existsByItemGroupCodeAndDeleteYnFalse(ItemGroupCode itemGroupCode);
}