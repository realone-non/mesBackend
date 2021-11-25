package com.mes.mesBackend.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "REFRESH_TOKEN")
@Data
public class RefreshToken extends BaseTimeEntity {
    /*
    * key 에는 user id
    * value 에는 Refresh Token String 이 들어감
    *
    * */

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '고유아이디'")
    private Long id;
    @Column(name = "USER_CODE", columnDefinition = "varchar(50) COMMENT 'USER_CODE'")
    private String userCode;
    @Column(name = "TOKEN", columnDefinition = "varchar(700) COMMENT 'REFRESH_TOKEN'")
    private String token;

    public RefreshToken updateValue(String token) {
        this.token = token;
        return this;
    }

//    @Builder
//    public RefreshToken(String key, String value) {
//        this.key = key;
//        this.token = value;
//    }
}
