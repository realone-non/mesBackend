package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.ItemAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemAccountRepository extends JpaRepository<ItemAccount, Long> {
    List<ItemAccount> findAllByDeleteYnFalse();
    ItemAccount findByIdAndDeleteYnFalse(Long id);
}