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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", columnDefinition = "bigint COMMENT 'Invoice 고유아이디'")
    private Long id;

    @Column(name = "INVOICE_NO", columnDefinition = "bigint COMMENT 'Invoice'")
    private String invoiceNo;   // Invoice

    @Column(name = "BILL_TO", columnDefinition = "bigint COMMENT 'Bill To'")
    private String billTo;      // Bill To

    @Column(name = "DESTINATION", columnDefinition = "bigint COMMENT 'DESTINATION'")
    private String destination; // DESTINATION

    @Column(name = "CUSTOMER_PO_NO", columnDefinition = "bigint COMMENT 'Customer PO No'")
    private String customerPoNo;    // Customer PO No

    @Column(name = "REMARKS", columnDefinition = "bigint COMMENT 'remarks'")
    private String remarks;     // remarks
}
