package com.company.application.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.company.application.dto.FormTypeDto;
import com.company.application.entity.FormType;
import com.company.application.mapper.FormTypeMapper;
import com.company.application.repository.FormTypeRepository;
import com.company.application.service.FormTypeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FormTypeServiceImpl implements FormTypeService {

    private final FormTypeRepository formTypeRepository;
    private final FormTypeMapper formTypeMapper;

    // Tum form turlerini getirir ve DTO listesine donusturur.
    @Override
    public List<FormTypeDto> findAll() {
        List<FormType> formTypes = formTypeRepository.findAll();
        return formTypeMapper.toDtoList(formTypes);
    }

    // Id ile form turunu bulur ve DTO olarak dondurur.
    @Override
    public FormTypeDto findById(Long id) {
        FormType formType = formTypeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("FormType not found with id: " + id));
        return formTypeMapper.toDto(formType);
    }

    // Yeni form turunu kaydeder ve DTO olarak dondurur.
    @Override
    public FormTypeDto save(FormTypeDto dto) {
        FormType formType = formTypeMapper.toEntity(dto);
        FormType savedFormType = formTypeRepository.save(formType);
        return formTypeMapper.toDto(savedFormType);
    }

    // Mevcut form turunu gunceller ve DTO olarak dondurur.
    @Override
    public FormTypeDto update(Long id, FormTypeDto dto) {
        FormType formType = formTypeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("FormType not found with id: " + id));

        formType.setName(dto.getName());
        formType.setDescription(dto.getDescription());

        FormType updatedFormType = formTypeRepository.save(formType);
        return formTypeMapper.toDto(updatedFormType);
    }

    // Id ile form turunu bulur ve veritabanindan siler.
    @Override
    public void delete(Long id) {
        FormType formType = formTypeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("FormType not found with id: " + id));
        formTypeRepository.delete(formType);
    }
}
