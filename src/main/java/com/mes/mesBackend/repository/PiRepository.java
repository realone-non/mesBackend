package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Pi;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PiRepository extends JpaRepository<Pi, Long> {
    List<Pi> findAllByInvoiceNo(String invoiceNo);
}