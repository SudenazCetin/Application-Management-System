package com.company.application.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.application.dto.UserDto;
import com.company.application.dto.request.UserProfileUpdateRequest;
import com.company.application.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// Bu sinif, User kaynagi icin HTTP isteklerini karsilar ve service katmanina yonlendirir.
@RestController
// Tum endpoint'leri tek bir kaynak adi altinda toplar: /api/users
@RequestMapping("/api/users")
// Constructor tabanli dependency injection icin gerekli constructor'u Lombok uretir.
@RequiredArgsConstructor
public class UserController {

    // Is kurallari controller'da yazilmaz; service katmanina delege edilir.
    private final UserService userService;

    // Tum kullanicilari listelemek icin GET /api/users endpoint'i kullanilir.
    // Bu endpoint'e sadece ADMIN rolundeki kullanicilar erisebilir.
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        // Veri servis katmanindan alinir.
        List<UserDto> users = userService.findAll();
        // Basarili listeleme icin 200 OK donulur.
        return ResponseEntity.ok(users);
    }

    // Giris yapan kullanicinin kendi profilini getirmek icin GET /api/users/me endpoint'i kullanilir.
    // Bu endpoint'e giris yapmis tum kullanicilar erisebilir.
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<UserDto> getMyProfile(Authentication authentication) {
        // SecurityContext'teki authenticated principal'dan email bilgisi alinir.
        String authenticatedEmail = authentication.getName();
        // Is kurali service katmaninda calisir; controller sadece yonlendirir.
        UserDto user = userService.getMyProfile(authenticatedEmail);
        // Basarili getirme icin 200 OK ile dto donulur.
        return ResponseEntity.ok(user);
    }

    // Giris yapan kullanicinin kendi profilini guncellemek icin PUT /api/users/me endpoint'i kullanilir.
    // Bu endpoint'e giris yapmis tum kullanicilar erisebilir.
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/me")
    public ResponseEntity<UserDto> updateMyProfile(Authentication authentication,
                                                   @Valid @RequestBody UserProfileUpdateRequest request) {
        // SecurityContext'teki authenticated principal'dan email bilgisi alinir.
        String authenticatedEmail = authentication.getName();
        // Is kurali service katmaninda calisir; controller sadece yonlendirir.
        UserDto updatedUser = userService.updateMyProfile(authenticatedEmail, request);
        // Basarili guncelleme icin 200 OK ile guncel dto donulur.
        return ResponseEntity.ok(updatedUser);
    }

    // Tek bir kullaniciyi id ile getirmek icin GET /api/users/{id} endpoint'i kullanilir.
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        // Path degiskenindeki id ile kayit aranir.
        UserDto user = userService.findById(id);
        // Kayit bulunduysa 200 OK ile dto donulur.
        return ResponseEntity.ok(user);
    }

    // Yeni kullanici olusturmak icin POST /api/users endpoint'i kullanilir.
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        // Gelen istek govdesi servis katmanina aktarilarak kayit olusturulur.
        UserDto createdUser = userService.save(userDto);
        // REST standardi geregi olusan kaynagin adresi Location header'a yazilir.
        URI location = URI.create("/api/users/" + createdUser.getId());
        // Basarili olusturma icin 201 Created + body donulur.
        return ResponseEntity.created(location).body(createdUser);
    }

    // Var olan kullaniciyi tam guncellemek icin PUT /api/users/{id} endpoint'i kullanilir.
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        // Hedef kayit id ile bulunur ve yeni veriyle guncellenir.
        UserDto updatedUser = userService.update(id, userDto);
        // Basarili guncelleme icin 200 OK ile guncel veri donulur.
        return ResponseEntity.ok(updatedUser);
    }

    // Kayit silmek icin DELETE /api/users/{id} endpoint'i kullanilir.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        // Silme islemi servis katmanina delege edilir.
        userService.delete(id);
        // Basarili silme icin response body'siz 204 No Content donulur.
        return ResponseEntity.noContent().build();
    }
}
