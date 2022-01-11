package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.MeasureCalibration;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.MeasureCalibrationRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasureCalibrationRepository extends JpaCustomRepository<MeasureCalibration, Long>, MeasureCalibrationRepositoryCustom {
}
