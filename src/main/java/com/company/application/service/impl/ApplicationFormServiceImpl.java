package com.company.application.service.impl;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.company.application.dto.ApplicationFormDto;
import com.company.application.dto.response.ApplicationFormDetailResponse;
import com.company.application.entity.ApplicationForm;
import com.company.application.entity.Attachment;
import com.company.application.entity.FormType;
import com.company.application.entity.User;
import com.company.application.entity.enums.ApplicationStatus;
import com.company.application.mapper.ApplicationFormMapper;
import com.company.application.repository.ApplicationFormRepository;
import com.company.application.repository.FormTypeRepository;
import com.company.application.repository.UserRepository;
import com.company.application.repository.specification.ApplicationFormSpecifications;
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

    // Filtreleme, siralama ve sayfalama destekli listeleme islemini uygular.
    @Override
    public Page<ApplicationFormDto> findAll(ApplicationStatus status,
                                           Long formTypeId,
                                           Long applicantId,
                                           String keyword,
                                           java.time.LocalDateTime createdDateStart,
                                           java.time.LocalDateTime createdDateEnd,
                                           Pageable pageable,
                                           boolean isAdmin) {
        // applicantId filtresi sadece ADMIN tarafindan kullanilabilir.
        if (applicantId != null && !isAdmin) {
            throw new AccessDeniedException("Only ADMIN can filter by applicantId");
        }

        // Tarih araligi ters girilmisse anlamsiz sorgu engellenir.
        if (createdDateStart != null && createdDateEnd != null && createdDateStart.isAfter(createdDateEnd)) {
            throw new IllegalArgumentException("createdDateStart cannot be after createdDateEnd");
        }

        Specification<ApplicationForm> specification = ApplicationFormSpecifications.withFilters(
            status,
            formTypeId,
            applicantId,
            keyword,
            createdDateStart,
            createdDateEnd
        );

        Pageable normalizedPageable = normalizePageableSort(pageable);
        return applicationFormRepository.findAll(specification, normalizedPageable)
            .map(applicationFormMapper::toDto);
    }

    // Id ile basvuru formunu bulur ve DTO olarak dondurur.
    @Override
    public ApplicationFormDto findById(Long id) {
        ApplicationForm applicationForm = applicationFormRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("ApplicationForm not found with id: " + id));
        return applicationFormMapper.toDto(applicationForm);
    }

    // Detay ekrani icin tek basvuruyu iliskileriyle getirir ve role bazli yetki kontrolu uygular.
    @Override
    public ApplicationFormDetailResponse findDetailById(Long id, String authenticatedEmail, boolean isAdmin) {
        ApplicationForm applicationForm = applicationFormRepository.findDetailById(id)
            .orElseThrow(() -> new RuntimeException("ApplicationForm not found with id: " + id));

        // PERSONNEL sadece kendi olusturdugu basvuruyu gorebilir.
        if (!isAdmin && !applicationForm.getUser().getEmail().equalsIgnoreCase(authenticatedEmail)) {
            throw new AccessDeniedException("You are not allowed to view this application detail");
        }

        return toDetailResponse(applicationForm);
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

    // API'deki siralama alanlarini entity alanlarina guvenli sekilde map eder.
    private Pageable normalizePageableSort(Pageable pageable) {
        if (pageable == null || pageable.getSort().isUnsorted()) {
            return pageable;
        }

        Sort normalizedSort = Sort.unsorted();

        for (Sort.Order order : pageable.getSort()) {
            String normalizedProperty = mapSortProperty(order.getProperty());
            normalizedSort = normalizedSort.and(Sort.by(new Sort.Order(order.getDirection(), normalizedProperty)));
        }

        return org.springframework.data.domain.PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), normalizedSort);
    }

    // Sadece izin verilen siralama alanlarini kabul eder, digerlerini reddeder.
    private String mapSortProperty(String requestedProperty) {
        if (requestedProperty == null || requestedProperty.isBlank()) {
            return "applicationDate";
        }

        // Dokumandaki createdDate alias'i entity'deki applicationDate alanina yonlendirilir.
        if ("createdDate".equalsIgnoreCase(requestedProperty) || "applicationDate".equalsIgnoreCase(requestedProperty)) {
            return "applicationDate";
        }

        if ("title".equalsIgnoreCase(requestedProperty)) {
            return "title";
        }

        if ("status".equalsIgnoreCase(requestedProperty)) {
            return "status";
        }

        throw new IllegalArgumentException("Unsupported sort field: " + requestedProperty);
    }

    // Entity grafini frontend'in detail ekranina uygun DTO yapisina donusturur.
    private ApplicationFormDetailResponse toDetailResponse(ApplicationForm applicationForm) {
        String fullName = buildFullName(applicationForm.getUser().getName(), applicationForm.getUser().getSurname());

        return ApplicationFormDetailResponse.builder()
            .id(applicationForm.getId())
            .title(applicationForm.getTitle())
            .description(applicationForm.getDescription())
            .status(applicationForm.getStatus())
            .createdDate(applicationForm.getCreatedDate())
            .updatedDate(applicationForm.getUpdatedDate())
            .applicant(ApplicationFormDetailResponse.ApplicantInfo.builder()
                .id(applicationForm.getUser().getId())
                .fullName(fullName)
                .email(applicationForm.getUser().getEmail())
                .build())
            .formType(ApplicationFormDetailResponse.FormTypeInfo.builder()
                .id(applicationForm.getFormType().getId())
                .name(applicationForm.getFormType().getName())
                .build())
            .attachments(applicationForm.getAttachments().stream()
                .map(this::toAttachmentInfo)
                .collect(Collectors.toList()))
            .build();
    }

    // Attachment entity'sini response icindeki hafif gorunume donusturur.
    private ApplicationFormDetailResponse.AttachmentInfo toAttachmentInfo(Attachment attachment) {
        return ApplicationFormDetailResponse.AttachmentInfo.builder()
            .id(attachment.getId())
            .originalFileName(attachment.getOriginalName())
            .fileType(attachment.getFileType())
            .fileSize(attachment.getFileSize())
            .uploadDate(attachment.getUploadDate())
            .build();
    }

    // Ad ve soyadi bosluk kurallarina gore tek alana birlestirir.
    private String buildFullName(String name, String surname) {
        String safeName = name == null ? "" : name.trim();
        String safeSurname = surname == null ? "" : surname.trim();
        return (safeName + " " + safeSurname).trim();
    }
}
