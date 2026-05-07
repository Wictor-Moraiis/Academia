package com.wictor.Service;

import com.wictor.Dto.ExercicioDto;
import com.wictor.Dto.ExercicioResponseDto;
import com.wictor.Dto.ExercicioUpdateDto;
import com.wictor.exception.InternalErrorException;
import com.wictor.exception.InvalidImageException;
import com.wictor.exception.NotFoundException;
import com.wictor.exception.SizeException;
import com.wictor.model.Exercicio;
import com.wictor.model.Maquina;
import com.wictor.repository.ExercicioRepository;
import com.wictor.repository.MaquinaRepository;
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

    private final ExercicioRepository repository;
    private final MaquinaRepository maquinaRepository;

    @Value("${file.upload-dir-exercicio}")
    private String uploadDir;
    int maximumSizeMB = 5000000;

    public ExercicioService(ExercicioRepository repository,
                            MaquinaRepository maquinaRepository
    ) {

        this.repository = repository;
        this.maquinaRepository = maquinaRepository;
    }

    public Exercicio cadastrar(ExercicioDto exercicioDTO, MultipartFile foto) {

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

        Exercicio savedExercicio = repository.save(exercicio);
        Integer lastId = savedExercicio.getId();

        if (foto != null && !foto.isEmpty()) {
            String tipo = foto.getContentType();

            if (tipo == null || !tipo.startsWith("image/")) {
                throw new InvalidImageException("Arquivo não é uma imagem válida");
            }

            if (foto.getSize() > maximumSizeMB) {
                throw new SizeException("A imagem é muito grande. Tamanho máximo é de 5 MB");
            }

            String newName = "exercicio_" + lastId + ".png";
            byte[] fotoBytes;
            Path basePath = Paths.get(uploadDir);
            Path finalPath = basePath.resolve(newName);
            try {
                fotoBytes = foto.getBytes();
                Files.write(finalPath, fotoBytes);
            } catch (IOException e) {
                throw new InternalErrorException("Erro ao salvar imagem");
            }
            savedExercicio.setFoto("Exercicio_img/" + newName);
            repository.save(savedExercicio);
        }
        return savedExercicio;
    }

    public Exercicio atualizar(Integer id, ExercicioUpdateDto dto, MultipartFile foto) {
        Exercicio exercicio = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Exercicio não encontrado"));

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
            String tipo = foto.getContentType();

            if (tipo == null || !tipo.startsWith("image/")) {
                throw new InvalidImageException("Arquivo não é uma imagem válida");
            }

            if (foto.getSize() > maximumSizeMB) {
                throw new SizeException("A imagem é muito grande. Tamanho máximo é de 5 MB");
            }

            String newName = "Exercicio_" + exercicio.getId() + ".png";
            byte[] fotoBytes;
            Path basePath = Paths.get(uploadDir);
            Path finalPath = basePath.resolve(newName);
            try {
                fotoBytes = foto.getBytes();
                Files.write(finalPath, fotoBytes);
            } catch (IOException e) {
                throw new InternalErrorException("Erro ao salvar imagem");
            }
            exercicio.setFoto("Exercicio_img/" + newName);
        }

        return repository.save(exercicio);

    }

    public List<ExercicioResponseDto> listar() {
        return repository.findAll()
                .stream()
                .map(e -> new ExercicioResponseDto(
                        e.getId(),
                        e.getNome(),
                        e.getObs(),
                        e.getMaquina() != null ? e.getMaquina().getNome() : null
                ))
                .toList();
    }

    public ExercicioResponseDto buscarPorId(Integer id) {

        Exercicio exercicio = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Exercicio não encontrado"));

        return new ExercicioResponseDto(
                exercicio.getId(),
                exercicio.getNome(),
                exercicio.getObs(),
                exercicio.getMaquina() != null
                        ? exercicio.getMaquina().getNome()
                        : null
        );
    }

    public void deletar(Integer id) {

        Exercicio exercicio = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Exercicio não encontrado"));
        if (exercicio.getFoto() != null) {
            try {
                Path path = Paths.get(exercicio.getFoto());
                Files.deleteIfExists(path);

            } catch (IOException e) {
                System.err.println("Falha ao deletar a imagem: " + e.getMessage());
            }
        }
        repository.deleteById(id);
    }
}
