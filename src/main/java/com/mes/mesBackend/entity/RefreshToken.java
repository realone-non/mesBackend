package com.mes.mesBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "REFRESH_TOKENS")
@Data
public class RefreshToken extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '토큰 고유아이디'")
    private Long id;

    @Column(name = "USER_CODE", columnDefinition = "varchar(255) COMMENT '직원 코드'")
    private String userCode;

    @Column(name = "REFRESH_TOKEN", columnDefinition = "varchar(500) COMMENT 'Refresh Token'")
    private String refreshToken;

    @Column(name = "USE_YN", columnDefinition = "bit(1) COMMENT '사용여부'")
    private Boolean useYn;

    public void save(String userCode, String refreshToken) {
        setUserCode(userCode);
        setRefreshToken(refreshToken);
        setUseYn(true);
    }

    public void useYnFalse() {
        setUseYn(false);
    }
}
