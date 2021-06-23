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
@Data
public class UserVo {
    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column (nullable = false, name = "FACTORY_CODE")
    private String factoryCode;
    @Column (nullable = false, name = "USER_NAME")
    private String userName;

    @Column(nullable = false, name = "PASSWORD")
    private String password;
    @Column(name = "TEL")
    private String telephone;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "ISUSE")
    private boolean useYn;

    @Column(nullable = false, updatable = false, name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdDate;
    @Column(name = "CREATE_ID")
    private String createId;
    @Column(nullable = false, updatable = true, name = "MODIFY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date modifiedDate;
    @Column(name = "MODIFY_ID")
    private String modifyId;
}
