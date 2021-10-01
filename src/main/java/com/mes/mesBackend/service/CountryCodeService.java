package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.CountryCodeRequest;
import com.mes.mesBackend.dto.response.CountryCodeResponse;
import com.mes.mesBackend.entity.CountryCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CountryCodeService {
    // 국가코드 타입 생성
    CountryCodeResponse createCountryCode(CountryCodeRequest countryCodeRequest);

    // 국가코드 타입 조회
    CountryCodeResponse getCountryCode(Long id);

    // 국가코드 타입 전체 조회
    Page<CountryCodeResponse> getCountryCodes(Pageable pageable);

    // 국가코드 타입 수정
    CountryCodeResponse updateCountryCode(Long id, CountryCodeRequest countryCodeRequest);

    // 국가코드 삭제
    void deleteCountryCode(Long id);

    CountryCode findCountryCodeByIdAndUseYn(Long id);
}
