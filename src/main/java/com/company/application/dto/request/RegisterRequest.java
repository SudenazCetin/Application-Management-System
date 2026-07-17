package com.company.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Bu sinif, register endpoint'ine gelen istek verisini tasir.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    // Kullanici adinin bos gecilmesini engellemek icin validation eklenir.
    @NotBlank(message = "Name is required")
    // Isim alaninin makul uzunlukta olmasini zorunlu kilar.
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    // Kullanici soyadinin bos gecilmesini engellemek icin validation eklenir.
    @NotBlank(message = "Surname is required")
    // Soyisim alaninin makul uzunlukta olmasini zorunlu kilar.
    @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters")
    private String surname;

    // Email formatinin dogru olmasi ve bos olmamasi icin validation eklenir.
    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    // Email uzunlugunu sinirlayarak veritabanina uygunlugu korur.
    @Size(max = 100, message = "Email must be at most 100 characters")
    private String email;

    // Sifrenin bos gecilmesini engellemek icin validation eklenir.
    @NotBlank(message = "Password is required")
    // Sifre guvenligi icin minimum uzunluk zorunlu tutulur.
    @Size(min = 8, max = 64, message = "Password must be at least 8 characters long")
    private String password;
}
