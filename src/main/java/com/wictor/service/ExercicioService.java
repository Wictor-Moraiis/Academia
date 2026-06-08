package com.wictor.service;

import com.wictor.dto.exercicio.ExercicioDto;
import com.wictor.dto.exercicio.ExercicioResponseDto;
import com.wictor.dto.exercicio.ExercicioUpdateDto;
import com.wictor.exception.InternalErrorException;
import com.wictor.exception.InvalidImageException;
import com.wictor.exception.NotFoundException;
import com.wictor.exception.SizeException;
import com.wictor.model.Exercicio;
import com.wictor.model.Maquina;
import com.wictor.repository.ExercicioRepository;
import com.wictor.repository.MaquinaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ExercicioService {

    private final ExercicioRepository exercicioRepository;
    private final MaquinaRepository maquinaRepository;

    @Value("${file.upload-dir-exercicio}")
    private String uploadDir;
    private static final long MAX_FILE_SIZE = 5_000_000;

    public ExercicioService(ExercicioRepository exercicioRepository,
                            MaquinaRepository maquinaRepository) {

        this.exercicioRepository = exercicioRepository;
        this.maquinaRepository = maquinaRepository;
    }

    @Transactional
    public ExercicioResponseDto cadastrar(ExercicioDto exercicioDTO, MultipartFile foto) {

        Maquina maquina = null;

        if(exercicioDTO.maquinaId() != null) {
            maquina = maquinaRepository.findById(exercicioDTO.maquinaId())
                    .orElseThrow(() -> new NotFoundException("Maquina não encontrada"));
        }

        Exercicio.ExercicioBuilder builder = Exercicio.builder()
                .nome(exercicioDTO.nome())
                .obs(exercicioDTO.obs())
                .maquina(maquina);
        Exercicio exercicio = builder.build();

        Exercicio savedExercicio = exercicioRepository.save(exercicio);

        if (foto != null && !foto.isEmpty()) {

            validarImagem(foto);

            savedExercicio.setFoto(
                    salvarImagem(foto, savedExercicio.getId())
            );

            exercicioRepository.save(savedExercicio);
        }
        return toResponseDto(savedExercicio);
    }

    @Transactional
    public ExercicioResponseDto atualizar(Integer id, ExercicioUpdateDto dto, MultipartFile foto) {
        Exercicio exercicio = buscarExercicioPorId(id);

        if (dto.nome() != null && !dto.nome().isBlank()) {
            exercicio.setNome(dto.nome());
        }

        if (dto.obs() != null && !dto.obs().isBlank()) {
            exercicio.setObs(dto.obs());
        }

        if (dto.maquinaId() != null) {
            Maquina maquina = maquinaRepository.findById(dto.maquinaId())
                    .orElseThrow(() -> new NotFoundException("Maquina não encontrada"));
            exercicio.setMaquina(maquina);
        }

        if (foto != null && !foto.isEmpty()) {

            validarImagem(foto);

            exercicio.setFoto(
                    salvarImagem(foto, exercicio.getId())
            );
        }
        return toResponseDto(exercicio);
    }

    public List<ExercicioResponseDto> listar() {

        return exercicioRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public ExercicioResponseDto buscarPorId(Integer id) {

        return toResponseDto(buscarExercicioPorId(id));
    }

    private Exercicio buscarExercicioPorId(Integer id) {
        return exercicioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Exercicio não encontrado"));
    }

    private ExercicioResponseDto toResponseDto(Exercicio exercicio) {
        return new ExercicioResponseDto(
                exercicio.getId(),
                exercicio.getNome(),
                exercicio.getObs(),
                exercicio.getMaquina() != null
                        ? exercicio.getMaquina().getNome()
                        : null
        );
    }

    @Transactional
    public void deletar(Integer id) {

        Exercicio exercicio = buscarExercicioPorId(id);

        if (exercicio.getFoto() != null) {
            try {
                Path path = Paths.get(uploadDir)
                        .resolve(exercicio.getFoto());
                Files.deleteIfExists(path);

            }catch (IOException e) {
                throw new InternalErrorException("Erro ao deletar imagem");
            }
        }
        exercicioRepository.delete(exercicio);
    }

    private void validarImagem(MultipartFile foto) {

        String tipo = foto.getContentType();

        if (tipo == null || !tipo.startsWith("image/")) {
            throw new InvalidImageException("Arquivo não é uma imagem válida");
        }

        if (foto.getSize() > MAX_FILE_SIZE) {
            throw new SizeException("A imagem é muito grande. Tamanho máximo é de 5 MB");
        }
    }

    private String salvarImagem(MultipartFile foto, Integer id) {

        Path basePath = Paths.get(uploadDir);

        try {
            Files.createDirectories(basePath);

            String fileName = "exercicio_" + id + ".png";

            Files.write(
                    basePath.resolve(fileName),
                    foto.getBytes()
            );

            return fileName;

        } catch (IOException e) {
            throw new InternalErrorException("Erro ao salvar imagem");
        }
    }
}

