package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.DevelopmentRequest;
import com.mes.mesBackend.dto.request.DevelopmentStateRequest;
import com.mes.mesBackend.dto.response.DevelopmentResponse;
import com.mes.mesBackend.dto.response.DevelopmentStateReponse;
import com.mes.mesBackend.entity.enumeration.DevelopmentChildrenStatusType;
import com.mes.mesBackend.entity.enumeration.DevelopmentStatusType;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface DevelopmentService {
    //개발품목 등록
    DevelopmentResponse createDevelopment(DevelopmentRequest request) throws NotFoundException, BadRequestException;

    //개발품목 리스트 조회
    List<DevelopmentResponse> getDevelopments(
            LocalDate fromDate,
            LocalDate toDate,
            Long userId,
            String itemNoOrItemName,
            DevelopmentStatusType status,
            DevelopmentChildrenStatusType childrenStatus);

    //개발품목 단건 조회
    DevelopmentResponse getDevelopment(Long id) throws NotFoundException;

    //개발품목 수정
    DevelopmentResponse modifyDevelopment(Long id, DevelopmentRequest request) throws NotFoundException, BadRequestException;

    //개발품목 삭제
    void deleteDevelopment(Long id) throws NotFoundException;

    //개발품목 진행상태 등록
    DevelopmentStateReponse createDevelopmentState(Long developId, DevelopmentStateRequest request) throws NotFoundException;

    //개발품목 진행상태 리스트 조회
    List<DevelopmentStateReponse> getDevelopmentStateList(
            Long developmentId,
            DevelopmentStatusType status,
            DevelopmentChildrenStatusType childrenStatus);

    //개발품목 진행상태 단건 조회
    DevelopmentStateReponse getDevelopmentState(Long developmentId, Long developmentStateId) throws NotFoundException;

    //개발품목 진행상태 수정
    DevelopmentStateReponse modifyDevelopmentState(Long developmentId, Long developmentStateId, DevelopmentStateRequest request) throws NotFoundException;

    //개발품목 진행상태 삭제
    void deleteDevelopmentState(Long developomentId, Long developmentStateId) throws NotFoundException;

    DevelopmentStateReponse createStatusFile(Long developId, Long developStateId, MultipartFile file) throws NotFoundException, BadRequestException, IOException;

}
