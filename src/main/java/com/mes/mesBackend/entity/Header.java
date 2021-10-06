package com.mes.mesBackend.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "HEADERS")
public class Header {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID")
    private Long id;

    @Column(name = "HEADER")
    private String header;  // 헤더

    @Column(name = "CONTROLLER_NAME")
    private String controllerName;  // 컨트롤러 이름

    @Column(name = "COLUMN_NAME")
    private String columnName;      // 컬럼명

    @Column(name = "SEQ")
    private int seq;        // 순서
}
