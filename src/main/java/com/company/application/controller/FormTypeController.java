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

import com.company.application.dto.FormTypeDto;
import com.company.application.service.FormTypeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// Bu sinif, FormType kaynagi icin HTTP isteklerini karsilar ve service katmanina yonlendirir.
@RestController
// Tum endpoint'leri tek bir kaynak adi altinda toplar: /api/form-types
@RequestMapping("/api/form-types")
// Constructor tabanli dependency injection icin gerekli constructor'u Lombok uretir.
@RequiredArgsConstructor
public class FormTypeController {

    // Is kurallari controller'da yazilmaz; service katmanina delege edilir.
    private final FormTypeService formTypeService;

    // Tum form turlerini listelemek icin GET /api/form-types endpoint'i kullanilir.
    @GetMapping
    public ResponseEntity<List<FormTypeDto>> getAllFormTypes() {
        // Veri servis katmanindan alinir.
        List<FormTypeDto> formTypes = formTypeService.findAll();
        // Basarili listeleme icin 200 OK donulur.
        return ResponseEntity.ok(formTypes);
    }

    // Tek bir form turunu id ile getirmek icin GET /api/form-types/{id} endpoint'i kullanilir.
    @GetMapping("/{id}")
    public ResponseEntity<FormTypeDto> getFormTypeById(@PathVariable Long id) {
        // Path degiskenindeki id ile kayit aranir.
        FormTypeDto formType = formTypeService.findById(id);
        // Kayit bulunduysa 200 OK ile dto donulur.
        return ResponseEntity.ok(formType);
    }

    // Yeni form turu olusturmak icin POST /api/form-types endpoint'i kullanilir.
    // Bu endpoint'e sadece ADMIN rolundeki kullanicilar erisebilir.
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<FormTypeDto> createFormType(@Valid @RequestBody FormTypeDto formTypeDto) {
        // Gelen istek govdesi servis katmanina aktarilarak kayit olusturulur.
        FormTypeDto createdFormType = formTypeService.save(formTypeDto);
        // REST standardi geregi olusan kaynagin adresi Location header'a yazilir.
        URI location = URI.create("/api/form-types/" + createdFormType.getId());
        // Basarili olusturma icin 201 Created + body donulur.
        return ResponseEntity.created(location).body(createdFormType);
    }

    // Var olan form turunu tam guncellemek icin PUT /api/form-types/{id} endpoint'i kullanilir.
    @PutMapping("/{id}")
    public ResponseEntity<FormTypeDto> updateFormType(@PathVariable Long id, @Valid @RequestBody FormTypeDto formTypeDto) {
        // Hedef kayit id ile bulunur ve yeni veriyle guncellenir.
        FormTypeDto updatedFormType = formTypeService.update(id, formTypeDto);
        // Basarili guncelleme icin 200 OK ile guncel veri donulur.
        return ResponseEntity.ok(updatedFormType);
    }

    // Kayit silmek icin DELETE /api/form-types/{id} endpoint'i kullanilir.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormType(@PathVariable Long id) {
        // Silme islemi servis katmanina delege edilir.
        formTypeService.delete(id);
        // Basarili silme icin response body'siz 204 No Content donulur.
        return ResponseEntity.noContent().build();
    }
}
