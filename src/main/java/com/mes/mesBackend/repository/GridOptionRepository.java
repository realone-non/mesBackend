package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.GridOption;
import com.mes.mesBackend.entity.Header;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GridOptionRepository extends JpaRepository<GridOption, Long> {
    GridOption findByHeaderAndUserId(Header header, Long userId);
    boolean existsByHeaderAndUserId(Header header, Long userId);
}