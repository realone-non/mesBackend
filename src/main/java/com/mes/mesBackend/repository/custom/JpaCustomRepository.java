package com.mes.mesBackend.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface JpaCustomRepository<T, ID> extends JpaRepository<T,ID> {

    Optional<T> findByIdAndDeleteYnFalse(ID id);

    Page<T> findAllByDeleteYnFalse(Pageable pageable);

    List<T> findAllByDeleteYnFalse();
}
