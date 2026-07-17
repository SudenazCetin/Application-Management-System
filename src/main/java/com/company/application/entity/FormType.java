package com.company.application.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Bu sınıf, form türlerini veritabanında temsil eden JPA entity'sidir.
@Entity
// Veritabanındaki tablo adını açıkça belirler.
@Table(name = "form_types")
// Alanlar için getter metotlarını otomatik üretir.
@Getter
// Alanlar için setter metotlarını otomatik üretir.
@Setter
// Parametresiz kurucu metodu otomatik üretir.
@NoArgsConstructor
// Tüm alanları içeren kurucu metodu otomatik üretir.
@AllArgsConstructor
public class FormType {

    // Birincil anahtar alanını belirtir.
    @Id
    // Kimlik değerinin otomatik üretilmesini sağlar.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Form türünün adını tutar.
    private String name;

    // Form türüne ait açıklamayı tutar.
    private String description;

    // Bu form türüne ait başvuru formlarını tutar.
    @OneToMany(
        mappedBy = "formType",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<ApplicationForm> applicationForms;
}
