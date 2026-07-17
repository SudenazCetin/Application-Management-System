package com.company.application.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.application.dto.ApplicationFormDto;
import com.company.application.service.ApplicationFormService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// Bu sinif, ApplicationForm kaynagi icin HTTP isteklerini karsilar ve service katmanina yonlendirir.
@RestController
// Tum endpoint'leri tek bir kaynak adi altinda toplar: /api/forms
@RequestMapping("/api/forms")
// Constructor tabanli dependency injection icin gerekli constructor'u Lombok uretir.
@RequiredArgsConstructor
public class ApplicationFormController {

    // Is kurallari controller'da yazilmaz; service katmanina delege edilir.
    private final ApplicationFormService applicationFormService;

    // Tum basvuru formlarini listelemek icin GET /api/forms endpoint'i kullanilir.
    // Bu endpoint'e ADMIN ve PERSONNEL rolleri birlikte erisebilir.
    @PreAuthorize("hasAnyRole('ADMIN','PERSONNEL')")
    @GetMapping
    public ResponseEntity<List<ApplicationFormDto>> getAllForms() {
        // Veri servis katmanindan alinir.
        List<ApplicationFormDto> forms = applicationFormService.findAll();
        // Basarili listeleme icin 200 OK donulur.
        return ResponseEntity.ok(forms);
    }

    // Tek bir basvuru formunu id ile getirmek icin GET /api/forms/{id} endpoint'i kullanilir.
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationFormDto> getFormById(@PathVariable Long id) {
        // Path degiskenindeki id ile kayit aranir.
        ApplicationFormDto form = applicationFormService.findById(id);
        // Kayit bulunduysa 200 OK ile dto donulur.
        return ResponseEntity.ok(form);
    }

    // Yeni basvuru formu olusturmak icin POST /api/forms endpoint'i kullanilir.
    @PostMapping
    public ResponseEntity<ApplicationFormDto> createForm(@Valid @RequestBody ApplicationFormDto applicationFormDto) {
        // Gelen istek govdesi servis katmanina aktarilarak kayit olusturulur.
        ApplicationFormDto createdForm = applicationFormService.save(applicationFormDto);
        // REST standardi geregi olusan kaynagin adresi Location header'a yazilir.
        URI location = URI.create("/api/forms/" + createdForm.getId());
        // Basarili olusturma icin 201 Created + body donulur.
        return ResponseEntity.created(location).body(createdForm);
    }

    // Var olan basvuru formunu tam guncellemek icin PUT /api/forms/{id} endpoint'i kullanilir.
    @PutMapping("/{id}")
    public ResponseEntity<ApplicationFormDto> updateForm(@PathVariable Long id, @Valid @RequestBody ApplicationFormDto applicationFormDto) {
        // Hedef kayit id ile bulunur ve yeni veriyle guncellenir.
        ApplicationFormDto updatedForm = applicationFormService.update(id, applicationFormDto);
        // Basarili guncelleme icin 200 OK ile guncel veri donulur.
        return ResponseEntity.ok(updatedForm);
    }

    // Basvuruyu onaylamak icin PUT /api/forms/{id}/approve endpoint'i kullanilir.
    // Bu endpoint'e sadece ADMIN rolundeki kullanicilar erisebilir.
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/approve")
    public ResponseEntity<ApplicationFormDto> approveForm(@PathVariable Long id) {
        // Durum degisikligi ve state machine kontrolu service katmaninda yapilir.
        ApplicationFormDto approvedForm = applicationFormService.approve(id);
        // Basarili onaylama icin 200 OK ile guncel veri donulur.
        return ResponseEntity.ok(approvedForm);
    }

    // Basvuruyu reddetmek icin PUT /api/forms/{id}/reject endpoint'i kullanilir.
    // Bu endpoint'e sadece ADMIN rolundeki kullanicilar erisebilir.
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/reject")
    public ResponseEntity<ApplicationFormDto> rejectForm(@PathVariable Long id) {
        // Durum degisikligi ve state machine kontrolu service katmaninda yapilir.
        ApplicationFormDto rejectedForm = applicationFormService.reject(id);
        // Basarili reddetme icin 200 OK ile guncel veri donulur.
        return ResponseEntity.ok(rejectedForm);
    }

    // Kayit silmek icin DELETE /api/forms/{id} endpoint'i kullanilir.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteForm(@PathVariable Long id) {
        // Silme islemi servis katmanina delege edilir.
        applicationFormService.delete(id);
        // Basarili silme icin response body'siz 204 No Content donulur.
        return ResponseEntity.noContent().build();
    }
}
