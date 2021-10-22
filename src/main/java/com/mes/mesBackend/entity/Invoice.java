package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
 * Invoice
 * Invoice No
 * Bill To
 * DESTINATION
 * Customer PO No
 * Remarks
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "INVOICES")
@Data
public class Invoice extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT 'Invoice 고유아이디'")
    private Long id;

    @Column(name = "INVOICE_NO", columnDefinition = "varchar(255) COMMENT 'Invoice'")
    private String invoiceNo;   // Invoice

    @Column(name = "BILL_TO", columnDefinition = "varchar(255) COMMENT 'Bill To'")
    private String billTo;      // Bill To

    @Column(name = "DESTINATION", columnDefinition = "varchar(255) COMMENT 'DESTINATION'")
    private String destination; // DESTINATION

    @Column(name = "CUSTOMER_PO_NO", columnDefinition = "varchar(255) COMMENT 'Customer PO No'")
    private String customerPoNo;    // Customer PO No

    @Column(name = "REMARKS", columnDefinition = "varchar(255) COMMENT 'remarks'")
    private String remarks;     // remarks
}
