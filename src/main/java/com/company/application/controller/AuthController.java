package com.company.application.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.application.dto.request.LoginRequest;
import com.company.application.dto.request.RegisterRequest;
import com.company.application.dto.response.LoginResponse;
import com.company.application.dto.response.RegisterResponse;
import com.company.application.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// Bu sinif, kimlik dogrulama endpoint'lerini karsilar ve service katmanina yonlendirir.
@RestController
// Tum auth endpoint'leri tek bir kaynak adi altinda toplanir: /api/auth
@RequestMapping("/api/auth")
// Constructor tabanli dependency injection icin gerekli constructor'u Lombok uretir.
@RequiredArgsConstructor
public class AuthController {

    // Register ve login is kurallari service katmanina delege edilir.
    private final AuthService authService;

    // Yeni kullanici kaydi olusturmak icin POST /api/auth/register endpoint'i kullanilir.
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        // Register is akisi service katmaninda calistirilir.
        RegisterResponse response = authService.register(request);

        // Islem basariliysa 201 Created donulur.
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        // Islem basarisizsa 400 Bad Request donulur.
        return ResponseEntity.badRequest().body(response);
    }

    // Kullanici girisini dogrulamak ve basariliysa JWT dondurmek icin POST /api/auth/login endpoint'i kullanilir.
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        // Login is akisi service katmaninda calistirilir.
        LoginResponse response = authService.login(request);

        // Giris basariliysa 200 OK donulur.
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }

        // Giris basarisizsa 401 Unauthorized donulur.
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
