package com.company.application.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.company.application.dto.request.LoginRequest;
import com.company.application.dto.request.RegisterRequest;
import com.company.application.dto.response.LoginResponse;
import com.company.application.dto.response.RegisterResponse;
import com.company.application.entity.User;
import com.company.application.entity.enums.Role;
import com.company.application.repository.UserRepository;
import com.company.application.security.CustomUserDetailsService;
import com.company.application.security.JwtService;
import com.company.application.service.AuthService;

import lombok.RequiredArgsConstructor;

// Bu sinif, register ve login is kurallarini uygulayan servis implementasyonudur.
@Service
// Constructor tabanli dependency injection icin gerekli constructor'u Lombok uretir.
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    // Kullanici kaydi ve sorgulama islemleri icin repository bagimliligi.
    private final UserRepository userRepository;

    // Sifre hashleme ve sifre dogrulama islemleri icin PasswordEncoder kullanilir.
    private final PasswordEncoder passwordEncoder;

    // Spring Security'nin merkezi kimlik dogrulama mekanizmasi icin kullanilir.
    private final AuthenticationManager authenticationManager;

    // JWT uretme ve dogrulama islemleri bu servis ile yonetilir.
    private final JwtService jwtService;

    // Kullanici detaylarini email uzerinden yuklemek icin kullanilir.
    private final CustomUserDetailsService customUserDetailsService;

    // Register akisini yonetir: email benzersizligi, sifre hashleme ve kayit olusturma.
    @Override
    public RegisterResponse register(RegisterRequest request) {
        // Ayni email ile daha once kayit var mi kontrol edilir.
        boolean emailAlreadyExists = userRepository.existsByEmail(request.getEmail());

        // Email kullaniliyorsa basarisiz register cevabi donulur.
        if (emailAlreadyExists) {
            return new RegisterResponse(false, "Email already exists", null, request.getEmail(), null);
        }

        // Yeni kullanici nesnesi olusturulur.
        User user = new User();
        // Request'ten gelen ad bilgisi entity'ye aktarilir.
        user.setName(request.getName());
        // Request'ten gelen soyad bilgisi entity'ye aktarilir.
        user.setSurname(request.getSurname());
        // Request'ten gelen email bilgisi entity'ye aktarilir.
        user.setEmail(request.getEmail());
        // Duz metin sifre BCrypt ile hashlenerek kaydedilir.
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        // Bu asamada varsayilan rol kurali geregi PERSONNEL atanir.
        user.setRole(Role.PERSONNEL);
        // Kayit zamani izlenebilirlik icin olusturma tarihi set edilir.
        user.setCreatedDate(LocalDateTime.now());

        // Hazirlanan kullanici veritabanina kaydedilir.
        User savedUser = userRepository.save(user);

        // Basarili kayit cevabi, gerekli ozet bilgilerle donulur.
        return new RegisterResponse(
            true,
            "User registered successfully",
            savedUser.getId(),
            savedUser.getEmail(),
            savedUser.getRole()
        );
    }

    // Login akisini yonetir: email varligi ve sifre dogrulama.
    @Override
    public LoginResponse login(LoginRequest request) {
        // Email bilgisine gore kullanici kaydi aranir.
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        // Email bulunamazsa basarisiz login cevabi donulur.
        if (optionalUser.isEmpty()) {
            return new LoginResponse(false, "Invalid email or password");
        }

        // UsernamePasswordAuthenticationToken ile authentication islemi tetiklenir.
        Authentication authentication;
        try {
            // Security provider, email ve sifreyi dogrulayarak authentication nesnesi uretir.
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (AuthenticationException ex) {
            // Hatali kimlik bilgileri durumunda kontrollu basarisiz cevap donulur.
            return new LoginResponse(false, "Invalid email or password");
        }

        // Kimlik dogrulama sonucu basarisizsa guvenli sekilde genel hata mesaji donulur.
        if (!authentication.isAuthenticated()) {
            return new LoginResponse(false, "Invalid email or password");
        }

        // Dogrulanan kullanicinin UserDetails bilgisi email ile yuklenir.
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());

        // Basarili dogrulama sonrasi kullaniciya JWT uretilir.
        String token = jwtService.generateToken(userDetails);

        // Login basarili cevabi ile birlikte uretilen token istemciye donulur.
        return new LoginResponse(true, "Login successful", token);
    }
}
