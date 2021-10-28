package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.HeaderRequest;
import com.mes.mesBackend.dto.response.GridOptionResponse;
import com.mes.mesBackend.dto.response.HeaderResponse;
import com.mes.mesBackend.entity.GridOption;
import com.mes.mesBackend.entity.Header;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.Mapper;
import com.mes.mesBackend.repository.GridOptionRepository;
import com.mes.mesBackend.repository.HeadersRepository;
import com.mes.mesBackend.service.HeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeaderServiceImpl implements HeaderService {

    @Autowired
    HeadersRepository headersRepository;

    @Autowired
    GridOptionRepository gridOptionRepository;

    @Autowired
    Mapper mapper;

    // header 조회
    public List<HeaderResponse> getHeaders(String controllerName) {
        List<Header> headers = headersRepository.findAllByControllerNameOrderBySeq(controllerName);
        return  mapper.toListResponses(headers, HeaderResponse.class);
    }

    // 망한거
//    public List<HeaderResponse> getHeaders(String controllerName, String userId) throws NotFoundException {
//        List<Header> headers = headersRepository.findAllByControllerNameOrderBySeq(controllerName);
//        List<HeaderResponse> headerResponses = mapper.toListResponses(headers, HeaderResponse.class);
//        for (HeaderResponse headerResponse : headerResponses) {
//            Long headerId = headerResponse.getId();
//            headerResponse.setGridOptionResponse(getGrid(headerId, userId, headerResponse));
//        }
//        return headerResponses;
//    }

    // 그리드 정보 조회
    private GridOptionResponse getGrid(Long headerId, String userId, HeaderResponse headerResponse) throws NotFoundException {
        Header header = headersRepository.findById(headerId).orElseThrow(() -> new NotFoundException("0"));
        GridOption gridResponse = gridOptionRepository.findByHeaderAndUserId(header, userId);
        return mapper.toResponse(gridResponse, GridOptionResponse.class);
    }

    // 헤더 생성
    public HeaderResponse createHeader(HeaderRequest headerRequest) {
        Header header = mapper.toEntity(headerRequest, Header.class);
        Header saveHeader = headersRepository.save(header);
        return mapper.toResponse(saveHeader, HeaderResponse.class);
    }

    // 헤더 수정
    public HeaderResponse updateHeader(Long id, HeaderRequest headerRequest) {
        Header findHeader = headersRepository.findById(id).orElseThrow(() -> new RuntimeException(""));

        Header updateHeader = mapper.toEntity(headerRequest, Header.class);

        findHeader.put(updateHeader);

        Header saveHeader = headersRepository.save(findHeader);
        return mapper.toResponse(saveHeader, HeaderResponse.class);
    }

    // 헤더 삭제
    public void deleteHeader(Long id) {
        headersRepository.deleteById(id);
    }

    public Header findHeader(Long id) throws NotFoundException {
        return headersRepository.findById(id).orElseThrow(() -> new NotFoundException("header does not exists. input id: "+ id));
    }
}
