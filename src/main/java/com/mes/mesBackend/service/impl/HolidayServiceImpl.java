package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.HolidayRequest;
import com.mes.mesBackend.dto.response.HolidayResponse;
import com.mes.mesBackend.entity.Holiday;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.HolidayRepository;
import com.mes.mesBackend.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class HolidayServiceImpl implements HolidayService {

    @Autowired
    HolidayRepository holidayRepo;

    @Autowired
    ModelMapper _mapper;

    // 휴일 생성
    public HolidayResponse createHoliday(HolidayRequest request) throws BadRequestException{
        Holiday holiday = _mapper.toEntity(request, Holiday.class);
        holidayRepo.save(holiday);
        return _mapper.toResponse(holiday, HolidayResponse.class);
    }
    // 휴일 단일 조회
    public HolidayResponse getHoliday(Long id) throws NotFoundException{
        Holiday holiday = holidayRepo.findByIdAndDeleteYnFalse(id).orElseThrow(()-> new NotFoundException("holidayInfo not in db:" + id));
        return _mapper.toResponse(holiday, HolidayResponse.class);
    }
    // 휴일 페이징 조회
    public List<HolidayResponse> getHolidays(){
        List<Holiday> holidays = holidayRepo.findAllByDeleteYnFalse();
        return _mapper.toListResponses(holidays, HolidayResponse.class);
    }
    // 휴일 수정
    public HolidayResponse updateHoliday(Long id, HolidayRequest request) throws NotFoundException, BadRequestException{
        Holiday dbHoliday = holidayRepo.findByIdAndDeleteYnFalse(id).orElseThrow(()-> new NotFoundException("holidayInfo not in db:" + id));
        dbHoliday.update(request);
        holidayRepo.save(dbHoliday);
        return _mapper.toResponse(dbHoliday, HolidayResponse.class);

    }
    // 휴일 삭제
    public void deleteHoliday(Long id) throws NotFoundException{
        Holiday dbHoliday = holidayRepo.findByIdAndDeleteYnFalse(id).orElseThrow(()-> new NotFoundException("holidayInfo not in db:" + id));
        dbHoliday.delete();
        holidayRepo.save(dbHoliday);
    }
}
