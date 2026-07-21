package com.company.application.service;

import java.util.List;

import com.company.application.dto.ApplicationFormDto;
import com.company.application.dto.response.ApplicationFormDetailResponse;
import com.company.application.entity.enums.ApplicationStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// Bu arayuz, basvuru formu is kurallarinin servis sozlesmesini tanimlar.
public interface ApplicationFormService {

    List<ApplicationFormDto> findAll();

    // Filtreleme, siralama ve sayfalama destekli listeleme sozlesmesini tanimlar.
    Page<ApplicationFormDto> findAll(ApplicationStatus status,
                                     Long formTypeId,
                                     Long applicantId,
                                     String keyword,
                                     java.time.LocalDateTime createdDateStart,
                                     java.time.LocalDateTime createdDateEnd,
                                     Pageable pageable,
                                     boolean isAdmin);

    ApplicationFormDto findById(Long id);

    // Detay ekrani icin tek basvurunun tum iliski verileriyle dondurulmasini saglar.
    ApplicationFormDetailResponse findDetailById(Long id, String authenticatedEmail, boolean isAdmin);

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
