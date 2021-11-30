package com.mes.mesBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

/*
 * P/I
 * 견적정보가 가지고 있는 운송정보 ????
 * */
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "PI")
@Data
public class Pi extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT 'PI 고유아이디'")
    private Long id;

    @Column(name = "INVIOCE_NO", columnDefinition = "varchar(255) COMMENT 'InvoiceNo'")
    private String invoiceNo;       // InvoiceNo

    @Column(name = "CUSTOMER_PO_NO", columnDefinition = "varchar(255) COMMENT 'customer PO NO'")
    private String customerPoNo;    // customer PO NO

    @Column(name = "BILL_TO", columnDefinition = "varchar(255) COMMENT 'Bill To'")
    private String billTo;          // Bill To

    @Column(name = "REMARKS", columnDefinition = "varchar(255) COMMENT 'remarks'")
    private String remarks;         // Remarks

    public void update(Pi newPi) {
        setCustomerPoNo(newPi.customerPoNo);
        setBillTo(newPi.billTo);
        setRemarks(newPi.remarks);
    }
}
