package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.GridOptionRequest;
import com.mes.mesBackend.dto.response.GridOptionResponse;
import com.mes.mesBackend.dto.response.HeaderResponse;
import com.mes.mesBackend.entity.GridOption;
import com.mes.mesBackend.entity.Header;
import com.mes.mesBackend.entity.User;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.GridOptionRepository;
import com.mes.mesBackend.repository.HeadersRepository;
import com.mes.mesBackend.service.GridOptionService;
import com.mes.mesBackend.service.HeaderService;
import com.mes.mesBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    UserService userService;

    @Autowired
    ModelMapper modelMapper;

    // 생성
    @Override
    public GridOptionResponse createGridOption(
            Long headerId, GridOptionRequest gridOptionRequest, Long userId
    ) throws NotFoundException {
        Header header = headerService.findHeader(headerId);
        User user = userService.findUserOrThrow(userId);
        GridOption gridOption = modelMapper.toEntity(gridOptionRequest, GridOption.class);

        boolean existsGrid = gridOptionRepository.existsByHeaderAndUserId(header, userId);

        gridOption.add(header, user);

        if (!existsGrid) {
            gridOptionRepository.save(gridOption);
        } else {
            GridOption findGrid = gridOptionRepository.findByHeaderAndUserId(header, userId);
            findGrid.put(gridOption);
            gridOptionRepository.save(findGrid);
        }
        return modelMapper.toResponse(gridOption, GridOptionResponse.class);
    }

    // 헤더, 그리드 옵션 동시 조회
    @Override
    public List<HeaderResponse> getHeaderGridOptions(Long userId, String controllerName) throws NotFoundException {
        // controller로 조회
        List<Header> headers = headerService.findHeaders(controllerName);
        List<HeaderResponse> headerResponses = new ArrayList<>();

        for (Header header : headers) {
            // header와 user에 해당하는 grid를 찾음
            GridOption grid = gridOptionRepository.findByHeaderAndUserId(header, userId);
            GridOptionResponse gridOptionResponse = modelMapper.toResponse(grid, GridOptionResponse.class);
            HeaderResponse headerResponse = modelMapper.toResponse(header, HeaderResponse.class);
            headerResponse.setGridOptionResponse(gridOptionResponse);
            headerResponses.add(headerResponse);
        }
        return headerResponses;
    }
}
