package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.DevelopmentRequest;
import com.mes.mesBackend.dto.request.DevelopmentStateRequest;
import com.mes.mesBackend.dto.response.DevelopmentResponse;
import com.mes.mesBackend.dto.response.DevelopmentStateReponse;
import com.mes.mesBackend.entity.Development;
import com.mes.mesBackend.entity.DevelopmentState;
import com.mes.mesBackend.entity.User;
import com.mes.mesBackend.entity.enumeration.DevelopmentChildrenStatusType;
import com.mes.mesBackend.entity.enumeration.DevelopmentStatusType;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.S3Uploader;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.DevelopmentRepository;
import com.mes.mesBackend.repository.DevelopmentStateRepository;
import com.mes.mesBackend.repository.UserRepository;
import com.mes.mesBackend.service.DevelopmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DevelopmentServiceImpl implements DevelopmentService {
    @Autowired
    DevelopmentRepository developmentRepository;

    @Autowired
    DevelopmentStateRepository developmentStateRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper mapper;

    @Autowired
    S3Uploader s3Uploader;

    //개발품목 등록
    public DevelopmentResponse createDevelopment(DevelopmentRequest request) throws NotFoundException {
        Development development = mapper.toEntity(request, Development.class);
        User user = userRepository.findByIdAndDeleteYnFalse(request.getUserId())
                .orElseThrow(() -> new NotFoundException("user does not exist. input id: " + request.getUserId()));
        development.setUser(user);
        developmentRepository.save(development);
        return mapper.toResponse(development, DevelopmentResponse.class);
    }

    //개발품목 리스트 조회
    public List<DevelopmentResponse> getDevelopments(
            LocalDate fromDate,
            LocalDate toDate,
            Long userId,
            String itemNo,
            String itemName,
            DevelopmentStatusType status,
            DevelopmentChildrenStatusType childrenStatus){
        return developmentRepository.findDevelopByCondition(
                userId, fromDate, toDate, itemNo, itemName, status, childrenStatus);
    }

    //개발품목 단건 조회
    public DevelopmentResponse getDevelopment(Long id) throws NotFoundException {
        return developmentRepository.findDevelopByIdAndDeleteYnFalse(id);
    }

    //개발품목 수정
    public DevelopmentResponse modifyDevelopment(Long id, DevelopmentRequest request) throws NotFoundException {
        Development dbDevelop = developmentRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("development does not exist. input id: " + id));
        User user = userRepository.findByIdAndDeleteYnFalse(request.getUserId())
                .orElseThrow(() -> new NotFoundException("user does not exist. input id: " + request.getUserId()));
        dbDevelop.setUser(user);
        dbDevelop.update(request);
        developmentRepository.save(dbDevelop);
        return mapper.toResponse(dbDevelop, DevelopmentResponse.class);
    }

    //개발품목 삭제
    public void deleteDevelopment(Long id) throws NotFoundException {
        Development dbDevelop = developmentRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("development does not exist. input id: " + id));
        dbDevelop.delete();
        developmentRepository.save(dbDevelop);
    }

    //개발품목 진행상태 등록
    public DevelopmentStateReponse createDevelopmentState(Long developId, DevelopmentStateRequest request) throws NotFoundException {
        Development development = developmentRepository.findByIdAndDeleteYnFalse(developId)
                .orElseThrow(() -> new NotFoundException("development does not exist. input id: " + developId));
        User user = userRepository.findByIdAndDeleteYnFalse(request.getUserId())
                .orElseThrow(() -> new NotFoundException("user does not exist. input id: " + request.getUserId()));
        DevelopmentState state = mapper.toEntity(request, DevelopmentState.class);
        development.setProcessState(state.getDevelopmentStatus() + "-" + state.getDevelopmentChildrenStatus());
        state.setDevelopment(development);
        state.setUser(user);
        developmentStateRepository.save(state);
        return mapper.toResponse(state, DevelopmentStateReponse.class);
    }

    //개발품목 진행상태 리스트 조회
    public List<DevelopmentStateReponse> getDevelopmentStateList(
            Long developmentId,
            DevelopmentStatusType status,
            DevelopmentChildrenStatusType childrenStatus){
        return developmentRepository.findAllStateByCondition(developmentId, status, childrenStatus);
    }

    //개발품목 진행상태 단건 조회
    public DevelopmentStateReponse getDevelopmentState(Long developId, Long developStateId) throws NotFoundException {
        return developmentRepository.findByIdAndDeleteYn(developId, developStateId);
    }

    //개발품목 진행상태 수정
    public DevelopmentStateReponse modifyDevelopmentState(
            Long developId,
            Long developStateId,
            DevelopmentStateRequest request) throws NotFoundException {
        Development development = developmentRepository.findByIdAndDeleteYnFalse(developId)
                .orElseThrow(() -> new NotFoundException("development does not exist. input id: " + developId));
        DevelopmentState state = developmentStateRepository.findByIdAndDeleteYnFalse(developStateId)
                .orElseThrow(() -> new NotFoundException("developmentState does not exist. input id: " + developStateId));
        User user = userRepository.findByIdAndDeleteYnFalse(request.getUserId())
                .orElseThrow(() -> new NotFoundException("user does not exist. input id: " + request.getUserId()));
        development.setProcessState(request.getDevelopmentStatus() + "-" + request.getDevelopmentChildrenStatus());
        developmentRepository.save(development);
        state.setUser(user);
        state.update(request);
        developmentStateRepository.save(state);
        return developmentRepository.findByIdAndDeleteYn(developId, developStateId);
    }

    //개발품목 진행상태 삭제
    public void deleteDevelopmentState(Long developId, Long developStateId) throws NotFoundException {
        Development development = developmentRepository.findByIdAndDeleteYnFalse(developId)
                .orElseThrow(() -> new NotFoundException("development does not exist. input id: " + developId));
        DevelopmentState state = developmentStateRepository.findByIdAndDeleteYnFalse(developStateId)
                .orElseThrow(() -> new NotFoundException("developmentState does not exist. input id: " + developStateId));
        state.delete();
        developmentStateRepository.save(state);
    }

    // 개발 진행 관련 파일 업로드
    public DevelopmentStateReponse createStatusFile(Long developId, Long developStateId, MultipartFile file) throws NotFoundException, BadRequestException, IOException {
        Development development = developmentRepository.findByIdAndDeleteYnFalse(developId)
                .orElseThrow(() -> new NotFoundException("development does not exist. input id: " + developId));
        DevelopmentState state = developmentStateRepository.findByIdAndDeleteYnFalse(developStateId)
                .orElseThrow(() -> new NotFoundException("developmentState does not exist. input id: " + developStateId));
        state.setFileUrl(s3Uploader.upload(file, "develop"));
        developmentStateRepository.save(state);
        return developmentRepository.findByIdAndDeleteYn(developId, developStateId);
    }
}
