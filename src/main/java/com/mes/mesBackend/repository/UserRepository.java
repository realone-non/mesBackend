package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.UserVo;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.HTMLDocument;
import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserVo, String> {

    Optional<UserVo> findByUserIdAndPassword(String id, String password);

    Optional<UserVo> findByUserId(String id);
}
