package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Page<Client> findAllByUseYnTrue(Pageable pageable);
    Client findByIdAndUseYnTrue(Long id);
}