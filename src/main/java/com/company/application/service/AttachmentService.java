package com.company.application.service;

import java.util.List;

import com.company.application.dto.AttachmentDto;
import com.company.application.dto.response.AttachmentResponse;

import org.springframework.web.multipart.MultipartFile;

// Bu arayuz, ek dosya is kurallarinin servis sozlesmesini tanimlar.
public interface AttachmentService {

    List<AttachmentDto> findAll();

    AttachmentDto findById(Long id);

    AttachmentDto save(AttachmentDto dto);

    // Gercek dosya yukleme akisini calistirir, diske yazar ve metadata bilgisini veritabanina kaydeder.
    AttachmentResponse upload(MultipartFile file);

    AttachmentDto update(Long id, AttachmentDto dto);

    void delete(Long id);
}
