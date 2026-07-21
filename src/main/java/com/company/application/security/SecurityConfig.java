package com.company.application.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

// Bu sinif, uygulamanin temel security ayarlarini merkezden yonetir.
@Configuration
// @PreAuthorize gibi method-level security annotation'larini aktif eder.
@EnableMethodSecurity
// Constructor tabanli dependency injection icin gerekli constructor'u Lombok uretir.
@RequiredArgsConstructor
public class SecurityConfig {

    // JWT token kontrolunu yapan filter bagimliligi.
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Kullanici detaylarini veritabanindan yukleyen servis bagimliligi.
    private final CustomUserDetailsService customUserDetailsService;

    // Sifre hashleme/dogrulama icin uygulama genelinde kullanilacak PasswordEncoder bean'i tanimlanir.
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt algoritmasi sifre guvenligi icin standart yaklasimdir.
        return new BCryptPasswordEncoder();
    }

    // DaoAuthenticationProvider ile UserDetailsService + PasswordEncoder eslestirilir.
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Spring Security'nin veri tabani tabanli provider nesnesi olusturulur.
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // Kullanici yukleme mantigi provider'a verilir.
        provider.setUserDetailsService(customUserDetailsService);
        // Sifre dogrulamada kullanilacak encoder provider'a verilir.
        provider.setPasswordEncoder(passwordEncoder());
        // Konfigure provider Spring'e bean olarak donulur.
        return provider;
    }

    // AuthenticationManager bean'i, AuthService tarafinda login dogrulamasinda kullanilir.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        // Spring'in uretecegi varsayilan AuthenticationManager instance'i donulur.
        return configuration.getAuthenticationManager();
    }

    // Bu zincir, hangi endpoint'in kimlik dogrulama isteyecegini belirler.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // API tabanli yapida CSRF korumasi token tabanli akis icin devre disi birakilir.
        http.csrf(csrf -> csrf.disable())
            // Frontend istemciden gelen cross-origin isteklerin CORS kurallarina gore islenmesini saglar.
            .cors(Customizer.withDefaults())
            // Session stateless yapilarak her istekte token dogrulamasi zorunlu hale getirilir.
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Auth endpoint'leri herkese acik, diger tum endpoint'ler kimlik dogrulama ister.
            .authorizeHttpRequests(auth -> auth
                // Tarayicinin preflight OPTIONS istekleri security tarafindan bloklanmamalidir.
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                // Kullanicinin kendi profil endpoint'i giris yapmis tum kullanicilara aciktir.
                .requestMatchers("/api/users/me").authenticated()
                // User endpoint'lerine sadece ADMIN rolundeki kullanicilar erisebilir.
                .requestMatchers("/api/users/**").hasRole("ADMIN")
                // FormType endpoint'lerine sadece ADMIN rolundeki kullanicilar erisebilir.
                .requestMatchers("/api/form-types/**").hasRole("ADMIN")
                // Form endpoint'lerine ADMIN ve PERSONNEL rolleri birlikte erisebilir.
                .requestMatchers("/api/forms/**").hasAnyRole("ADMIN", "PERSONNEL")
                // Attachment endpoint'lerine kimlik dogrulamasi olan tum kullanicilar erisebilir.
                .requestMatchers("/api/attachments/**").authenticated()
                // File upload endpoint'lerine kimlik dogrulamasi olan tum kullanicilar erisebilir.
                .requestMatchers("/api/files/**").authenticated()
                // Diger tum endpoint'ler icin varsayilan olarak kimlik dogrulamasi zorunludur.
                .anyRequest().authenticated()
            )
            // Security zincirine custom authentication provider eklenir.
            .authenticationProvider(authenticationProvider())
            // JWT filter'i, username/password filter'inden once calistirilir.
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Konfige edilen security zinciri Spring'e donulur.
        return http.build();
    }

    // Frontend uygulamasinin preflight ve API istekleri icin CORS politikasi tanimlanir.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Vite frontend'in calistigi 5174 ve olasi alternatif localhost/127.0.0.1 origin'leri izinli olur.
        configuration.setAllowedOrigins(List.of(
            "http://localhost:5173",
            "http://127.0.0.1:5173",
            "http://localhost:5174",
            "http://127.0.0.1:5174"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
