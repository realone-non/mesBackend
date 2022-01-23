package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.DevelopmentRequest;
import com.mes.mesBackend.dto.request.DevelopmentStateRequest;
import com.mes.mesBackend.dto.response.DevelopmentResponse;
import com.mes.mesBackend.dto.response.DevelopmentStateReponse;
import com.mes.mesBackend.entity.ProductDevelopment;
import com.mes.mesBackend.entity.ProductDevelopmentState;
import com.mes.mesBackend.entity.User;
import com.mes.mesBackend.entity.enumeration.DevelopmentChildrenStatusType;
import com.mes.mesBackend.entity.enumeration.DevelopmentStatusType;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.S3Uploader;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.ProductDevelopmentRepository;
import com.mes.mesBackend.repository.ProductDevelopmentStateRepository;
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
    ProductDevelopmentRepository productDevelopmentRepository;

    @Autowired
    ProductDevelopmentStateRepository productDevelopmentStateRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper mapper;

    @Autowired
    S3Uploader s3Uploader;

    //개발품목 등록
    public DevelopmentResponse createDevelopment(DevelopmentRequest request) throws NotFoundException {
        ProductDevelopment productDevelopment = mapper.toEntity(request, ProductDevelopment.class);
        User user = userRepository.findByIdAndDeleteYnFalse(request.getUserId())
                .orElseThrow(() -> new NotFoundException("user does not exist. input id: " + request.getUserId()));
        productDevelopment.setUser(user);
        productDevelopmentRepository.save(productDevelopment);
        return mapper.toResponse(productDevelopment, DevelopmentResponse.class);
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
        return productDevelopmentRepository.findDevelopByCondition(
                userId, fromDate, toDate, itemNo, itemName, status, childrenStatus);
    }

    //개발품목 단건 조회
    public DevelopmentResponse getDevelopment(Long id) throws NotFoundException {
        return mapper.toResponse(productDevelopmentRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("development does not exist. input id: " + id)), DevelopmentResponse.class);
    }

    //개발품목 수정
    public DevelopmentResponse modifyDevelopment(Long id, DevelopmentRequest request) throws NotFoundException {
        ProductDevelopment dbDevelop = productDevelopmentRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("development does not exist. input id: " + id));
        User user = userRepository.findByIdAndDeleteYnFalse(request.getUserId())
                .orElseThrow(() -> new NotFoundException("user does not exist. input id: " + request.getUserId()));
        dbDevelop.setUser(user);
        dbDevelop.update(request);
        productDevelopmentRepository.save(dbDevelop);
        return mapper.toResponse(dbDevelop, DevelopmentResponse.class);
    }

    //개발품목 삭제
    public void deleteDevelopment(Long id) throws NotFoundException {
        ProductDevelopment dbDevelop = productDevelopmentRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("development does not exist. input id: " + id));
        dbDevelop.delete();
        productDevelopmentRepository.save(dbDevelop);
    }

    //개발품목 진행상태 등록
    public DevelopmentStateReponse createDevelopmentState(Long developId, DevelopmentStateRequest request) throws NotFoundException {
        ProductDevelopment productDevelopment = productDevelopmentRepository.findByIdAndDeleteYnFalse(developId)
                .orElseThrow(() -> new NotFoundException("development does not exist. input id: " + developId));
        ProductDevelopmentState state = mapper.toEntity(request, ProductDevelopmentState.class);
        state.setProductDevelopment(productDevelopment);
        productDevelopmentStateRepository.save(state);
        return mapper.toResponse(state, DevelopmentStateReponse.class);
    }

    //개발품목 진행상태 리스트 조회
    public List<DevelopmentStateReponse> getDevelopmentStateList(
            Long developmentId,
            DevelopmentStatusType status,
            DevelopmentChildrenStatusType childrenStatus){
        return productDevelopmentRepository.findAllStateByCondition(developmentId, status, childrenStatus);
    }

    //개발품목 진행상태 단건 조회
    public DevelopmentStateReponse getDevelopmentState(Long developId, Long developStateId) throws NotFoundException {
        ProductDevelopment productDevelopment = productDevelopmentRepository.findByIdAndDeleteYnFalse(developId)
                .orElseThrow(() -> new NotFoundException("development does not exist. input id: " + developId));
        return mapper.toResponse(productDevelopmentStateRepository.findByIdAndDeleteYnFalse(developStateId), DevelopmentStateReponse.class);
    }

    //개발품목 진행상태 수정
    public DevelopmentStateReponse modifyDevelopmentState(
            Long developId,
            Long developStateId,
            DevelopmentStateRequest request) throws NotFoundException {
        ProductDevelopment productDevelopment = productDevelopmentRepository.findByIdAndDeleteYnFalse(developId)
                .orElseThrow(() -> new NotFoundException("development does not exist. input id: " + developId));
        ProductDevelopmentState state = productDevelopmentStateRepository.findByIdAndDeleteYnFalse(developStateId)
                .orElseThrow(() -> new NotFoundException("developmentState does not exist. input id: " + developStateId));
        User user = userRepository.findByIdAndDeleteYnFalse(request.getUserId())
                .orElseThrow(() -> new NotFoundException("user does not exist. input id: " + request.getUserId()));
        state.setUser(user);
        state.update(request);
        productDevelopmentStateRepository.save(state);
        return mapper.toResponse(state, DevelopmentStateReponse.class);
    }

    //개발품목 진행상태 삭제
    public void deleteDevelopmentState(Long developId, Long developStateId) throws NotFoundException {
        ProductDevelopment productDevelopment = productDevelopmentRepository.findByIdAndDeleteYnFalse(developId)
                .orElseThrow(() -> new NotFoundException("development does not exist. input id: " + developId));
        ProductDevelopmentState state = productDevelopmentStateRepository.findByIdAndDeleteYnFalse(developStateId)
                .orElseThrow(() -> new NotFoundException("developmentState does not exist. input id: " + developStateId));
        state.delete();
        productDevelopmentStateRepository.save(state);
    }

    // 개발 진행 관련 파일 업로드
    public DevelopmentStateReponse createStatusFile(Long developId, Long developStateId, MultipartFile file) throws NotFoundException, BadRequestException, IOException {
        ProductDevelopment productDevelopment = productDevelopmentRepository.findByIdAndDeleteYnFalse(developId)
                .orElseThrow(() -> new NotFoundException("development does not exist. input id: " + developId));
        ProductDevelopmentState state = productDevelopmentStateRepository.findByIdAndDeleteYnFalse(developStateId)
                .orElseThrow(() -> new NotFoundException("developmentState does not exist. input id: " + developStateId));
        state.setFileUrl(s3Uploader.upload(file, "develop"));
        return mapper.toResponse(state, DevelopmentStateReponse.class);
    }
}
