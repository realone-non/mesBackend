package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.ItemGroup;
import com.mes.mesBackend.entity.ItemGroupCode;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// 품목그룹
@Repository
public interface ItemGroupRepository extends JpaCustomRepository<ItemGroup, Long> {
    boolean existsByItemGroupCodeAndDeleteYnFalse(ItemGroupCode itemGroupCode);
}