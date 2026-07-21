package com.company.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Bu sinif, kullanicinin kendi profilini guncellerken izin verilen alanlari tasir.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateRequest {

    // Isim alaninin bos gecilmesini engeller.
    @NotBlank(message = "Name is required")
    // Isim uzunlugunu makul aralikta tutar.
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    // Soyisim alaninin bos gecilmesini engeller.
    @NotBlank(message = "Surname is required")
    // Soyisim uzunlugunu makul aralikta tutar.
    @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters")
    private String surname;

    // Email alaninin bos gecilmesini engeller.
    @NotBlank(message = "Email is required")
    // Email formatinin gecerli olmasini zorunlu kilar.
    @Email(message = "Email format is invalid")
    // Email uzunlugunu veritabanina uygun sekilde sinirlar.
    @Size(max = 100, message = "Email must be at most 100 characters")
    private String email;
}