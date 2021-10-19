package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/*
 * 라우팅 등록
 * 검색: 공장(드롭박스)
 * 라우팅번호 (01,02,03)
 * 라우팅명 (필터생산1,필터생산2,계측기)
 * 사용
 * */
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

    @Column(name = "DELETE_YN")
    private boolean deleteYn = false;  // 삭제여부

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "FACTORIES_ID")
    private Factory factory;                // 공장
}

