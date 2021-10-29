package com.mes.mesBackend.mapper;

import org.springframework.data.domain.Page;

import java.util.List;

public interface ModelMapper {


    <E, R> E toEntity(R source, Class<? extends E> destinationType);

    <E, R> List<E> toEntities(List<R> sources, Class<? extends E> destinationType);

    <E, R> R toResponse(E source, Class<? extends  R> destinationType);

    <E, R> Page<R> toPageResponses(Page<E> sources, Class<? extends R> destinationType);

    <E, R> List<R> toListResponses(List<E> sources, Class<? extends R> destinationType);
}
