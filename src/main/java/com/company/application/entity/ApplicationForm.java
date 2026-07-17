package com.company.application.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.company.application.entity.enums.ApplicationStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Bu sınıf, başvuru formlarını veritabanında temsil eden entity'dir.
@Entity
// Tablo adını açıkça belirler.
@Table(name = "application_forms")
// Getter metodlarını otomatik üretir.
@Getter
// Setter metodlarını otomatik üretir.
@Setter
// Parametresiz kurucu oluşturur.
@NoArgsConstructor
// Tüm alanları içeren kurucu oluşturur.
@AllArgsConstructor
public class ApplicationForm {

    // Birincil anahtar alanıdır.
    @Id
    // Kimlik değerini otomatik üretir.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Başvurunun başlığını tutar.
    private String title;

    // Başvurunun açıklamasını tutar.
    private String description;

    // Başvuru tarihini ve saatini tutar.
    private LocalDateTime applicationDate;

    // Durum alanını enum olarak metin biçiminde saklar.
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    // Başvuruya ait kullanıcıyı temsil eder.
    @ManyToOne
    // Kullanıcı yabancı anahtar kolonunu belirtir.
    @JoinColumn(name = "user_id")
    private User user;

    // Başvuruya ait form türünü temsil eder.
    @ManyToOne
    // Form türü yabancı anahtar kolonunu belirtir.
    @JoinColumn(name = "form_type_id")
    private FormType formType;

    // Başvuru formuna ait ek dosyaları temsil eder.
    @OneToMany(
        mappedBy = "applicationForm",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Attachment> attachments;
}
