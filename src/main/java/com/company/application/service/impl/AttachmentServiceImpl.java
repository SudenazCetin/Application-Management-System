package com.company.application.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.company.application.dto.AttachmentDto;
import com.company.application.dto.response.AttachmentResponse;
import com.company.application.entity.Attachment;
import com.company.application.mapper.AttachmentMapper;
import com.company.application.repository.AttachmentRepository;
import com.company.application.service.AttachmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    // Uygulama gereksinimine gore izin verilen dosya uzantilari tek noktadan yonetilir.
    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(
        Arrays.asList("pdf", "docx", "png", "jpg", "jpeg"));

    // Is gereksinimine gore maksimum dosya boyutu 10 MB ile sinirlandirilir.
    private static final long MAX_FILE_SIZE = 10L * 1024L * 1024L;

    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper attachmentMapper;

    // Disk kaydi icin uploads klasorunun kok yolu konfigurasyondan okunur.
    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    // Tum ek dosyalari getirir ve DTO listesine donusturur.
    @Override
    public List<AttachmentDto> findAll() {
        List<Attachment> attachments = attachmentRepository.findAll();
        return attachmentMapper.toDtoList(attachments);
    }

    // Id ile ek dosyayi bulur ve DTO olarak dondurur.
    @Override
    public AttachmentDto findById(Long id) {
        Attachment attachment = attachmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Attachment not found with id: " + id));
        return attachmentMapper.toDto(attachment);
    }

    // Yeni ek dosyayi kaydeder ve DTO olarak dondurur.
    @Override
    public AttachmentDto save(AttachmentDto dto) {
        Attachment attachment = attachmentMapper.toEntity(dto);
        Attachment savedAttachment = attachmentRepository.save(attachment);
        return attachmentMapper.toDto(savedAttachment);
    }

    // Multipart dosyayi dogrular, diske kaydeder ve metadata bilgisini veritabanina yazar.
    @Override
    public AttachmentResponse upload(MultipartFile file) {
        // Bos dosya yukleme denemesini erken asamada reddeder.
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Yuklenecek dosya bos olamaz");
        }

        // Boyut kontrolu service katmaninda da uygulanarak kural merkezi hale getirilir.
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Dosya boyutu 10 MB sinirini asamaz");
        }

        // Orijinal ad temizlenerek path traversal gibi riskler azaltilir.
        String originalName = StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "");
        String extension = extractExtension(originalName);

        // Sadece dokumanda belirtilen uzantilara izin verilir.
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Sadece PDF, DOCX, PNG ve JPG dosyalari yuklenebilir");
        }

        // UUID ile benzersiz dosya adi uretilerek isim cakismasi onlenir.
        String storedFileName = UUID.randomUUID() + "_" + originalName;

        // Dosya sistemi operasyonlari icin uploads klasoru olusturulur.
        Path uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path targetFilePath = uploadRoot.resolve(storedFileName).normalize();

        try {
            Files.createDirectories(uploadRoot);
            Files.copy(file.getInputStream(), targetFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            // I/O hatasi tek tip RuntimeException ile GlobalExceptionHandler'a birakilir.
            throw new RuntimeException("Dosya diske kaydedilemedi", ex);
        }

        Attachment attachment = new Attachment();
        attachment.setOriginalName(originalName);
        attachment.setStoredFileName(storedFileName);
        attachment.setFileType(extension);
        attachment.setFileSize(file.getSize());
        // Veritabaninda goreli yol saklanarak ortamdan bagimsizlik korunur.
        attachment.setFilePath(uploadDir + "/" + storedFileName);
        attachment.setUploadDate(LocalDateTime.now());

        Attachment savedAttachment = attachmentRepository.save(attachment);
        return toUploadResponse(savedAttachment);
    }

    // Mevcut ek dosyayi gunceller ve DTO olarak dondurur.
    @Override
    public AttachmentDto update(Long id, AttachmentDto dto) {
        Attachment attachment = attachmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Attachment not found with id: " + id));

        attachment.setOriginalName(dto.getOriginalName());
        attachment.setStoredFileName(dto.getStoredFileName());
        attachment.setFilePath(dto.getFilePath());
        attachment.setFileType(dto.getFileType());
        attachment.setFileSize(dto.getFileSize());
        attachment.setUploadDate(dto.getUploadDate());

        Attachment updatedAttachment = attachmentRepository.save(attachment);
        return attachmentMapper.toDto(updatedAttachment);
    }

    // Id ile ek dosyayi bulur ve veritabanindan siler.
    @Override
    public void delete(Long id) {
        Attachment attachment = attachmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Attachment not found with id: " + id));
        attachmentRepository.delete(attachment);
    }

    // Dosya adindan uzanti bilgisini guvenli bicimde cikarir.
    private String extractExtension(String originalName) {
        int lastDotIndex = originalName.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == originalName.length() - 1) {
            throw new IllegalArgumentException("Dosya uzantisi bulunamadi");
        }
        return originalName.substring(lastDotIndex + 1).toLowerCase();
    }

    // Entity nesnesini upload cevabi icin sade bir DTO'ya donusturur.
    private AttachmentResponse toUploadResponse(Attachment attachment) {
        return new AttachmentResponse(
            attachment.getId(),
            attachment.getOriginalName(),
            attachment.getStoredFileName(),
            attachment.getFileType(),
            attachment.getFileSize(),
            attachment.getFilePath(),
            attachment.getUploadDate());
    }
}
