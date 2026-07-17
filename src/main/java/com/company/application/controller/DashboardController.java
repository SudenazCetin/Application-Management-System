package com.company.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.application.dto.DashboardStatsDto;
import com.company.application.service.DashboardService;

import lombok.RequiredArgsConstructor;

// Bu sinif, dashboard ekraninin ihtiyac duydugu ozet istatistikleri sunan endpoint'i icerir.
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    // Toplam/bekleyen/onaylanan/reddedilen/bugunku basvuru sayilari ile son 10 basvuruyu dondurur.
    // Kimlik dogrulamasi yapilmis (ADMIN veya PERSONNEL) her kullanici erisebilir.
    @GetMapping
    public ResponseEntity<DashboardStatsDto> getStats() {
        return ResponseEntity.ok(dashboardService.getStats());
    }
}
