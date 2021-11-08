package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.ItemForm;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemFormRepository extends JpaCustomRepository<ItemForm, Long> {
}