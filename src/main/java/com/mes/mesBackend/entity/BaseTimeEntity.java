package com.mes.mesBackend.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @CreatedDate @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @LastModifiedDate @Column(name = "MODIFIED_DATE")
    private LocalDateTime modifiedDate;
}