package com.company.application.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Bu sinif, dashboard ekraninda gosterilecek ozet istatistikleri tasir.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDto {

    // Sistemdeki toplam basvuru sayisi.
    private long totalApplications;

    // Henuz sonuclanmamis (NEW veya IN_REVIEW durumundaki) basvuru sayisi.
    private long pendingApplications;

    // Onaylanmis basvuru sayisi.
    private long approvedApplications;

    // Reddedilmis basvuru sayisi.
    private long rejectedApplications;

    // Bugun olusturulan basvuru sayisi.
    private long todayApplications;

    // En son olusturulan 10 basvuru.
    private List<ApplicationFormDto> recentApplications;
}
