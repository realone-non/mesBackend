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
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(name = "INVOICE_NO")
    private String invoiceNo;   // Invoice

    @Column(name = "BILL_TO")
    private String billTo;      // Bill To

    @Column(name = "DESTINATION")
    private String destination; // DESTINATION

    @Column(name = "CUSTOMER_PO_NO")
    private String customerPoNo;    // Customer PO No

    @Column(name = "REMARKS")
    private String remarks;     // remarks
}
