package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.ItemCheck;
import com.mes.mesBackend.entity.ItemCheckDetail;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemCheckDetailsRepository extends JpaCustomRepository<ItemCheckDetail, Long> {
    Optional<ItemCheckDetail> findByIdAndItemCheckCategoryAndDeleteYnFalse(Long id, ItemCheck itemCheck);
    List<ItemCheckDetail> findAllByItemCheckCategoryAndDeleteYnFalseOrderByCreatedDateDesc(ItemCheck itemCheck);
}