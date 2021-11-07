package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.ItemAccount;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemAccountRepository extends JpaCustomRepository<ItemAccount, Long> {
}