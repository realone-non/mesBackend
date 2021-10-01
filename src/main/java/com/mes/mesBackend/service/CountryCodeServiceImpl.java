package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.CountryCodeRequest;
import com.mes.mesBackend.dto.response.CountryCodeResponse;
import com.mes.mesBackend.entity.CountryCode;
import com.mes.mesBackend.repository.CountryCodeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryCodeServiceImpl implements CountryCodeService {

    @Autowired
    private CountryCodeRepository countryCodeRepository;

    @Autowired
    ModelMapper modelMapper;

    public CountryCode findCountryCodeByIdAndUseYn(Long id) {
        return countryCodeRepository.findByIdAndUseYnTrue(id);
    }

    // 국가코드 타입 생성
    public CountryCodeResponse createCountryCode(CountryCodeRequest countryCodeRequest) {
        CountryCode countryCode = countryCodeRequestToCountryCode(countryCodeRequest);
        CountryCode saveCountryCode = countryCodeRepository.save(countryCode);
        return countryCodeToCountryCodeResponse(saveCountryCode);
    }

    // 국가코드 타입 조회
    public CountryCodeResponse getCountryCode(Long id) {
        CountryCode countryCode = findCountryCodeByIdAndUseYn(id);
        return countryCodeToCountryCodeResponse(countryCode);
    }

    // 업체 타입 전체 조회
    public Page<CountryCodeResponse> getCountryCodes(Pageable pageable) {
        Page<CountryCode> countryCodes = countryCodeRepository.findAllByUseYnTrue(pageable);
        return countryCodeToPageCountryCodeResponses(countryCodes);
    }

    // 국가코드 타입 수정
    public CountryCodeResponse updateCountryCode(Long id, CountryCodeRequest countryCodeRequest) {
        CountryCode countryCode = countryCodeRequestToCountryCode(countryCodeRequest);
        CountryCode findCountryCode = findCountryCodeByIdAndUseYn(id);
        findCountryCode.setName(countryCode.getName());
        CountryCode updateCountryCode = countryCodeRepository.save(findCountryCode);
        return countryCodeToCountryCodeResponse(updateCountryCode);
    }

    // 국가코드 삭제
    public void deleteCountryCode(Long id) {
        CountryCode countryCode = findCountryCodeByIdAndUseYn(id);
        countryCode.setUseYn(false);
        countryCodeRepository.save(countryCode);
    }

    // Entity -> Response
    private CountryCodeResponse countryCodeToCountryCodeResponse(CountryCode countryCode) {
        return modelMapper.map(countryCode, CountryCodeResponse.class);
    }

    // List<entity> -> List<Response>
    private List<CountryCodeResponse> countryCodeToListCountryCodeResponses(List<CountryCode> countryCodes) {
        return countryCodes
                .stream()
                .map(countryCode ->
                        modelMapper.map(countryCode, CountryCodeResponse.class)).collect(Collectors.toList());
    }

    // Request -> Entity
    private CountryCode countryCodeRequestToCountryCode(CountryCodeRequest countryCodeRequest) {
        return modelMapper.map(countryCodeRequest, CountryCode.class);
    }

    // Page<Entity> -> Page<Response>
    private Page<CountryCodeResponse> countryCodeToPageCountryCodeResponses(Page<CountryCode> countryCodes) {
        return countryCodes.map(countryCode -> modelMapper.map(countryCode, CountryCodeResponse.class));
    }
}
