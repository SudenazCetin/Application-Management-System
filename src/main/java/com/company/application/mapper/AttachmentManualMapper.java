package com.company.application.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.company.application.dto.AttachmentDto;
import com.company.application.entity.Attachment;

// MapStruct annotation processing devre disi olsa bile mapper bean'ini saglar.
@Component
@Primary
public class AttachmentManualMapper implements AttachmentMapper {

    @Override
    public AttachmentDto toDto(Attachment entity) {
        if (entity == null) {
            return null;
        }

        AttachmentDto dto = new AttachmentDto();
        dto.setId(entity.getId());
        dto.setOriginalName(entity.getOriginalName());
        dto.setStoredFileName(entity.getStoredFileName());
        dto.setFilePath(entity.getFilePath());
        dto.setFileType(entity.getFileType());
        dto.setFileSize(entity.getFileSize());
        dto.setUploadDate(entity.getUploadDate());

        if (entity.getApplicationForm() != null) {
            dto.setApplicationFormId(entity.getApplicationForm().getId());
        }

        return dto;
    }

    @Override
    public Attachment toEntity(AttachmentDto dto) {
        if (dto == null) {
            return null;
        }

        Attachment entity = new Attachment();
        entity.setId(dto.getId());
        entity.setOriginalName(dto.getOriginalName());
        entity.setStoredFileName(dto.getStoredFileName());
        entity.setFilePath(dto.getFilePath());
        entity.setFileType(dto.getFileType());
        entity.setFileSize(dto.getFileSize());
        entity.setUploadDate(dto.getUploadDate());
        return entity;
    }

    @Override
    public List<AttachmentDto> toDtoList(List<Attachment> entities) {
        if (entities == null) {
            return null;
        }

        List<AttachmentDto> result = new ArrayList<>(entities.size());
        for (Attachment entity : entities) {
            result.add(toDto(entity));
        }
        return result;
    }

    @Override
    public List<Attachment> toEntityList(List<AttachmentDto> dtos) {
        if (dtos == null) {
            return null;
        }

        List<Attachment> result = new ArrayList<>(dtos.size());
        for (AttachmentDto dto : dtos) {
            result.add(toEntity(dto));
        }
        return result;
    }
}
