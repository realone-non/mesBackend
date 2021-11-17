package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Item;
import com.mes.mesBackend.entity.SubItem;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.SubItemRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubItemRepository extends JpaCustomRepository<SubItem, Long> , SubItemRepositoryCustom {
    boolean existsByItemAndSubItemAndDeleteYnFalseAndSubOrders(Item item, Item subItem, int subOrders);
}