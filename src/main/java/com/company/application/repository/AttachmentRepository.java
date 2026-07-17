package com.company.application.repository;

import com.company.application.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

// Bu arayuz, Attachment entity'si icin veritabani islemlerini saglar.
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
