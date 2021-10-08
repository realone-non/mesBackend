package com.mes.mesBackend.repository;


import com.mes.mesBackend.entity.ClientType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientTypeRepository extends JpaRepository<ClientType, Long> {
    Page<ClientType> findAllByDeleteYnFalse(Pageable pageable);
    ClientType findByIdAndDeleteYnFalse(Long id);
}