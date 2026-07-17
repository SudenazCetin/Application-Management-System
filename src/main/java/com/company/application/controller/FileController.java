package com.company.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.company.application.dto.response.AttachmentResponse;
import com.company.application.service.AttachmentService;

import lombok.RequiredArgsConstructor;

// Bu controller, dosya yukleme akisina ozel endpoint'leri barindirir.
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    // Is kurallari service katmaninda kalmasi icin controller sadece delege eder.
    private final AttachmentService attachmentService;

    // Gercek dosya yukleme endpoint'i: POST /api/files/upload
    // Sadece giris yapmis kullanicilar dosya yukleyebilir.
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/upload")
    public ResponseEntity<AttachmentResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        // Multipart dosya, validation ve kayit sureci icin service katmanina aktarilir.
        AttachmentResponse response = attachmentService.upload(file);
        // Basarili yukleme icin 200 OK ve metadata cevabi donulur.
        return ResponseEntity.ok(response);
    }
}