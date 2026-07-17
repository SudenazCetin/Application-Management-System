package com.company.application.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.company.application.entity.User;
import com.company.application.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// Bu servis, Spring Security'nin bekledigi UserDetails nesnesini veritabanindan yukler.
@Service
// Constructor tabanli dependency injection icin gerekli constructor'u Lombok uretir.
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    // Kullanici kaydina email uzerinden erismek icin repository bagimliligi.
    private final UserRepository userRepository;

    // Spring Security login sirasinda username olarak email degeri ile bu metodu cagirir.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Email'e gore kullanici bulunamazsa standart security exception firlatilir.
        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        // Security icin framework'un User nesnesi olusturulur.
        return org.springframework.security.core.userdetails.User.builder()
            // Username olarak email kullanilir.
            .username(user.getEmail())
            // Password alanina veritabanindaki hash deger verilir.
            .password(user.getPassword())
            // Kullanici rolu Spring'in bekledigi authority formatina cevrilir.
            .authorities(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
            // UserDetails nesnesi hazirlanarak security altyapisina donulur.
            .build();
    }
}
