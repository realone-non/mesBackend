package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Currency;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaCustomRepository<Currency, Long> {
}