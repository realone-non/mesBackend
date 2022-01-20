package com.mes.mesBackend.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "HEADERS")
public class Header {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '헤더 고유아이디'")
    private Long id;

    @Column(name = "HEADER", columnDefinition = "varchar(255) COMMENT '헤더 명'")
    private String header;  // 헤더

    @Column(name = "CONTROLLER_NAME", columnDefinition = "varchar(255) COMMENT '컨드롤러 명'")
    private String controllerName;  // 컨트롤러 이름

    @Column(name = "COLUMN_NAME", columnDefinition = "varchar(255) COMMENT '컬럼 명'")
    private String columnName;      // 컬럼명

    @Column(name = "SEQ", columnDefinition = "int COMMENT '출력순서'")
    private int seq;        // 순서

    @Column(name = "IS_REQUIRE", columnDefinition = "bit(1) COMMENT '필수여부'")
    private boolean isRequire;

    public void put(Header newHeader) {
        setHeader(newHeader.header);
        setControllerName(newHeader.controllerName);
        setColumnName(newHeader.columnName);
        setSeq(newHeader.seq);
    }
}
