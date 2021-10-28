package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.GridOption;
import com.mes.mesBackend.entity.Header;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GridOptionRepository extends JpaRepository<GridOption, Long> {
    List<GridOption> findAllByUserIdAndHeader(String id, Header header);
    GridOption findByHeaderAndUserId(Header header, String userId);
}