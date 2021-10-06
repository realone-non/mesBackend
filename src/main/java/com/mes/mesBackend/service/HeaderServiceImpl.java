package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.HeaderRequest;
import com.mes.mesBackend.dto.response.HeaderResponse;
import com.mes.mesBackend.entity.Header;
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
    ModelMapper modelMapper;

    // header 조회
    public List<HeaderResponse> getHeaders(String controllerName) {
        List<Header> headers = headersRepository.findAllByControllerNameOrderBySeq(controllerName);

        return headers.stream().map(header -> modelMapper.map(header, HeaderResponse.class)).collect(Collectors.toList());
    }

    // 헤더 생성
    public HeaderResponse createHeader(HeaderRequest headerRequest) {
        Header header = modelMapper.map(headerRequest, Header.class);
        Header saveHeader = headersRepository.save(header);
        return modelMapper.map(saveHeader, HeaderResponse.class);
    }

    // 헤더 수정
    public HeaderResponse updateHeader(Long id, HeaderRequest headerRequest) {
        Header findHeader = headersRepository.findById(id).orElseThrow(() -> new RuntimeException(""));

        Header updateHeader = modelMapper.map(headerRequest, Header.class);

        findHeader.setHeader(updateHeader.getHeader());
        findHeader.setColumnName(updateHeader.getColumnName());
        findHeader.setControllerName(updateHeader.getControllerName());
        findHeader.setSeq(updateHeader.getSeq());

        Header saveHeader = headersRepository.save(findHeader);
        return modelMapper.map(saveHeader, HeaderResponse.class);
    }

    // 헤더 삭제
    public void deleteHeader(Long id) {
        headersRepository.deleteById(id);
    }
}
