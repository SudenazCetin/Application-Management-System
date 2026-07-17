package com.company.application.service.impl;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.company.application.dto.ApplicationFormDto;
import com.company.application.entity.ApplicationForm;
import com.company.application.entity.FormType;
import com.company.application.entity.User;
import com.company.application.entity.enums.ApplicationStatus;
import com.company.application.mapper.ApplicationFormMapper;
import com.company.application.repository.ApplicationFormRepository;
import com.company.application.repository.FormTypeRepository;
import com.company.application.repository.UserRepository;
import com.company.application.service.ApplicationFormService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationFormServiceImpl implements ApplicationFormService {

    // Her durumdan hangi durumlara gecis yapilabilecegini tanimlayan kurallar haritasi.
    // APPROVED, REJECTED ve CANCELLED nihai (terminal) durumlardir; bu durumlara ulasan
    // bir basvuru bir daha degistirilemez (ornegin onaylanmis bir basvuru tekrar onaylanamaz).
    private static final Map<ApplicationStatus, Set<ApplicationStatus>> ALLOWED_TRANSITIONS = new EnumMap<>(ApplicationStatus.class);

    static {
        ALLOWED_TRANSITIONS.put(ApplicationStatus.NEW, EnumSet.of(ApplicationStatus.IN_REVIEW, ApplicationStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(ApplicationStatus.IN_REVIEW, EnumSet.of(ApplicationStatus.APPROVED, ApplicationStatus.REJECTED, ApplicationStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(ApplicationStatus.APPROVED, EnumSet.noneOf(ApplicationStatus.class));
        ALLOWED_TRANSITIONS.put(ApplicationStatus.REJECTED, EnumSet.noneOf(ApplicationStatus.class));
        ALLOWED_TRANSITIONS.put(ApplicationStatus.CANCELLED, EnumSet.noneOf(ApplicationStatus.class));
    }

    private final ApplicationFormRepository applicationFormRepository;
    private final UserRepository userRepository;
    private final FormTypeRepository formTypeRepository;
    private final ApplicationFormMapper applicationFormMapper;

    // Tum basvuru formlarini getirir ve DTO listesine donusturur.
    @Override
    public List<ApplicationFormDto> findAll() {
        List<ApplicationForm> applicationForms = applicationFormRepository.findAll();
        return applicationFormMapper.toDtoList(applicationForms);
    }

    // Id ile basvuru formunu bulur ve DTO olarak dondurur.
    @Override
    public ApplicationFormDto findById(Long id) {
        ApplicationForm applicationForm = applicationFormRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("ApplicationForm not found with id: " + id));
        return applicationFormMapper.toDto(applicationForm);
    }

    // Yeni basvuru formunu kaydeder ve DTO olarak dondurur.
    @Override
    public ApplicationFormDto save(ApplicationFormDto dto) {
        ApplicationForm applicationForm = applicationFormMapper.toEntity(dto);

        User user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));
        FormType formType = formTypeRepository.findById(dto.getFormTypeId())
            .orElseThrow(() -> new RuntimeException("FormType not found with id: " + dto.getFormTypeId()));

        // Yeni olusturulan her basvuru, durum akisinin baslangic noktasi olan NEW ile baslar.
        applicationForm.setStatus(ApplicationStatus.NEW);
        applicationForm.setUser(user);
        applicationForm.setFormType(formType);

        ApplicationForm savedApplicationForm = applicationFormRepository.save(applicationForm);
        return applicationFormMapper.toDto(savedApplicationForm);
    }

    // Mevcut basvuru formunu gunceller ve DTO olarak dondurur.
    @Override
    public ApplicationFormDto update(Long id, ApplicationFormDto dto) {
        ApplicationForm applicationForm = applicationFormRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("ApplicationForm not found with id: " + id));

        // Durum gecisi is kurallarina uygun mu kontrol edilir (bkz. ALLOWED_TRANSITIONS).
        validateStatusTransition(applicationForm.getStatus(), dto.getStatus());

        User user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));
        FormType formType = formTypeRepository.findById(dto.getFormTypeId())
            .orElseThrow(() -> new RuntimeException("FormType not found with id: " + dto.getFormTypeId()));

        applicationForm.setTitle(dto.getTitle());
        applicationForm.setDescription(dto.getDescription());
        applicationForm.setApplicationDate(dto.getApplicationDate());
        applicationForm.setStatus(dto.getStatus());
        applicationForm.setUser(user);
        applicationForm.setFormType(formType);

        ApplicationForm updatedApplicationForm = applicationFormRepository.save(applicationForm);
        return applicationFormMapper.toDto(updatedApplicationForm);
    }

    // Bir basvurunun mevcut durumundan istenen yeni duruma gecisinin gecerli olup olmadigini dogrular.
    private void validateStatusTransition(ApplicationStatus currentStatus, ApplicationStatus newStatus) {
        // Durum degismiyorsa (baslik/aciklama gibi diger alanlarin guncellenmesi) ek kontrole gerek yoktur.
        if (currentStatus == null || currentStatus == newStatus) {
            return;
        }

        Set<ApplicationStatus> allowedNextStatuses = ALLOWED_TRANSITIONS.getOrDefault(currentStatus, EnumSet.noneOf(ApplicationStatus.class));

        if (!allowedNextStatuses.contains(newStatus)) {
            if (allowedNextStatuses.isEmpty()) {
                throw new IllegalArgumentException(
                    "Application is already finalized with status " + currentStatus + " and cannot be changed");
            }

            throw new IllegalArgumentException(
                "Invalid status transition from " + currentStatus + " to " + newStatus);
        }
    }

    // Basvuruyu onaylar. Ortak durum degistirme akisi changeStatus() icinde calisir,
    // burada sadece hedef durum (APPROVED) belirtilir.
    @Override
    public ApplicationFormDto approve(Long id) {
        return changeStatus(id, ApplicationStatus.APPROVED);
    }

    // Basvuruyu reddeder. Ortak durum degistirme akisi changeStatus() icinde calisir,
    // burada sadece hedef durum (REJECTED) belirtilir.
    @Override
    public ApplicationFormDto reject(Long id) {
        return changeStatus(id, ApplicationStatus.REJECTED);
    }

    // approve() ve reject() tarafindan ortak kullanilan durum degistirme akisi.
    // Boylece iki metod arasinda "id ile bul -> gecisi dogrula -> kaydet" mantigi tekrar edilmez.
    private ApplicationFormDto changeStatus(Long id, ApplicationStatus targetStatus) {
        ApplicationForm applicationForm = applicationFormRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("ApplicationForm not found with id: " + id));

        // update() ile ayni state machine kurali burada da uygulanir; ornegin
        // zaten APPROVED/REJECTED/CANCELLED olan bir basvuru tekrar degistirilemez.
        validateStatusTransition(applicationForm.getStatus(), targetStatus);

        applicationForm.setStatus(targetStatus);

        ApplicationForm savedApplicationForm = applicationFormRepository.save(applicationForm);
        return applicationFormMapper.toDto(savedApplicationForm);
    }

    // Id ile basvuru formunu bulur ve veritabanindan siler.
    @Override
    public void delete(Long id) {
        ApplicationForm applicationForm = applicationFormRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("ApplicationForm not found with id: " + id));
        applicationFormRepository.delete(applicationForm);
    }
}
