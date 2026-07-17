package com.company.application.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.company.application.dto.FormTypeDto;
import com.company.application.entity.FormType;

// MapStruct annotation processing devre disi olsa bile mapper bean'ini saglar.
@Component
@Primary
public class FormTypeManualMapper implements FormTypeMapper {

    @Override
    public FormTypeDto toDto(FormType entity) {
        if (entity == null) {
            return null;
        }

        FormTypeDto dto = new FormTypeDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    @Override
    public FormType toEntity(FormTypeDto dto) {
        if (dto == null) {
            return null;
        }

        FormType entity = new FormType();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    @Override
    public List<FormTypeDto> toDtoList(List<FormType> entities) {
        if (entities == null) {
            return null;
        }

        List<FormTypeDto> result = new ArrayList<>(entities.size());
        for (FormType entity : entities) {
            result.add(toDto(entity));
        }
        return result;
    }

    @Override
    public List<FormType> toEntityList(List<FormTypeDto> dtos) {
        if (dtos == null) {
            return null;
        }

        List<FormType> result = new ArrayList<>(dtos.size());
        for (FormTypeDto dto : dtos) {
            result.add(toEntity(dto));
        }
        return result;
    }
}
