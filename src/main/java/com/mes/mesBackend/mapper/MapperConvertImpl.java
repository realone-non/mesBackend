package com.mes.mesBackend.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class MapperConvertImpl implements MapperConvert {

    @Autowired
    ModelMapper modelMapper;
}
