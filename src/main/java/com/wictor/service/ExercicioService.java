package com.wictor.service;

import com.wictor.annotation.Auditar;
import com.wictor.audit.AuditoriaContext;
import com.wictor.audit.AuditoriaInfo;
import com.wictor.dto.exercicio.ExercicioDto;
import com.wictor.dto.exercicio.ExercicioResponseDto;
import com.wictor.dto.exercicio.ExercicioUpdateDto;
import com.wictor.enums.AcaoLog;
import com.wictor.exception.NotFoundException;
import com.wictor.model.Exercicio;
import com.wictor.model.Maquina;
import com.wictor.repository.ExercicioRepository;
import com.wictor.repository.MaquinaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ExercicioService {

    private final ExercicioRepository exercicioRepository;
    private final MaquinaRepository maquinaRepository;
    private final ImageService imageService;

    @Value("${file.upload-dir-exercicio}")
    private String uploadDir;
    private static final String PREFIXO_IMAGEM  = "exercicio";

    public ExercicioService(ExercicioRepository exercicioRepository,
                            MaquinaRepository maquinaRepository,
                            ImageService imageService) {

        this.exercicioRepository = exercicioRepository;
        this.maquinaRepository = maquinaRepository;
        this.imageService = imageService;
    }

    @Auditar(acao = AcaoLog.CADASTRO)
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

        atualizarFoto(savedExercicio, foto);

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .depois(savedExercicio)
                        .entidade("Exercicio")
                        .entidadeId(savedExercicio.getId())
                        .build()
        );

        return toResponseDto(savedExercicio);
    }

    @Auditar(acao = AcaoLog.ALTERACAO)
    @Transactional
    public ExercicioResponseDto atualizar(Integer id, ExercicioUpdateDto dto, MultipartFile foto) {

        Exercicio exercicio = buscarExercicioPorId(id);
        Exercicio exercicioAntes = copiarExercicio(exercicio);

        if (dto.nome() != null && !dto.nome().isBlank()) {exercicio.setNome(dto.nome());}

        if (dto.obs() != null && !dto.obs().isBlank()) {exercicio.setObs(dto.obs());}

        if (dto.maquinaId() != null) {
            Maquina maquina = maquinaRepository.findById(dto.maquinaId())
                    .orElseThrow(() -> new NotFoundException("Maquina não encontrada"));
            exercicio.setMaquina(maquina);
        }

        atualizarFoto(exercicio, foto);

        Exercicio exercicioSalvo = exercicioRepository.save(exercicio);

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .antes(exercicioAntes)
                        .depois(exercicioSalvo)
                        .entidade("Exercicio")
                        .entidadeId(exercicioSalvo.getId())
                        .build()
        );

        return toResponseDto(exercicio);
    }

    @Auditar(acao = AcaoLog.EXCLUSAO)
    @Transactional
    public void deletar(Integer id) {

        Exercicio exercicio = buscarExercicioPorId(id);

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .antes(exercicio)
                        .entidade("Exercicio")
                        .entidadeId(exercicio.getId())
                        .build()
        );

        imageService.deletarImagem(uploadDir, exercicio.getFoto());
        exercicioRepository.delete(exercicio);
    }

    @Auditar(acao = AcaoLog.CONSULTA, descricao = "Listagem de exercicios.")
    public List<ExercicioResponseDto> listar() {

        return exercicioRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Auditar(acao = AcaoLog.CONSULTA, descricao = "Consulta de exercicio por ID.")
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

    private void atualizarFoto(Exercicio exercicio, MultipartFile foto) {

        if (foto == null || foto.isEmpty()) {
            return;
        }

        imageService.validarImagem(foto);

        exercicio.setFoto(
                imageService.salvarImagem(
                        foto,
                        exercicio.getId(),
                        uploadDir,
                        PREFIXO_IMAGEM
                )
        );
    }

    private Exercicio copiarExercicio(Exercicio exercicio) {

        return Exercicio.builder()
                .id(exercicio.getId())
                .nome(exercicio.getNome())
                .obs(exercicio.getObs())
                .foto(exercicio.getFoto())
                .maquina(exercicio.getMaquina())
                .build();
    }
}

