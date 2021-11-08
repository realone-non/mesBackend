package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Client;
import com.mes.mesBackend.repository.custom.ClientRepositoryCustom;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaCustomRepository<Client, Long>, ClientRepositoryCustom {
}
