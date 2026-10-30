package com.igarrido.store.domain.port;

/**
 * Puerto de salida para guardar imágenes. El dominio no sabe si se guardan
 * en el disco, en la nube (S3...) o en otro sitio.
 */
public interface ImageStoragePort {
    /**
     * Guarda la imagen y devuelve la ruta pública con la que luego se consulta
     * (por ejemplo "/uploads/abc123.png").
     */
    String save(String filename, byte[] content);
}
