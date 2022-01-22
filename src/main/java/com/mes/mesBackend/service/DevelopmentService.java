package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.DevelopmentRequest;
import com.mes.mesBackend.dto.request.DevelopmentStateRequest;
import com.mes.mesBackend.dto.response.DevelopmentResponse;
import com.mes.mesBackend.dto.response.DevelopmentStateReponse;
import com.mes.mesBackend.entity.enumeration.DevelopmentChildrenStatusType;
import com.mes.mesBackend.entity.enumeration.DevelopmentStatusType;

import java.time.LocalDate;
import java.util.List;

public interface DevelopmentService {
    //개발품목 등록
    DevelopmentResponse createDevelopment(DevelopmentRequest request);

    //개발품목 리스트 조회
    List<DevelopmentResponse> getDevelopments(
            LocalDate fromDate,
            LocalDate toDate,
            Long userId,
            Long itemId,
            DevelopmentStatusType status,
            DevelopmentChildrenStatusType childrenStatus);

    //개발품목 단건 조회
    DevelopmentResponse getDevelopment(Long id);

    //개발품목 수정
    DevelopmentResponse modifyDevelopment(Long id, DevelopmentResponse request);

    //개발품목 삭제
    void deleteDevelopment(Long id);

    //개발품목 진행상태 등록
    DevelopmentStateReponse createDevelopmentState(DevelopmentStateRequest request);

    //개발품목 진행상태 리스트 조회
    List<DevelopmentStateReponse> getDevelopmentStateList(Long developmentId);

    //개발품목 진행상태 단건 조회
    DevelopmentResponse getDevelopmentState(Long developmentId, Long developmentStateId);

    //개발품목 진행상태 수정
    DevelopmentResponse modifyDevelopmentState(Long developmentId, Long developmentStateId, DevelopmentStateRequest request);

    //개발품목 진행상태 삭제
    void deleteDevelopmentState(Long developomentId, Long developmentStateId);

}
