package com.company.application.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.company.application.dto.ApplicationFormDto;
import com.company.application.entity.ApplicationForm;
import com.company.application.entity.FormType;
import com.company.application.entity.User;

// MapStruct annotation processing devre disi olsa bile mapper bean'ini saglar.
@Component
@Primary
public class ApplicationFormManualMapper implements ApplicationFormMapper {

    @Override
    public ApplicationFormDto toDto(ApplicationForm entity) {
        if (entity == null) {
            return null;
        }

        ApplicationFormDto dto = new ApplicationFormDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setApplicationDate(entity.getApplicationDate());
        dto.setStatus(entity.getStatus());

        User user = entity.getUser();
        if (user != null) {
            dto.setUserId(user.getId());
        }

        FormType formType = entity.getFormType();
        if (formType != null) {
            dto.setFormTypeId(formType.getId());
        }

        return dto;
    }

    @Override
    public ApplicationForm toEntity(ApplicationFormDto dto) {
        if (dto == null) {
            return null;
        }

        ApplicationForm entity = new ApplicationForm();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setApplicationDate(dto.getApplicationDate());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    @Override
    public List<ApplicationFormDto> toDtoList(List<ApplicationForm> entities) {
        if (entities == null) {
            return null;
        }

        List<ApplicationFormDto> result = new ArrayList<>(entities.size());
        for (ApplicationForm entity : entities) {
            result.add(toDto(entity));
        }
        return result;
    }

    @Override
    public List<ApplicationForm> toEntityList(List<ApplicationFormDto> dtos) {
        if (dtos == null) {
            return null;
        }

        List<ApplicationForm> result = new ArrayList<>(dtos.size());
        for (ApplicationFormDto dto : dtos) {
            result.add(toEntity(dto));
        }
        return result;
    }
}
