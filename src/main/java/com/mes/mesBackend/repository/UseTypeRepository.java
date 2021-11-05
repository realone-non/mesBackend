package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.UseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UseTypeRepository extends JpaRepository<UseType, Long> {
    List<UseType> findAllByDeleteYnFalse();
    UseType findByIdAndDeleteYnFalse(Long id);
}