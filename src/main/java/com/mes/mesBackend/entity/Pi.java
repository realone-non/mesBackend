package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
 * P/I
 * 견적정보가 가지고 있는 운송정보 ????
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "PI")
@Data
public class Pi extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", columnDefinition = "bigint COMMENT 'PI 고유아이디'")
    private Long id;

    @Column(name = "INVIOCE_NO", columnDefinition = "bigint COMMENT 'InvoiceNo'")
    private String invoiceNo;       // InvoiceNo

    @Column(name = "CUSTOMER_PO_NO", columnDefinition = "bigint COMMENT 'customer PO NO'")
    private String customerPoNo;    // customer PO NO

    @Column(name = "BILL_TO", columnDefinition = "bigint COMMENT 'Bill To'")
    private String billTo;          // Bill To

    @Column(name = "REMARKS", columnDefinition = "bigint COMMENT 'remarks'")
    private String remarks;         // Remarks
}
