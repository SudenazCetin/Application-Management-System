package com.company.application.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

// Bu servis, JWT olusturma ve token dogrulama islemlerini tek noktadan yonetir.
@Service
public class JwtService {

    // Token imzalama anahtari dis konfigrasyondan okunur.
    @Value("${jwt.secret}")
    private String jwtSecret;

    // Token gecerlilik suresi (milisaniye) dis konfigrasyondan okunur.
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // Giris yapan kullanici icin signed JWT token uretir.
    public String generateToken(UserDetails userDetails) {
        // Token'in hangi kullaniciya ait oldugunu belirtmek icin subject alanina email yazilir.
        return Jwts.builder()
            .subject(userDetails.getUsername())
            // Token olusturulma zamani kaydedilir.
            .issuedAt(new Date(System.currentTimeMillis()))
            // Token'in ne zaman gecersiz olacagi belirlenir.
            .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
            // Token, secret key ile imzalanarak guvenli hale getirilir.
            .signWith(getSigningKey())
            // JWT string formunda uretilir.
            .compact();
    }

    // Token icinden kullanici email bilgisini (subject) alir.
    public String extractUsername(String token) {
        // Subject claim'i username/email olarak kullanildigi icin direkt bu claim cekilir.
        return extractClaim(token, Claims::getSubject);
    }

    // Token gecerliligini kullanici bilgisi ile birlikte dogrular.
    public boolean isTokenValid(String token, UserDetails userDetails) {
        // Token'dan username cikarilir.
        String username = extractUsername(token);
        // Username eslesmesi ve expiration kontrolu birlikte yapilir.
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Token suresi dolmus mu kontrol eder.
    public boolean isTokenExpired(String token) {
        // Expiration tarihi, simdiki zamandan onceyse token gecersiz kabul edilir.
        return extractExpiration(token).before(new Date());
    }

    // Token icindeki expiration claim degerini dondurur.
    private Date extractExpiration(String token) {
        // Claims uzerinden expiration alani okunur.
        return extractClaim(token, Claims::getExpiration);
    }

    // Generic claim okuma metodu ile kod tekrarini azaltir.
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        // Tum claim'ler imza dogrulamasi yapilarak parse edilir.
        Claims claims = extractAllClaims(token);
        // Istek yapan fonksiyon ile ilgili claim degeri dondurulur.
        return claimsResolver.apply(claims);
    }

    // Token'daki tum claim'leri imzayi dogrulayarak parse eder.
    private Claims extractAllClaims(String token) {
        // Parser, signing key ile kurulur ve imza dogrulamasi aktif edilir.
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            // Token parse edilerek payload (claims) okunur.
            .parseSignedClaims(token)
            .getPayload();
    }

    // JWT imza islemi icin SecretKey nesnesini olusturur.
    private SecretKey getSigningKey() {
        // Secret string byte dizisine cevrilerek HMAC-SHA key uretilir.
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}
