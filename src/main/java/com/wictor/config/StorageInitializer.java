package com.wictor.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class StorageInitializer {

    @Value("${file.upload-dir-user}")
    private String userDir;

    @Value("${file.upload-dir-exercicio}")
    private String exercicioDir;

    @Value("${file.upload-dir-produto}")
    private String produtoDir;

    @PostConstruct
    public void init() throws IOException {

        Files.createDirectories(Path.of(userDir));
        Files.createDirectories(Path.of(exercicioDir));
        Files.createDirectories(Path.of(produtoDir));
    }
}