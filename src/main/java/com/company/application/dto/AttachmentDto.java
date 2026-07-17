package com.company.application.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Bu sinif, ek dosya verilerini tasimak icin kullanilir.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentDto {

    private Long id;

    // Dosya adinin bos gecilmesini engeller.
    @NotBlank(message = "Original name is required")
    // Orijinal dosya adi uzunlugunu sinirlar.
    @Size(min = 1, max = 255, message = "Original name must be between 1 and 255 characters")
    private String originalName;

    // Dosyanin diskte saklanan benzersiz adinin bos gecilmesini engeller.
    @NotBlank(message = "Stored file name is required")
    // Diskteki dosya adi uzunlugunu sinirlar.
    @Size(min = 1, max = 255, message = "Stored file name must be between 1 and 255 characters")
    private String storedFileName;

    // Dosya yolunun bos gecilmesini engeller.
    @NotBlank(message = "File path is required")
    // Dosya yolu uzunlugunu sinirlar.
    @Size(min = 1, max = 500, message = "File path must be between 1 and 500 characters")
    private String filePath;

    // Dosya turunun bos gecilmesini engeller.
    @NotBlank(message = "File type is required")
    // Dosya turu alaninin uzunlugunu sinirlar.
    @Size(min = 2, max = 100, message = "File type must be between 2 and 100 characters")
    private String fileType;

    // Dosya boyutunun bos gecilmesini engeller.
    @NotNull(message = "File size is required")
    // Dosya boyutunun pozitif olmasini zorunlu kilar.
    @Positive(message = "File size must be greater than 0")
    private Long fileSize;

    // Yuklenme tarihinin bos gecilmesini engeller.
    @NotNull(message = "Upload date is required")
    private LocalDateTime uploadDate;

    // Ek dosyanin bir basvuru formuna bagli olmasini zorunlu kilar.
    @NotNull(message = "Application form id is required")
    // Basvuru formu id degerinin pozitif olmasini zorunlu kilar.
    @Positive(message = "Application form id must be greater than 0")
    private Long applicationFormId;
}
