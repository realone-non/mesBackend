package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Item;
import com.mes.mesBackend.repository.custom.ItemRepositoryCustom;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaCustomRepository<Item, Long>, ItemRepositoryCustom {
}