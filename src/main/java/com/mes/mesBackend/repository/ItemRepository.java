package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Item;
import com.mes.mesBackend.repository.custom.ItemRepositoryCustom;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaCustomRepository<Item, Long>, ItemRepositoryCustom {
    Item findByItemNameAndDeleteYnFalse(String itemName);

}