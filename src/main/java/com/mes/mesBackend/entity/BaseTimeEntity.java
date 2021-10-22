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

    @CreatedDate @Column(name = "CREATED_DATE",
            columnDefinition = "bigint COMMENT '생성일자'")
    private LocalDateTime createdDate;

    @LastModifiedDate @Column(name = "MODIFIED_DATE",
            columnDefinition = "bigint COMMENT '수정일자'")
    private LocalDateTime modifiedDate;

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bigint COMMENT '사용여부'")
    private Boolean useYn = true;      //  사용여부

    @Column(name = "DELETE_YN", columnDefinition = "bigint COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부
}