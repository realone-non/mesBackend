package com.mes.mesBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.time.LocalDate;

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
    @Column(name = "CREATED_DATE", columnDefinition = "date COMMENT '생성 날짜'")
    private LocalDate createdDate;
    @Column(name = "RECPTN_DT", columnDefinition = "varchar(255) COMMENT '수신일시'")
    private String recptnDt;
    @Column(name = "RECPTN_RSLT_CD", columnDefinition = "varchar(255) COMMENT '수신결과코드'")
    private String recptnRsltCd;
    @Column(name = "RECPTN_RSLT", columnDefinition = "varchar(255) COMMENT '수신결과코드설명'")
    private String recptnRslt;
    @Column(name = "RECPTN_RSLT_DTL", columnDefinition = "varchar(255) COMMENT '수신결과상세설명'")
    private String recptnRsltDtl;
}
