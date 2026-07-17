package com.company.application.service;

import com.company.application.dto.request.LoginRequest;
import com.company.application.dto.request.RegisterRequest;
import com.company.application.dto.response.LoginResponse;
import com.company.application.dto.response.RegisterResponse;

// Bu arayuz, kimlik dogrulama islemleri icin servis sozlesmesini tanimlar.
public interface AuthService {

    // Yeni kullanici kaydi olusturma is akisini yonetir.
    RegisterResponse register(RegisterRequest request);

    // Kullanici girisi dogrulama is akisini yonetir.
    LoginResponse login(LoginRequest request);
}
