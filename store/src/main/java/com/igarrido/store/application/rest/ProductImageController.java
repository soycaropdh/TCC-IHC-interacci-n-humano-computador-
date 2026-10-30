package com.igarrido.store.application.rest;

import com.igarrido.store.domain.exception.DomainValidationException;
import com.igarrido.store.domain.service.ProductImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * Sube la imagen de un producto. Está bajo /api/products, así que
 * (por SecurityConfig) solo un ADMIN puede usarla.
 */
@RestController
@RequestMapping("/api/products/images")
public class ProductImageController {

    private final ProductImageService imageService;

    public ProductImageController(ProductImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        byte[] content;
        try {
            content = file.getBytes();
        } catch (IOException ex) {
            throw new DomainValidationException("No se pudo leer el archivo");
        }

        String url = imageService.upload(content);
        return new ResponseEntity<>(Map.of("url", url), HttpStatus.CREATED);
    }
}
