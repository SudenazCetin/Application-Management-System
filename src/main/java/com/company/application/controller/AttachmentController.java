package com.company.application.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.application.dto.AttachmentDto;
import com.company.application.service.AttachmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// Bu sinif, Attachment kaynagi icin HTTP isteklerini karsilar ve service katmanina yonlendirir.
@RestController
// Tum endpoint'leri tek bir kaynak adi altinda toplar: /api/attachments
@RequestMapping("/api/attachments")
// Constructor tabanli dependency injection icin gerekli constructor'u Lombok uretir.
@RequiredArgsConstructor
public class AttachmentController {

    // Is kurallari controller'da yazilmaz; service katmanina delege edilir.
    private final AttachmentService attachmentService;

    // Tum ek dosyalari listelemek icin GET /api/attachments endpoint'i kullanilir.
    // Bu endpoint'e giris yapmis tum kullanicilar erisebilir.
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<AttachmentDto>> getAllAttachments() {
        // Veri servis katmanindan alinir.
        List<AttachmentDto> attachments = attachmentService.findAll();
        // Basarili listeleme icin 200 OK donulur.
        return ResponseEntity.ok(attachments);
    }

    // Tek bir ek dosyayi id ile getirmek icin GET /api/attachments/{id} endpoint'i kullanilir.
    @GetMapping("/{id}")
    public ResponseEntity<AttachmentDto> getAttachmentById(@PathVariable Long id) {
        // Path degiskenindeki id ile kayit aranir.
        AttachmentDto attachment = attachmentService.findById(id);
        // Kayit bulunduysa 200 OK ile dto donulur.
        return ResponseEntity.ok(attachment);
    }

    // Yeni ek dosya olusturmak icin POST /api/attachments endpoint'i kullanilir.
    @PostMapping
    public ResponseEntity<AttachmentDto> createAttachment(@Valid @RequestBody AttachmentDto attachmentDto) {
        // Gelen istek govdesi servis katmanina aktarilarak kayit olusturulur.
        AttachmentDto createdAttachment = attachmentService.save(attachmentDto);
        // REST standardi geregi olusan kaynagin adresi Location header'a yazilir.
        URI location = URI.create("/api/attachments/" + createdAttachment.getId());
        // Basarili olusturma icin 201 Created + body donulur.
        return ResponseEntity.created(location).body(createdAttachment);
    }

    // Var olan ek dosyayi tam guncellemek icin PUT /api/attachments/{id} endpoint'i kullanilir.
    @PutMapping("/{id}")
    public ResponseEntity<AttachmentDto> updateAttachment(@PathVariable Long id, @Valid @RequestBody AttachmentDto attachmentDto) {
        // Hedef kayit id ile bulunur ve yeni veriyle guncellenir.
        AttachmentDto updatedAttachment = attachmentService.update(id, attachmentDto);
        // Basarili guncelleme icin 200 OK ile guncel veri donulur.
        return ResponseEntity.ok(updatedAttachment);
    }

    // Kayit silmek icin DELETE /api/attachments/{id} endpoint'i kullanilir.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long id) {
        // Silme islemi servis katmanina delege edilir.
        attachmentService.delete(id);
        // Basarili silme icin response body'siz 204 No Content donulur.
        return ResponseEntity.noContent().build();
    }
}
