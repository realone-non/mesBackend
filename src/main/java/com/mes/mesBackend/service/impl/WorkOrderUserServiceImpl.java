package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.WorkOrderUserResponse;
import com.mes.mesBackend.entity.Item;
import com.mes.mesBackend.entity.User;
import com.mes.mesBackend.entity.WorkOrderDetail;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.repository.ItemRepository;
import com.mes.mesBackend.repository.WorkOrderDetailRepository;
import com.mes.mesBackend.service.UserService;
import com.mes.mesBackend.service.WorkOrderUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.mes.mesBackend.entity.enumeration.OrderState.COMPLETION;
import static com.mes.mesBackend.entity.enumeration.OrderState.ONGOING;
import static com.mes.mesBackend.entity.enumeration.WorkProcessDivision.PACKAGING;

// 8-2. 작업자 투입 수정
@Service
@RequiredArgsConstructor
public class WorkOrderUserServiceImpl implements WorkOrderUserService {
    private final WorkOrderDetailRepository workOrderDetailRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    @Override
    public List<WorkOrderUserResponse> getWorkOrderUsers(
            Long workProcessId,
            String produceOrderNo,
            Long itemAccountId,
            OrderState orderState,
            LocalDate fromDate,
            LocalDate toDate,
            String contractNo
    ) {
        List<WorkOrderUserResponse> findWorkOrderUsers =
                workOrderDetailRepository.findWorkOrderUserResponsesByCondition(workProcessId, produceOrderNo, itemAccountId, orderState, fromDate, toDate, contractNo);

        // 해당 공정에 맞는 bomDetailItem
        for (WorkOrderUserResponse response : findWorkOrderUsers) {
            Item item = response.getWorkProcessDivision().equals(PACKAGING) ? getItemOrNull(response.getItemId())
                    : workOrderDetailRepository.findBomDetailHalfProductByBomMasterItemIdAndWorkProcessId(response.getItemId(), response.getWorkProcessId(), null)
                    .orElse(null);
            if (item != null) response.setItems(item);
        }

        findWorkOrderUsers.forEach(WorkOrderUserResponse::putCostTime);

        if (itemAccountId != null) {
            return findWorkOrderUsers.stream().filter(
                    f -> f.getItemAccountId() != null && f.getItemAccountId().equals(itemAccountId)
            ).collect(Collectors.toList());
        } else {
            return findWorkOrderUsers;
        }
    }

    // 품목 조회 및 없으면 null
    private Item getItemOrNull(Long id) {
        return itemRepository.findByIdAndDeleteYnFalse(id).orElse(null);
    }

    // 작업자 투입 단일 조회
    @Override
    public WorkOrderUserResponse getWorkOrderUserResponseOrThrow(Long workOrderId) throws NotFoundException {
        WorkOrderUserResponse workOrderUser = workOrderDetailRepository.findWorkOrderUserResponseByIdAndDeleteYn(workOrderId)
                .orElseThrow(() -> new NotFoundException("workOrderUser does not exist. input workOrderDetail id: " + workOrderId));
        workOrderUser.putCostTime();
        return workOrderUser;
    }

    // 작업자 투입 수정
    @Override
    public WorkOrderUserResponse updateWorkOrderUser(
            Long workOrderDetailId,
            Long newUserId,
            LocalDateTime newStartDate,
            LocalDateTime newEndDate
    ) throws NotFoundException, BadRequestException {
        WorkOrderDetail workOrderDetail = workOrderDetailRepository.findByIdAndDeleteYnFalse(workOrderDetailId)
                .orElseThrow(() -> new NotFoundException("workOrderDetail does not exist. input id: " + workOrderDetailId));

        // 시작날짜보다 종료날짜가 뒤면 예외
        checkStartDateAndEndDate(newStartDate, newEndDate);

        if (!workOrderDetail.getOrderState().equals(COMPLETION)) {  // 작업지시 상태가 완료가 아닌데
            if (workOrderDetail.getStartDate() != newStartDate && workOrderDetail.getEndDate() != newEndDate) {     // 기존 작업지시의 시작날짜 종료일자와 입력받은 데이터가 다를경우 예외
                throw new BadRequestException("작업지시의 지시상태가 완료일 경우에만 시작일시, 종료일시를 변경 또는 입력할수 있습니다.");
            }
        } else {
            // 작업지시의 상태가 완료인데 입력받은 newStartDate, newEndDate 가 null 이면 예외
            if (newStartDate == null || newEndDate == null) {
                throw new BadRequestException("작업지시의 지시상태가 완료일 경우에는 시작일시, 종료일시를 값을 삭제할수 없습니다.");
            }
        }

        User newUser = userService.getUserOrThrow(newUserId);
        workOrderDetail.setUser(newUser);
        workOrderDetail.setStartDate(newStartDate);
        workOrderDetail.setEndDate(newEndDate);
        workOrderDetailRepository.save(workOrderDetail);

        return getWorkOrderUserResponseOrThrow(workOrderDetailId);
    }

    //     시작날짜, 종료날자 둘다 입력 받았을 시 endDate 가 startDate 보다 과거면 badRequestException.
    private void checkStartDateAndEndDate(LocalDateTime startDate, LocalDateTime endDateTime) throws BadRequestException {
        if (startDate != null && endDateTime != null) {
            if (endDateTime.isBefore(startDate)) {
                throw new BadRequestException("endDate cannot be in the past than startDate. input startDateTime: " + startDate +", endDateTime: " + endDateTime);
            }
        }
    }
}
