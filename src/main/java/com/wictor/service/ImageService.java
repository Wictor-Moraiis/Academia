package com.wictor.service;

import com.wictor.dto.exercicio.ExercicioResponseDto;
import com.wictor.exception.InternalErrorException;
import com.wictor.exception.InvalidImageException;
import com.wictor.exception.NotFoundException;
import com.wictor.exception.SizeException;
import com.wictor.model.Exercicio;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {

    private static final long MAX_FILE_SIZE = 5_000_000;

    public void validarImagem(MultipartFile foto) {

        String tipo = foto.getContentType();

        if (tipo == null || !tipo.startsWith("image/")) {
            throw new InvalidImageException("Arquivo não é uma imagem válida");
        }

        if (foto.getSize() > MAX_FILE_SIZE) {
            throw new SizeException("A imagem é muito grande. Tamanho máximo é de 5 MB");
        }
    }

    public String salvarImagem(MultipartFile foto, Integer id, String uploadDir, String prefixo) {

        Path basePath = Paths.get(uploadDir);

        try {
            Files.createDirectories(basePath);

            String fileName = prefixo + "_" + id + ".png";

            Files.write(
                    basePath.resolve(fileName),
                    foto.getBytes()
            );

            return fileName;

        } catch (IOException e) {
            throw new InternalErrorException("Erro ao salvar imagem");
        }
    }

    public void deletarImagem(String uploadDir, String nomeArquivo) {

        if (nomeArquivo == null || nomeArquivo.isBlank()) {
            return;
        }

        try {
            Path path = Paths.get(uploadDir)
                    .resolve(nomeArquivo);

            Files.deleteIfExists(path);

        } catch (IOException e) {
            throw new InternalErrorException("Erro ao deletar imagem");
        }
    }
}
