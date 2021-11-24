package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Estimate;
import com.mes.mesBackend.entity.EstimateItemDetail;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstimateItemDetailRepository extends JpaCustomRepository<EstimateItemDetail, Long> {
    Optional<EstimateItemDetail> findByIdAndEstimateAndDeleteYnFalse(Long id, Estimate estimate);
    List<EstimateItemDetail> findAllByEstimateAndDeleteYnFalse(Estimate estimate);
}