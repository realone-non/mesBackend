package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.HeaderRequest;
import com.mes.mesBackend.dto.response.HeaderResponse;
import com.mes.mesBackend.entity.Header;
import com.mes.mesBackend.helper.Mapper;
import com.mes.mesBackend.repository.HeadersRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HeaderServiceImpl implements HeaderService {

    @Autowired
    HeadersRepository headersRepository;

    @Autowired
    Mapper mapper;

    // header 조회
    public List<HeaderResponse> getHeaders(String controllerName) {
        List<Header> headers = headersRepository.findAllByControllerNameOrderBySeq(controllerName);
        return mapper.toListResponses(headers, HeaderResponse.class);
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
}
