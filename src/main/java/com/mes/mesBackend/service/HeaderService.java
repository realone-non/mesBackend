package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.HeaderRequest;
import com.mes.mesBackend.dto.response.HeaderResponse;
import com.mes.mesBackend.entity.Header;
import com.mes.mesBackend.exception.NotFoundException;
import jdk.javadoc.internal.doclets.formats.html.markup.Head;

import java.util.List;


public interface HeaderService {
    // 헤더 조회
    List<HeaderResponse> getHeaders(String controllerName) throws NotFoundException;

    // 헤더 생성
    HeaderResponse createHeader(HeaderRequest headerRequest);

    // 헤더 수정
    HeaderResponse updateHeader(Long id, HeaderRequest headerRequest);

    // 헤더 삭제
    void deleteHeader(Long id);

    Header findHeader(Long id) throws NotFoundException;

    List<Header> findHeaders(String controllerName) throws NotFoundException;
}
