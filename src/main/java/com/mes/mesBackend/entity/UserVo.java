package com.mes.mesBackend.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="MES_USER")
@Getter
@Setter
public class UserVo {
    @Id
    private String userId;

    @Column (nullable = false)
    private String factoryCode;
    @Column (nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;
    private String telephone;
    private String email;
    private String description;
    private boolean isUse;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdDate;
    private String createId;
    @Column(nullable = false, updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date modifiedDate;
    private String modifyId;
}
