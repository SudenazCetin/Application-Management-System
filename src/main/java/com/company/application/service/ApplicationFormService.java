package com.company.application.service;

import java.util.List;

import com.company.application.dto.ApplicationFormDto;

// Bu arayuz, basvuru formu is kurallarinin servis sozlesmesini tanimlar.
public interface ApplicationFormService {

    List<ApplicationFormDto> findAll();

    ApplicationFormDto findById(Long id);

    ApplicationFormDto save(ApplicationFormDto dto);

    ApplicationFormDto update(Long id, ApplicationFormDto dto);

    // Basvuruyu onaylar (status -> APPROVED). State machine kurallarina uymayan
    // gecisler service katmaninda engellenir.
    ApplicationFormDto approve(Long id);

    // Basvuruyu reddeder (status -> REJECTED). State machine kurallarina uymayan
    // gecisler service katmaninda engellenir.
    ApplicationFormDto reject(Long id);

    void delete(Long id);
}
