package com.company.application.service;

import com.company.application.dto.DashboardStatsDto;

// Bu arayuz, dashboard ekrani icin ozet istatistik hesaplama islemlerini tanimlar.
public interface DashboardService {

    // Toplam/bekleyen/onaylanan/reddedilen/bugunku basvuru sayilari ile son 10 basvuruyu hesaplar.
    DashboardStatsDto getStats();
}
