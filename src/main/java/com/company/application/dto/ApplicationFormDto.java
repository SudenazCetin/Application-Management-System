package com.company.application.dto;

import java.time.LocalDateTime;

import com.company.application.entity.enums.ApplicationStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Bu sinif, basvuru formu verilerini tasimak icin kullanilir.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationFormDto {

    private Long id;

    // Baslik alaninin bos gecilmesini engeller.
    @NotBlank(message = "Title is required")
    // Baslik uzunlugunu makul aralikta tutar.
    @Size(min = 3, max = 150, message = "Title must be between 3 and 150 characters")
    private String title;

    // Aciklama alaninin bos gecilmesini engeller.
    @NotBlank(message = "Description is required")
    // Aciklama uzunlugunu yonetilebilir aralikta tutar.
    @Size(min = 5, max = 2000, message = "Description must be between 5 and 2000 characters")
    private String description;

    // Basvuru tarihinin bos gecilmesini engeller.
    @NotNull(message = "Application date is required")
    private LocalDateTime applicationDate;

    // Basvuru durumunun bos olmasini engeller.
    @NotNull(message = "Application status is required")
    private ApplicationStatus status;

    // Basvurunun bir kullaniciya bagli olmasini zorunlu kilar.
    @NotNull(message = "User id is required")
    // Kullanici id degerinin pozitif olmasini zorunlu kilar.
    @Positive(message = "User id must be greater than 0")
    private Long userId;

    // Basvurunun bir form turune bagli olmasini zorunlu kilar.
    @NotNull(message = "Form type id is required")
    // Form turu id degerinin pozitif olmasini zorunlu kilar.
    @Positive(message = "Form type id must be greater than 0")
    private Long formTypeId;
}
