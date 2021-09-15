package com.mes.mesBackend.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="MES_USER")
@Data
public class UserVo {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(nullable = false, name = "NICK_NAME")
    private String nickName;

    @Column (nullable = false, name = "FACTORY_CODE")
    private String factoryCode;

    @Column (nullable = false, name = "USER_NAME")
    private String userName;

    @Column(nullable = false, name = "PASSWORD")
    private String password;

    @Column(name = "TELEPHONE")
    private String telephone;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "USE_YN")
    private boolean useYn;

    @Column(nullable = false, updatable = false, name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdDate;

    @Column(name = "CREATE_ID")
    private String createId;

    @Column(nullable = false, name = "MODIFIED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date modifiedDate;

    @Column(name = "MODIFY_ID")
    private String modifyId;
}
