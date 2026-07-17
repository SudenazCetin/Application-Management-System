package com.company.application.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Bu sinif, tum controller'lardan firlayan hatalari tek merkezden yakalayip
// tutarli bir JSON govdesiyle istemciye dondurur. Boylece @Valid hatalari
// Spring'in varsayilan hata sayfasina (/error) yonlendirilmez ve guvenlik
// filtre zincirinin bu yonlendirmeyi 403 olarak maskelemesi engellenir.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // DTO validation kurallari (@NotBlank, @Size, @Email vb.) ihlal edildiginde tetiklenir.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        // Ilk alan hatasi kullaniciya gosterilecek ana mesaj olarak secilir.
        FieldError firstFieldError = ex.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);
        String message = firstFieldError != null ? firstFieldError.getDefaultMessage() : "Validation failed";

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", false);
        body.put("message", message);

        // Tum alan hatalari, ihtiyac halinde alan bazli gosterim yapabilmek icin ayrica listelenir.
        body.put("errors", ex.getBindingResult().getFieldErrors().stream()
            .map(error -> Map.of("field", error.getField(), "message", String.valueOf(error.getDefaultMessage())))
            .toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // Durum gecisi ihlali gibi is kurali hatalarinda firlatilir (bkz. ApplicationFormServiceImpl).
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", false);
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // Multipart istekte boyut limiti asildiginda daha anlasilir bir mesaj dondurur.
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, Object>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", false);
        body.put("message", "Dosya boyutu en fazla 10 MB olabilir");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // Multipart parse hatalarini (bozuk/formatsiz multipart istegi gibi) tek merkezden yonetir.
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<Map<String, Object>> handleMultipartException(MultipartException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", false);
        body.put("message", "Dosya yukleme istegi islenemedi");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // Service katmaninda "not found" durumlari icin kullanilan genel RuntimeException'lari yakalar.
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "Unexpected error occurred";
        HttpStatus status = message.toLowerCase().contains("not found") ? HttpStatus.NOT_FOUND : HttpStatus.INTERNAL_SERVER_ERROR;

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", false);
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
