package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '라우팅 등록 고유아이디'")
    private Long id;

    @Column(name = "ROUTING_NO",  columnDefinition = "varchar(255) COMMENT '라우팅 번호'")
    private String routingNo;     // 라우팅 번호

    @Column(name = "ROUTING_NAME", nullable = false, columnDefinition = "varchar(255) COMMENT '라우팅 명'")
    private String routingName;     // 라우팅 명

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn;

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    public void update(Routing newRouting) {
        setRoutingNo(newRouting.routingNo);
        setRoutingName(newRouting.routingName);
        setUseYn(newRouting.useYn);
    }

    public void delete() {
        setDeleteYn(true);
    }
}

