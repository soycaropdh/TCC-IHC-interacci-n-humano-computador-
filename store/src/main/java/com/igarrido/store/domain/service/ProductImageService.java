package com.igarrido.store.domain.service;

import com.igarrido.store.domain.exception.DomainValidationException;
import com.igarrido.store.domain.port.ImageStoragePort;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Reglas de negocio para las imágenes de producto. Clase Java pura (sin Spring).
 *
 * Seguridad: el tipo de archivo se decide mirando el CONTENIDO real (los primeros
 * bytes), no el nombre ni el tipo que declara el navegador. Y el nombre final lo
 * genera el sistema, así nadie puede subir "virus.exe" ni escribir fuera de la carpeta.
 */
public class ProductImageService {

    public static final int MAX_BYTES = 5 * 1024 * 1024; // 5 MB

    private final ImageStoragePort storage;

    public ProductImageService(ImageStoragePort storage) {
        this.storage = storage;
    }

    public String upload(byte[] content) {
        if (content == null || content.length == 0) {
            throw new DomainValidationException("Selecciona una imagen");
        }
        if (content.length > MAX_BYTES) {
            throw new DomainValidationException("La imagen no puede superar 5 MB");
        }

        String extension = detectExtension(content);
        if (extension == null) {
            throw new DomainValidationException("Formato no permitido. Usa JPG, PNG, WEBP o GIF");
        }

        String filename = UUID.randomUUID() + "." + extension;
        return storage.save(filename, content);
    }

    /** Devuelve la extensión según el contenido, o null si no es una imagen permitida. */
    static String detectExtension(byte[] b) {
        // JPEG: FF D8 FF
        if (b.length >= 3 && (b[0] & 0xFF) == 0xFF && (b[1] & 0xFF) == 0xD8 && (b[2] & 0xFF) == 0xFF) {
            return "jpg";
        }
        // PNG: 89 50 4E 47 0D 0A 1A 0A
        if (b.length >= 8
                && (b[0] & 0xFF) == 0x89 && b[1] == 'P' && b[2] == 'N' && b[3] == 'G'
                && b[4] == 0x0D && b[5] == 0x0A && b[6] == 0x1A && b[7] == 0x0A) {
            return "png";
        }
        // GIF: "GIF87a" o "GIF89a"
        if (matches(b, 0, "GIF87a") || matches(b, 0, "GIF89a")) {
            return "gif";
        }
        // WEBP: "RIFF" + 4 bytes de tamaño + "WEBP"
        if (b.length >= 12 && matches(b, 0, "RIFF") && matches(b, 8, "WEBP")) {
            return "webp";
        }
        return null;
    }

    private static boolean matches(byte[] data, int offset, String ascii) {
        byte[] expected = ascii.getBytes(StandardCharsets.US_ASCII);
        if (data.length < offset + expected.length) {
            return false;
        }
        for (int i = 0; i < expected.length; i++) {
            if (data[offset + i] != expected[i]) {
                return false;
            }
        }
        return true;
    }
}
