package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
 * 라우팅 등록
 * 라우팅번호, 라우팅명, 사용 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "ROUTINGS")
@Data
public class Routing extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(name = "ROUTING_NO", unique = true)
    private Long routingNo;     // 라우팅 번호

    @Column(name = "ROUTING_NAME", nullable = false)
    private String routingName;     // 라우팅 명

    @Column(name = "USE_YN", nullable = false)
    private boolean useYn;
}

