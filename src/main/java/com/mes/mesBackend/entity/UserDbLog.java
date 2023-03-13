package com.mes.mesBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;

@Entity(name = "USER_DB_LOGS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDbLog {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '유저 로그 고유 아이디'")
    private Long id;
    @Column(name = "LOG_DT", columnDefinition = "varchar(255) COMMENT '로그 일시'")
    private String logDt;
    @Column(name = "USE_SE", columnDefinition = "varchar(255) COMMENT '사용 구분'")
    private String useSe;
    @Column(name = "SYS_USER", columnDefinition = "varchar(255) COMMENT '시스템 사용자'")
    private String sysUser;
    @Column(name = "CONECT_IP", columnDefinition = "varchar(255) COMMENT '접속 IP'")
    private String conectIp;
    @Column(name = "DATA_USGQTY", columnDefinition = "varchar(255) COMMENT '데이터 사용량'")
    private String dataUsgqty;
}
