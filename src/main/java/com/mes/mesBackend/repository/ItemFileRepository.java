package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Item;
import com.mes.mesBackend.entity.ItemFile;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemFileRepository extends JpaCustomRepository<ItemFile, Long> {
    List<ItemFile> findAllByItemAndDeleteYnFalse(Item item);
}