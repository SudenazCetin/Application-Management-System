package com.company.application.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.company.application.entity.ApplicationForm;
import com.company.application.entity.enums.ApplicationStatus;

// Bu arayuz, ApplicationForm entity'si icin veritabani islemlerini saglar.
public interface ApplicationFormRepository extends JpaRepository<ApplicationForm, Long>, JpaSpecificationExecutor<ApplicationForm> {

    // Detay ekrani icin gerekli iliskileri tek sorguda cekerek lazy-loading/N+1 riskini azaltir.
    @Query("select distinct af from ApplicationForm af " +
           "left join fetch af.user u " +
           "left join fetch af.formType ft " +
           "left join fetch af.attachments att " +
           "where af.id = :id")
    Optional<ApplicationForm> findDetailById(@Param("id") Long id);

    // Dashboard istatistikleri icin belirli bir duruma sahip basvuru sayisini dondurur.
    long countByStatus(ApplicationStatus status);

    // Dashboard istatistikleri icin birden fazla durumdan herhangi birine sahip basvuru sayisini dondurur.
    long countByStatusIn(Collection<ApplicationStatus> statuses);

    // Dashboard istatistikleri icin belirli bir tarih araligindaki basvuru sayisini dondurur (bugunku basvurular).
    long countByApplicationDateBetween(LocalDateTime start, LocalDateTime end);

    // Dashboard icin en son olusturulan 10 basvuruyu tarihe gore azalan sirada dondurur.
    List<ApplicationForm> findTop10ByOrderByApplicationDateDesc();
}

