package com.company.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Bu sinif, login islemi sonucunda API'nin donecegi cevabi tasir.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    // Giris denemesinin basarili olup olmadigini belirtir.
    private boolean success;

    // Giris sonucu ile ilgili aciklayici mesaji tasir.
    private String message;

    // Basarili login sonrasinda istemciye donulecek JWT degerini tasir.
    private String token;

    // Token olmayan cevaplar icin sade constructor saglar.
    public LoginResponse(boolean success, String message) {
        // Basari durumunu nesneye atar.
        this.success = success;
        // Mesaj bilgisini nesneye atar.
        this.message = message;
        // Basarisiz veya token gerekmeyen durumda token alanini bos birakir.
        this.token = null;
    }
}
