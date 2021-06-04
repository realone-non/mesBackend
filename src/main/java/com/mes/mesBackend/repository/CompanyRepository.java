package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.CompanyVo;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CompanyRepository extends CrudRepository<CompanyVo, String> {

    Optional<CompanyVo> findByCompanyId(String id);

}
