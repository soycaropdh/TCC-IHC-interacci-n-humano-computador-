package com.igarrido.store.infrastructure.storage;

import com.igarrido.store.domain.port.ImageStoragePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Adaptador: implementa el puerto guardando las imágenes en una carpeta del disco.
 * Por defecto la carpeta "uploads" se crea dentro de back/store.
 */
@Component
public class LocalImageStorage implements ImageStoragePort {

    private final Path root;

    public LocalImageStorage(@Value("${uploads.dir:uploads}") String uploadsDir) {
        this.root = Paths.get(uploadsDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(root);
        } catch (IOException ex) {
            throw new UncheckedIOException("No se pudo crear la carpeta de imágenes: " + root, ex);
        }
    }

    @Override
    public String save(String filename, byte[] content) {
        Path target = root.resolve(filename).normalize();
        if (!target.startsWith(root)) {
            throw new IllegalArgumentException("Nombre de archivo no válido");
        }
        try {
            Files.write(target, content);
        } catch (IOException ex) {
            throw new UncheckedIOException("No se pudo guardar la imagen", ex);
        }
        return "/uploads/" + filename;
    }

    public Path getRoot() {
        return root;
    }
}
