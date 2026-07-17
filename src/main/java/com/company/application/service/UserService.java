package com.company.application.service;

import java.util.List;

import com.company.application.dto.UserDto;

// Bu arayuz, kullanici is kurallarinin servis sozlesmesini tanimlar.
public interface UserService {

    List<UserDto> findAll();

    UserDto findById(Long id);

    UserDto save(UserDto dto);

    UserDto update(Long id, UserDto dto);

    void delete(Long id);
}
