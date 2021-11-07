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

    public CountryCode getCountryCodeOrThrow(Long id) throws NotFoundException {
        return countryCodeRepository
                .findByIdAndDeleteYnFalse(id).orElseThrow(() -> new NotFoundException("countryCode does not exists. input countryCodeId: " + id));
    }

    // 국가코드 타입 생성
    public CountryCodeResponse createCountryCode(CountryCodeRequest countryCodeRequest) {
        CountryCode countryCode = modelMapper.toEntity(countryCodeRequest, CountryCode.class);
        countryCodeRepository.save(countryCode);
        return modelMapper.toResponse(countryCode, CountryCodeResponse.class);
    }

    // 국가코드 타입 조회
    public CountryCodeResponse getCountryCode(Long id) throws NotFoundException {
        CountryCode countryCode = getCountryCodeOrThrow(id);
        return modelMapper.toResponse(countryCode, CountryCodeResponse.class);
    }

    // 국가코드 전체 조회
    public Page<CountryCodeResponse> getCountryCodes(Pageable pageable) {
        Page<CountryCode> countryCodes = countryCodeRepository.findAllByDeleteYnFalse(pageable);
        return modelMapper.toPageResponses(countryCodes, CountryCodeResponse.class);
    }

    // 국가코드 타입 수정
    public CountryCodeResponse updateCountryCode(Long id, CountryCodeRequest countryCodeRequest) throws NotFoundException {
        CountryCode newCountryCode = modelMapper.toEntity(countryCodeRequest, CountryCode.class);
        CountryCode findCountryCode = getCountryCodeOrThrow(id);
        findCountryCode.put(newCountryCode);
        countryCodeRepository.save(findCountryCode);
        return modelMapper.toResponse(findCountryCode, CountryCodeResponse.class);
    }

    // 국가코드 삭제
    public void deleteCountryCode(Long id) throws NotFoundException {
        CountryCode countryCode = getCountryCodeOrThrow(id);
        countryCode.delete();
        countryCodeRepository.save(countryCode);
    }
}
