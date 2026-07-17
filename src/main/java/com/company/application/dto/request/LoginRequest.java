package com.company.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Bu sinif, login endpoint'ine gelen istek verisini tasir.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    // Email alaninin bos olmasini engellemek icin validation eklenir.
    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    // Email uzunlugunu sinirlayarak tutarli veri girisini destekler.
    @Size(max = 100, message = "Email must be at most 100 characters")
    private String email;

    // Sifre alaninin bos olmasini engellemek icin validation eklenir.
    @NotBlank(message = "Password is required")
    // Login isteginde sifre uzunlugunun makul aralikta olmasini saglar.
    @Size(min = 6, max = 64, message = "Password must be between 6 and 64 characters")
    private String password;
}
