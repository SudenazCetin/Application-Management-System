package com.company.application.entity.enums;

public enum ApplicationStatus {
    // Basvuru sisteme yeni kaydedildi ve henuz isleme alinmadi.
    NEW,

    // Basvuru ilgili ekip tarafindan degerlendirme surecindedir.
    IN_REVIEW,

    // Basvuru degerlendirme sonucu olumlu bulunarak onaylandi.
    APPROVED,

    // Basvuru degerlendirme sonucu olumsuz bulunarak reddedildi.
    REJECTED,

    // Basvuru, surec tamamlanmadan basvuru sahibi veya sistem tarafindan iptal edildi.
    CANCELLED
}
