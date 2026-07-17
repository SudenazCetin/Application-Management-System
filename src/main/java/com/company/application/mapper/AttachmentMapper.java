package com.company.application.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.company.application.dto.AttachmentDto;
import com.company.application.entity.Attachment;

// Bu arayuz, Attachment ile AttachmentDto arasinda donusum saglar.
@Mapper
public interface AttachmentMapper {

    @Mapping(source = "applicationForm.id", target = "applicationFormId")
    AttachmentDto toDto(Attachment entity);

    @Mapping(target = "applicationForm", ignore = true)
    Attachment toEntity(AttachmentDto dto);

    List<AttachmentDto> toDtoList(List<Attachment> entities);

    List<Attachment> toEntityList(List<AttachmentDto> dtos);
}
