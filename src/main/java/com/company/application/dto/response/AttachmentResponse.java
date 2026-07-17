package com.company.application.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Bu sinif, basarili dosya yukleme sonrasinda istemciye dondurulecek cevabi tasir.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentResponse {

    // Veritabaninda olusan attachment kaydinin kimligidir.
    private Long id;

    // Kullanici tarafindan gonderilen orijinal dosya adidir.
    private String originalName;

    // Diskte saklanan benzersiz dosya adidir.
    private String storedFileName;

    // Dosyanin tur bilgisidir.
    private String fileType;

    // Dosya boyutu byte cinsinden tutulur.
    private Long fileSize;

    // Dosyanin sunucudaki goreli/lojik yoludur.
    private String filePath;

    // Dosyanin yuklendigi tarih-saat bilgisidir.
    private LocalDateTime uploadDate;
}