package com.company.application.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.company.application.dto.ApplicationFormDto;
import com.company.application.dto.DashboardStatsDto;
import com.company.application.entity.ApplicationForm;
import com.company.application.entity.enums.ApplicationStatus;
import com.company.application.mapper.ApplicationFormMapper;
import com.company.application.repository.ApplicationFormRepository;
import com.company.application.service.DashboardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ApplicationFormRepository applicationFormRepository;
    private final ApplicationFormMapper applicationFormMapper;

    @Override
    public DashboardStatsDto getStats() {
        long total = applicationFormRepository.count();
        long pending = applicationFormRepository.countByStatusIn(List.of(ApplicationStatus.NEW, ApplicationStatus.IN_REVIEW));
        long approved = applicationFormRepository.countByStatus(ApplicationStatus.APPROVED);
        long rejected = applicationFormRepository.countByStatus(ApplicationStatus.REJECTED);

        // Bugunun basi ve sonu, "bugunku basvurular" sayisini hesaplamak icin kullanilir.
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
        long today = applicationFormRepository.countByApplicationDateBetween(startOfDay, endOfDay);

        List<ApplicationForm> recentForms = applicationFormRepository.findTop10ByOrderByApplicationDateDesc();
        List<ApplicationFormDto> recentApplications = applicationFormMapper.toDtoList(recentForms);

        return new DashboardStatsDto(total, pending, approved, rejected, today, recentApplications);
    }
}
