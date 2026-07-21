package com.company.application.security;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

// Bu filter, her istekte JWT token'i okuyup SecurityContext'i doldurur.
@Component
// Constructor tabanli dependency injection icin gerekli constructor'u Lombok uretir.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Token uzerindeki claim okuma ve dogrulama islemleri icin servis bagimliligi.
    private final JwtService jwtService;

    // Token'daki username/email ile kullanici detaylarini yuklemek icin servis bagimliligi.
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/auth/") || "OPTIONS".equalsIgnoreCase(request.getMethod());
    }

    // Her HTTP isteginde bir kez cagrilan ana filter metodu.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        // Authorization header degeri istekten okunur.
        String authHeader = request.getHeader("Authorization");

        // Header yoksa veya Bearer ile baslamiyorsa zincirde bir sonraki filter'a gecilir.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Header'daki Bearer on eki atilarak token degeri elde edilir.
        String jwt = authHeader.substring(7);
        String userEmail;

        try {
            // Token icinden username/email bilgisi cikarilir.
            userEmail = jwtService.extractUsername(jwt);
        } catch (JwtException | IllegalArgumentException ex) {
            // Gecersiz token, public endpoint'leri bloklamamasi icin yoksayilir.
            filterChain.doFilter(request, response);
            return;
        }

        // Email bulunduysa ve context'te hali hazirda authentication yoksa dogrulama yapilir.
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Token'dan elde edilen email ile kullanici detaylari veritabanindan yuklenir.
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);

            // Token kullaniciya ait ve suresi dolmamis ise context'e authentication yazilir.
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Security context icin username-password token nesnesi olusturulur.
                org.springframework.security.authentication.UsernamePasswordAuthenticationToken authToken = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );

                // Request detaylari authentication nesnesine baglanir.
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Olusturulan authentication context'e yerlestirilir.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Islem sonunda request zinciri devam ettirilir.
        filterChain.doFilter(request, response);
    }
}
