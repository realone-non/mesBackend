package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, ClientRepositoryCustom {
    Client findByIdAndDeleteYnFalse(Long id);
}
