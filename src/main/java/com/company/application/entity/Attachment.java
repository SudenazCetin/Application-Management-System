package com.company.application.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Bu sınıf, sisteme yüklenen ek dosyaları temsil eder.
@Entity
// Tablo adını açıkça belirtir.
@Table(name = "attachments")
// Getter metotlarını otomatik üretir.
@Getter
// Setter metotlarını otomatik üretir.
@Setter
// Parametresiz kurucu oluşturur.
@NoArgsConstructor
// Tüm alanları içeren kurucu oluşturur.
@AllArgsConstructor
public class Attachment {

    // Birincil anahtar alanıdır.
    @Id
    // Kimlik değerini otomatik üretir.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Kullanıcının yüklediği orijinal dosya adını tutar.
    private String originalName;

    // Diskte saklanan benzersiz dosya adını tutar.
    private String storedFileName;

    // Dosyanın sistemdeki yolunu tutar.
    private String filePath;

    // Dosyanın türünü tutar.
    private String fileType;

    // Dosya boyutunu byte cinsinden tutar.
    private Long fileSize;

    // Yüklenme tarih ve saatini tutar.
    private LocalDateTime uploadDate;

    // Bu ek dosyanın ait olduğu başvuru formunu belirtir.
    @ManyToOne
    // Yabancı anahtar kolonunu tanımlar.
    @JoinColumn(name = "application_form_id")
    private ApplicationForm applicationForm;
}
