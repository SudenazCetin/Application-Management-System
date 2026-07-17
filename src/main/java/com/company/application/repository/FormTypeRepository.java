package com.company.application.repository;

import com.company.application.entity.FormType;
import org.springframework.data.jpa.repository.JpaRepository;

// Bu arayuz, FormType entity'si icin veritabani islemlerini saglar.
public interface FormTypeRepository extends JpaRepository<FormType, Long> {
}
