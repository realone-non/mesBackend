package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.WorkOrderUserResponse;
import com.mes.mesBackend.entity.User;
import com.mes.mesBackend.entity.WorkOrderDetail;
import com.mes.mesBackend.entity.WorkOrderState;
import com.mes.mesBackend.entity.enumeration.OrderState;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.repository.WorkOrderDetailRepository;
import com.mes.mesBackend.repository.WorkOrderStateRepository;
import com.mes.mesBackend.service.UserService;
import com.mes.mesBackend.service.WorkOrderUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkOrderUserServiceImpl implements WorkOrderUserService {
    private final WorkOrderStateRepository workOrderStateRepository;
    private final WorkOrderDetailRepository workOrderDetailRepository;
    private final UserService userService;

    @Override
    public List<WorkOrderUserResponse> getWorkOrderUsers(Long workLineId, String produceOrderNo, Long itemAccountId, OrderState orderState, LocalDate fromDate, LocalDate toDate, String contractNo) {
        List<WorkOrderUserResponse> findWorkOrderUsers =
                workOrderStateRepository.findWorkOrderUserByCondition(workLineId, produceOrderNo, itemAccountId, fromDate, toDate, contractNo);

        // dateTime 필드 가져오기
        for (WorkOrderUserResponse workOrderUser : findWorkOrderUsers) {
            LocalDateTime schedule = workOrderStateRepository.findWorkOrderUserState(workOrderUser.getId(), OrderState.SCHEDULE);
            LocalDateTime onGoing = workOrderStateRepository.findWorkOrderUserState(workOrderUser.getId(), OrderState.ONGOING);
            LocalDateTime completion = workOrderStateRepository.findWorkOrderUserState(workOrderUser.getId(), OrderState.COMPLETION);

            workOrderUser.setScheduleDateTime(schedule);
            workOrderUser.setStartDateTime(onGoing);
            workOrderUser.setEndDateTime(completion);
        }

        List<WorkOrderUserResponse> workOrderUserStates =
                findWorkOrderUsers.stream().map(WorkOrderUserResponse::dateTimeCheckingConverter).collect(Collectors.toList());

        workOrderUserStates.remove(null);

        List<WorkOrderUserResponse> works = new ArrayList<>();
        // 지시상태 별 조회
        if (orderState != null) {
            for (WorkOrderUserResponse workOrderUserState : workOrderUserStates) {
                WorkOrderUserResponse workOrderUserResponse = workOrderUserState != null ? workOrderUserState.orderStateCondition(orderState) : null;
                if(workOrderUserResponse != null)
                    works.add(workOrderUserResponse);
            }
            return works;
        }
        return workOrderUserStates;
    }

    // 작업자 투입 단일 조회
    @Override
    public WorkOrderUserResponse getWorkOrderUser(Long workOrderId) throws NotFoundException {
        WorkOrderUserResponse workOrderUser = workOrderStateRepository.findWorkOrderUserByIdAndDeleteYn(workOrderId)
                .orElseThrow(() -> new NotFoundException("workOrderUser does not exist. input workOrderDetail id: " + workOrderId));

        LocalDateTime schedule = workOrderStateRepository.findWorkOrderUserState(workOrderUser.getId(), OrderState.SCHEDULE);
        LocalDateTime onGoing = workOrderStateRepository.findWorkOrderUserState(workOrderUser.getId(), OrderState.ONGOING);
        LocalDateTime completion = workOrderStateRepository.findWorkOrderUserState(workOrderUser.getId(), OrderState.COMPLETION);

        workOrderUser.setScheduleDateTime(schedule);
        workOrderUser.setStartDateTime(onGoing);
        workOrderUser.setEndDateTime(completion);

        return workOrderUser;
    }

    // 작업자 투입 수정
    @Override
    public WorkOrderUserResponse updateWorkOrderUser(Long workOrderDetailId, Long userId, LocalDateTime startDate, LocalDateTime endDate) throws NotFoundException, BadRequestException {
        WorkOrderDetail workOrderDetail = workOrderDetailRepository.findByIdAndDeleteYnFalse(workOrderDetailId)
                .orElseThrow(() -> new NotFoundException("workOrderDetail does not exist. input id: " + workOrderDetailId));

        WorkOrderUserResponse workOrderUser = getWorkOrderUser(workOrderDetailId);

        // 종료 날짜가 시작 날짜보다 과거에 있으면 예외처리
        checkStartDateAndEndDate(startDate, endDate);

        // 찾은 작업지시정보의 작업지시상태의 startDate 가 없으면 예외(db 에 startDate 가 있어도 최근 등록된 scheduleDateTime 보다 과거면 존재하지 않는거)
        WorkOrderState findOnGoing =
                workOrderStateRepository.findTopByOrderStateAndWorkOrderDateTimeAndWorkOrderDetailOrderByModifiedDateDesc(OrderState.ONGOING, workOrderUser.getStartDateTime(), workOrderDetail)
                .orElseThrow(() -> new BadRequestException("workOrderDetail does not have startDateTime. please input startDateTime in workOrderDetail."));
        // 찾은 작업지시정보의 작업지시상태 endDate 가 없으면(db에 endDate 가 있어도 최근 등록된 startDate 보다 endDate 가 과거면 존재하지 않는거) 작업지시상태 정보에 입력받은 데이터 생성
        WorkOrderState findEndDateTime =
                workOrderStateRepository.findTopByOrderStateAndWorkOrderDateTimeAndWorkOrderDetailOrderByModifiedDateDesc(OrderState.COMPLETION, workOrderUser.getEndDateTime(), workOrderDetail)
                .orElse(workOrderStateRepository.save(new WorkOrderState(workOrderDetail, endDate, OrderState.COMPLETION)));

        User newUser = userService.getUserOrThrow(userId);
        workOrderDetail.setUser(newUser);

        if (startDate != null) {
            findOnGoing.setWorkOrderDateTime(startDate);
            workOrderStateRepository.save(findOnGoing);
        }
        if (endDate != null) {
            findEndDateTime.setWorkOrderDateTime(endDate);
            workOrderStateRepository.save(findEndDateTime);
        }
        workOrderDetailRepository.save(workOrderDetail);
        return getWorkOrderUser(workOrderDetailId);
    }

    // 시작날짜, 종료날자 둘다 입력 받았을 시 endDate 가 startDate 보다 과거면 badRequestException.
    private void checkStartDateAndEndDate(LocalDateTime startDate, LocalDateTime endDateTime) throws BadRequestException {
        if (startDate != null && endDateTime != null) {
            if (endDateTime.isBefore(startDate)) {
                throw new BadRequestException("endDate cannot be in the past than startDate. input startDateTime: " + startDate +", endDateTime: " + endDateTime);
            }
        }
    }
}
