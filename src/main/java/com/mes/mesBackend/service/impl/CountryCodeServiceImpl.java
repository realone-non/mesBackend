package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.CountryCodeRequest;
import com.mes.mesBackend.dto.response.CountryCodeResponse;
import com.mes.mesBackend.entity.CountryCode;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.CountryCodeRepository;
import com.mes.mesBackend.service.CountryCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CountryCodeServiceImpl implements CountryCodeService {

    @Autowired CountryCodeRepository countryCodeRepository;

    @Autowired
    ModelMapper modelMapper;

    public CountryCode findCountryCodeByIdAndDeleteYn(Long id) throws NotFoundException {
        CountryCode findCountryCode = countryCodeRepository.findByIdAndDeleteYnFalse(id);
        if (findCountryCode == null) {
            throw new NotFoundException("countryCode does not exists. input countryCodeId: " + id);
        }
        return findCountryCode;
    }

    // 국가코드 타입 생성
    public CountryCodeResponse createCountryCode(CountryCodeRequest countryCodeRequest) {
        CountryCode countryCode = modelMapper.toEntity(countryCodeRequest, CountryCode.class);
        return modelMapper.toResponse(countryCodeRepository.save(countryCode), CountryCodeResponse.class);
    }

    // 국가코드 타입 조회
    public CountryCodeResponse getCountryCode(Long id) throws NotFoundException {
        CountryCode countryCode = findCountryCodeByIdAndDeleteYn(id);
        return modelMapper.toResponse(countryCode, CountryCodeResponse.class);
    }

    // 업체 타입 전체 조회
    public Page<CountryCodeResponse> getCountryCodes(Pageable pageable) {
        Page<CountryCode> countryCodes = countryCodeRepository.findAllByDeleteYnFalse(pageable);
        return modelMapper.toPageResponses(countryCodes, CountryCodeResponse.class);
    }

    // 국가코드 타입 수정
    public CountryCodeResponse updateCountryCode(Long id, CountryCodeRequest countryCodeRequest) throws NotFoundException {
        CountryCode countryCode = modelMapper.toEntity(countryCodeRequest, CountryCode.class);
        CountryCode findCountryCode = findCountryCodeByIdAndDeleteYn(id);
        findCountryCode.setName(countryCode.getName());
        CountryCode updateCountryCode = countryCodeRepository.save(findCountryCode);
        return modelMapper.toResponse(updateCountryCode, CountryCodeResponse.class);
    }

    // 국가코드 삭제
    public void deleteCountryCode(Long id) throws NotFoundException {
        CountryCode countryCode = findCountryCodeByIdAndDeleteYn(id);
        countryCode.setDeleteYn(true);
        countryCodeRepository.save(countryCode);
    }
}
