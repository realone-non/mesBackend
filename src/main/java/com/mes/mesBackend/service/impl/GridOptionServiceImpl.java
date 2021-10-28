package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.GridOptionRequest;
import com.mes.mesBackend.dto.response.GridOptionResponse;
import com.mes.mesBackend.entity.GridOption;
import com.mes.mesBackend.entity.Header;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.Mapper;
import com.mes.mesBackend.repository.GridOptionRepository;
import com.mes.mesBackend.repository.HeadersRepository;
import com.mes.mesBackend.service.GridOptionService;
import com.mes.mesBackend.service.HeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GridOptionServiceImpl implements GridOptionService {

    @Autowired
    GridOptionRepository gridOptionRepository;

    @Autowired
    HeadersRepository headersRepository;

    @Autowired
    HeaderService headerService;

    @Autowired
    Mapper mapper;

    // 다중 생성
    @Override
    public List<GridOptionResponse> createGridOptions(Long userId, List<GridOptionRequest> gridOptionRequest) {
        List<GridOption> gridOptions = mapper.toEntities(gridOptionRequest, GridOption.class);
        List<GridOption> saveGrids = gridOptionRepository.saveAll(gridOptions);
        return mapper.toListResponses(saveGrids, GridOptionResponse.class);
    }

    // 단일 생성
    @Override
    public GridOptionResponse createGridOption(
            Long headerId, String controllerName, GridOptionRequest gridOptionRequest
    ) throws NotFoundException {
        Header header = headerService.findHeader(headerId);
        GridOption gridOption = mapper.toEntity(gridOptionRequest, GridOption.class);
        gridOption.setHeader(header);
        GridOption saveGrid = gridOptionRepository.save(gridOption);
        return mapper.toResponse(saveGrid, GridOptionResponse.class);
    }
}
