package com.company.application.service;

import java.util.List;

import com.company.application.dto.UserDto;
import com.company.application.dto.request.UserProfileUpdateRequest;

// Bu arayuz, kullanici is kurallarinin servis sozlesmesini tanimlar.
public interface UserService {

    List<UserDto> findAll();

    UserDto findById(Long id);

    UserDto save(UserDto dto);

    UserDto update(Long id, UserDto dto);

    // Giris yapan kullanicinin profilini token'dan gelen email bilgisiyle getirir.
    UserDto getMyProfile(String authenticatedEmail);

    // Giris yapan kullanicinin kendi profilini sadece izinli alanlarla gunceller.
    UserDto updateMyProfile(String authenticatedEmail, UserProfileUpdateRequest request);

    void delete(Long id);
}
