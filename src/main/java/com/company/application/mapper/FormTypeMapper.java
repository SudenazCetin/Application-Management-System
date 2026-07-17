package com.company.application.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.company.application.dto.FormTypeDto;
import com.company.application.entity.FormType;

// Bu arayuz, FormType ile FormTypeDto arasinda donusum saglar.
@Mapper
public interface FormTypeMapper {

    FormTypeDto toDto(FormType entity);

    @Mapping(target = "applicationForms", ignore = true)
    FormType toEntity(FormTypeDto dto);

    List<FormTypeDto> toDtoList(List<FormType> entities);

    List<FormType> toEntityList(List<FormTypeDto> dtos);
}
