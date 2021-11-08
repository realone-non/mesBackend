package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.RoutingDetailRequest;
import com.mes.mesBackend.dto.request.RoutingRequest;
import com.mes.mesBackend.dto.response.RoutingDetailResponse;
import com.mes.mesBackend.dto.response.RoutingResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.RoutingDetailsRepository;
import com.mes.mesBackend.repository.RoutingRepository;
import com.mes.mesBackend.service.RoutingService;
import com.mes.mesBackend.service.WareHouseService;
import com.mes.mesBackend.service.WorkCenterService;
import com.mes.mesBackend.service.WorkProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoutingServiceImpl implements RoutingService {
    @Autowired
    RoutingRepository routingRepository;

    @Autowired
    RoutingDetailsRepository routingDetailRepository;

    @Autowired
    WorkProcessService workProcessService;
    @Autowired
    WorkCenterService workCenterService;
    @Autowired
    WareHouseService wareHouseService;

    @Autowired
    ModelMapper mapper;

    // 라우팅 단일 조회 및 예외
    @Override
    public Routing getRoutingOrThrow(Long id) throws NotFoundException {
        return routingRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("routing does not exist. input id: " + id));
    }

    // 라우팅 생성
    @Override
    public RoutingResponse createRouting(RoutingRequest routingRequest) {
        Routing routing = mapper.toEntity(routingRequest, Routing.class);
        routingRepository.save(routing);
        return mapper.toResponse(routing, RoutingResponse.class);
    }

    // 라우팅 단일 조회
    @Override
    public RoutingResponse getRouting(Long id) throws NotFoundException {
        Routing routing = getRoutingOrThrow(id);
        return mapper.toResponse(routing, RoutingResponse.class);
    }

    // 라우팅 페이징 조회
    @Override
    public Page<RoutingResponse> getRoutings(Pageable pageable) {
        Page<Routing> routings = routingRepository.findAllByDeleteYnFalse(pageable);
        return mapper.toPageResponses(routings, RoutingResponse.class);
    }

    // 라우팅 수정
    @Override
    public RoutingResponse updateRouting(Long id, RoutingRequest routingRequest) throws NotFoundException {
        Routing findRouting = getRoutingOrThrow(id);
        Routing newRouting = mapper.toEntity(routingRequest, Routing.class);
        findRouting.update(newRouting);
        routingRepository.save(findRouting);
        return mapper.toResponse(findRouting, RoutingResponse.class);
    }

    // 라우팅 삭제
    @Override
    public void deleteRouting(Long id) throws NotFoundException {
        Routing routing = getRoutingOrThrow(id);
        List<RoutingDetail> routingDetails = routingDetailRepository.findAllByRoutingAndDeleteYnFalse(routing);
        for (RoutingDetail routingDetail : routingDetails) {
            routingDetail.delete();
        }
        routingDetailRepository.saveAll(routingDetails);

        routing.delete();
        routingRepository.save(routing);
    }



    // 라우팅 디테일 생성
    @Override
    public RoutingDetailResponse createRoutingDetails(Long routingId, RoutingDetailRequest routingDetailRequest) throws NotFoundException {
        Routing routing = getRoutingOrThrow(routingId);
        RoutingDetail routingDetail = mapper.toEntity(routingDetailRequest, RoutingDetail.class);

        WorkProcess workProcess = workProcessService.getWorkProcessOrThrow(routingDetailRequest.getWorkProcess());
        WorkCenter workCenter = workCenterService.getWorkCenterOrThrow(routingDetailRequest.getWorkCenter());
        WareHouse rawMaterialWareHouse = wareHouseService.getWareHouseOrThrow(routingDetailRequest.getRawMaterialWareHouse());
        WareHouse inputWareHouse = wareHouseService.getWareHouseOrThrow(routingDetailRequest.getInputWareHouse());

        routingDetail.addJoin(routing, workProcess, workCenter, rawMaterialWareHouse, inputWareHouse);
        routingDetailRepository.save(routingDetail);
        return mapper.toResponse(routingDetail, RoutingDetailResponse.class);
    }

    // 라우팅 디테일 리스트 조회
    @Override
    public List<RoutingDetailResponse> getRoutingDetails(Long routingId) throws NotFoundException {
        Routing routing = getRoutingOrThrow(routingId);
        List<RoutingDetail> routingDetails = routingDetailRepository.findAllByRoutingAndDeleteYnFalse(routing);
        return mapper.toListResponses(routingDetails, RoutingDetailResponse.class);
    }

    // 라우팅 디테일 수정
    @Override
    public RoutingDetailResponse updateRoutingDetail(
            Long routingId, 
            Long routingDetailId, 
            RoutingDetailRequest routingDetailRequest
    ) throws NotFoundException {
        RoutingDetail findRoutingDetail = getRoutingDetailOrThrow(routingId, routingDetailId);
        RoutingDetail newRoutingDetail = mapper.toEntity(routingDetailRequest, RoutingDetail.class);

        WorkProcess newWorkProcess = workProcessService.getWorkProcessOrThrow(routingDetailRequest.getWorkProcess());
        WorkCenter newWorkCenter = workCenterService.getWorkCenterOrThrow(routingDetailRequest.getWorkCenter());
        WareHouse newRawMaterialWareHouse = wareHouseService.getWareHouseOrThrow(routingDetailRequest.getRawMaterialWareHouse());
        WareHouse newInputWareHouse = wareHouseService.getWareHouseOrThrow(routingDetailRequest.getInputWareHouse());

        findRoutingDetail.update(newRoutingDetail, newWorkProcess, newWorkCenter, newRawMaterialWareHouse, newInputWareHouse);
        routingDetailRepository.save(findRoutingDetail);
        return mapper.toResponse(findRoutingDetail, RoutingDetailResponse.class);
    }

    // 라우팅 디테일 삭제
    @Override
    public void deleteRoutingDetail(Long routingId, Long routingDetailId) throws NotFoundException {
        RoutingDetail routingDetail = getRoutingDetailOrThrow(routingId, routingDetailId);
        routingDetail.delete();
        routingDetailRepository.save(routingDetail);
    }

    // 라우팅 조회 및 예외
    private RoutingDetail getRoutingDetailOrThrow(
            Long routingId,
            Long routingDetailId
    ) throws NotFoundException {
        Routing routing = getRoutingOrThrow(routingId);
        RoutingDetail routingDetail = routingDetailRepository.findByIdAndRoutingAndDeleteYnFalse(routingDetailId, routing);
        if (routingDetail == null) {
            throw new NotFoundException("routingDetail does not exist. input routingId: " + routingId + ", input routingDetailId: " + routingDetailId);
        }
        return routingDetail;
    }
}
