package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.ReceiptAndPaymentResponse;
import jdk.vm.ci.meta.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface MaterialWarehouseService {
    //수불부 조회
    List<ReceiptAndPaymentResponse> getReceiptAndPaymentList(Long warehouseId, Long itemAccountId, LocalDate fromDate, LocalDate toDate);
}
