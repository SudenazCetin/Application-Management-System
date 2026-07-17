package com.company.application.dto.response;

import com.company.application.entity.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Bu sinif, register islemi sonucunda API'nin donecegi cevabi tasir.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {

    // Islemin basarili olup olmadigini belirtir.
    private boolean success;

    // API istemcisine anlamli durum mesaji verir.
    private String message;

    // Kaydedilen kullanicinin id bilgisini tasir.
    private Long userId;

    // Kaydedilen kullanicinin email bilgisini tasir.
    private String email;

    // Kaydedilen kullaniciya atanan rol bilgisini tasir.
    private Role role;
}
