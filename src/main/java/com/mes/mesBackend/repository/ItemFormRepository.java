package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.ItemForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemFormRepository extends JpaRepository<ItemForm, Long> {
    List<ItemForm> findAllByDeleteYnFalse();
    ItemForm findByIdAndDeleteYnFalse(Long id);
}