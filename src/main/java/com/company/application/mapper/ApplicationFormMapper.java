package com.company.application.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.company.application.dto.ApplicationFormDto;
import com.company.application.entity.ApplicationForm;

// Bu arayuz, ApplicationForm ile ApplicationFormDto arasinda donusum saglar.
@Mapper
public interface ApplicationFormMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "formType.id", target = "formTypeId")
    ApplicationFormDto toDto(ApplicationForm entity);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "formType", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    ApplicationForm toEntity(ApplicationFormDto dto);

    List<ApplicationFormDto> toDtoList(List<ApplicationForm> entities);

    List<ApplicationForm> toEntityList(List<ApplicationFormDto> dtos);
}
