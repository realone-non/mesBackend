package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.WorkOrderUserResponse;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.repository.WorkOrderStateRepository;
import com.mes.mesBackend.service.WorkOrderUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkOrderUserServiceImpl implements WorkOrderUserService {
    private final WorkOrderStateRepository workOrderStateRepository;

    @Override
    public List<WorkOrderUserResponse> getWorkOrderUsers(Long workLineId, String produceOrderNo, Long itemAccountId, OrderState orderState, LocalDate fromDate, LocalDate toDate, String contractNo) {
        List<WorkOrderUserResponse> workOrderUsers =
                workOrderStateRepository.findWorkOrderUserByCondition(workLineId, produceOrderNo, itemAccountId, orderState, fromDate, toDate, contractNo);

        List<WorkOrderUserResponse> ll = workOrderUsers.stream().map(WorkOrderUserResponse::ahah).collect(Collectors.toList());

        ll.remove(null);

        return ll;
    }

    @Override
    public WorkOrderUserResponse getWorkOrderUser(Long workOrderId) {
        return null;
    }

    @Override
    public WorkOrderUserResponse updateWorkOrderUser(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return null;
    }
}
