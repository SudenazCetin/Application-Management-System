package com.company.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.company.application.entity.enums.ApplicationStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Bu DTO, tek bir basvurunun detay ekraninda gereken tum veriyi tasir.
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationFormDetailResponse {

    private Long id;
    private String title;
    private String description;
    private ApplicationStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private ApplicantInfo applicant;
    private FormTypeInfo formType;
    private List<AttachmentInfo> attachments;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplicantInfo {
        private Long id;
        private String fullName;
        private String email;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FormTypeInfo {
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttachmentInfo {
        private Long id;
        private String originalFileName;
        private String fileType;
        private Long fileSize;
        private LocalDateTime uploadDate;
    }
}
