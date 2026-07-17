package com.company.application.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.application.entity.ApplicationForm;
import com.company.application.entity.enums.ApplicationStatus;

// Bu arayuz, ApplicationForm entity'si icin veritabani islemlerini saglar.
public interface ApplicationFormRepository extends JpaRepository<ApplicationForm, Long> {

    // Dashboard istatistikleri icin belirli bir duruma sahip basvuru sayisini dondurur.
    long countByStatus(ApplicationStatus status);

    // Dashboard istatistikleri icin birden fazla durumdan herhangi birine sahip basvuru sayisini dondurur.
    long countByStatusIn(Collection<ApplicationStatus> statuses);

    // Dashboard istatistikleri icin belirli bir tarih araligindaki basvuru sayisini dondurur (bugunku basvurular).
    long countByApplicationDateBetween(LocalDateTime start, LocalDateTime end);

    // Dashboard icin en son olusturulan 10 basvuruyu tarihe gore azalan sirada dondurur.
    List<ApplicationForm> findTop10ByOrderByApplicationDateDesc();
}

