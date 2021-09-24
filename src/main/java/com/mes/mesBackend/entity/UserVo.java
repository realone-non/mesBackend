package com.mes.mesBackend.entity;

import lombok.*;
import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="MES_USERS")
@Data
public class UserVo extends BaseTimeEntity{
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

    @Column(name = "CREATE_ID")
    private String createId;

    @Column(name = "MODIFY_ID")
    private String modifyId;
}