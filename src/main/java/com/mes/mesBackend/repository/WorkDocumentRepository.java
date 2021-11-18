package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.WorkDocument;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.WorkDocumentRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkDocumentRepository extends JpaCustomRepository<WorkDocument, Long>, WorkDocumentRepositoryCustom {
}