package com.company.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Bu sinif, form turu verilerini tasimak icin kullanilir.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormTypeDto {

    private Long id;

    // Form turu adinin bos gecilmesini engeller.
    @NotBlank(message = "Form type name is required")
    // Form turu adinin uzunlugunu sinirlar.
    @Size(min = 2, max = 100, message = "Form type name must be between 2 and 100 characters")
    private String name;

    // Aciklama alaninin bos gecilmesini engeller.
    @NotBlank(message = "Description is required")
    // Aciklama uzunlugunu yonetilebilir bir aralikta tutar.
    @Size(min = 3, max = 500, message = "Description must be between 3 and 500 characters")
    private String description;
}
