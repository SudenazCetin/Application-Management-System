package com.company.application.service;

import java.util.List;

import com.company.application.dto.FormTypeDto;

// Bu arayuz, form turu is kurallarinin servis sozlesmesini tanimlar.
public interface FormTypeService {

    List<FormTypeDto> findAll();

    FormTypeDto findById(Long id);

    FormTypeDto save(FormTypeDto dto);

    FormTypeDto update(Long id, FormTypeDto dto);

    void delete(Long id);
}
