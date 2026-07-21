package com.company.application.repository.specification;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.company.application.entity.ApplicationForm;
import com.company.application.entity.enums.ApplicationStatus;

// Bu sinif, ApplicationForm icin dinamik filtre kriterlerini tek noktada toplar.
public final class ApplicationFormSpecifications {

    private ApplicationFormSpecifications() {
        // Utility sinifi oldugu icin instance uretilmesi engellenir.
    }

    // Gelen query parametrelerine gore birlestirilmis specification nesnesi olusturur.
    public static Specification<ApplicationForm> withFilters(ApplicationStatus status,
                                                             Long formTypeId,
                                                             Long applicantId,
                                                             String keyword,
                                                             LocalDateTime createdDateStart,
                                                             LocalDateTime createdDateEnd) {
        Specification<ApplicationForm> specification = Specification.where(null);

        // Duruma gore filtre eklenir (ornegin APPROVED, NEW vb.).
        if (status != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status));
        }

        // Form turune gore filtre eklenir.
        if (formTypeId != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("formType").get("id"), formTypeId));
        }

        // Basvuru sahibine gore filtre eklenir (yalnizca ADMIN tarafinda kullanilacak).
        if (applicantId != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user").get("id"), applicantId));
        }

        // Baslik ve aciklama alanlarinda case-insensitive anahtar kelime aramasi yapilir.
        if (keyword != null && !keyword.isBlank()) {
            String pattern = "%" + keyword.trim().toLowerCase() + "%";
            specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern)
                ));
        }

        // Baslangic tarihi verildiyse bu tarihten sonraki kayitlar filtrelenir.
        if (createdDateStart != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("applicationDate"), createdDateStart));
        }

        // Bitis tarihi verildiyse bu tarihten onceki kayitlar filtrelenir.
        if (createdDateEnd != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("applicationDate"), createdDateEnd));
        }

        return specification;
    }
}
