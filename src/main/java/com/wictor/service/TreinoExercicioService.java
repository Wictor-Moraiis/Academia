package com.wictor.service;

import com.wictor.dto.treino.TreinoExercicioDto;
import com.wictor.dto.treino.TreinoExercicioResponseDto;
import com.wictor.dto.treinoexercicio.TreinoExercicioUpdateDto;
import com.wictor.exception.NotFoundException;
import com.wictor.model.*;
import com.wictor.repository.ExercicioRepository;
import com.wictor.repository.TreinoExercicioRepository;
import com.wictor.repository.TreinoRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TreinoExercicioService {

    private final TreinoExercicioRepository repository;
    private final TreinoRepository treinoRepository;
    private final ExercicioRepository exercicioRepository;

    public TreinoExercicioService(
            TreinoExercicioRepository repository,
            TreinoRepository treinoRepository,
            ExercicioRepository exercicioRepository) {

        this.repository = repository;
        this.treinoRepository = treinoRepository;
        this.exercicioRepository = exercicioRepository;
    }
    private void validarPermissao(Treino treino, User logado) {

        boolean admin = logado.getRole().name().equals("ADMIN");

        boolean dono =
                treino.getAluno() != null
                        && logado.getAluno() != null
                        && treino.getAluno().getId().equals(logado.getAluno().getId());

        if (!admin && !dono) {
            throw new AccessDeniedException("Sem permissão");
        }
    }
    public TreinoExercicio cadastrar(TreinoExercicioDto dto, User logado) {
        Treino treino = treinoRepository.findById(dto.treinoId())
                .orElseThrow(() -> new NotFoundException("Treino não encontrado"));

        Exercicio exercicio = exercicioRepository.findById(dto.exercicioId())
                .orElseThrow(() -> new NotFoundException("Exercício não encontrado"));

        validarPermissao(treino, logado);

        TreinoExercicioId id = TreinoExercicioId.builder()
                .treinoId(treino.getId())
                .exercId(exercicio.getId())
                .build();

        TreinoExercicio treinoExercicio = TreinoExercicio.builder()
                .id(id)
                .treino(treino)
                .exercicio(exercicio)
                .ordem(dto.ordem())
                .carga(dto.carga())
                .series(dto.series())
                .rep(dto.rep())
                .obs(dto.obs())
                .build();

        return repository.save(treinoExercicio);
    }

    public TreinoExercicio atualizar(TreinoExercicioId id, TreinoExercicioUpdateDto dto, User logado) {
        TreinoExercicio treinoExercicio = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Exercício do treino não encontrado"));
        validarPermissao(treinoExercicio.getTreino(), logado);
        if (dto.ordem() != null) {
            treinoExercicio.setOrdem(dto.ordem());
        }

        if (dto.carga() != null && !dto.carga().isBlank()) {
            treinoExercicio.setCarga(dto.carga());
        }

        if (dto.series() != null && !dto.series().isBlank()) {
            treinoExercicio.setSeries(dto.series());
        }

        if (dto.rep() != null && !dto.rep().isBlank()) {
            treinoExercicio.setRep(dto.rep());
        }

        if (dto.obs() != null && !dto.obs().isBlank()) {
            treinoExercicio.setObs(dto.obs());
        }

        return repository.save(treinoExercicio);
    }

    public List<TreinoExercicioResponseDto> listar() {
        return repository.findAll()
                .stream()
                .map(te -> new TreinoExercicioResponseDto(
                        te.getTreino().getId(),
                        te.getTreino().getNome(),
                        te.getExercicio().getId(),
                        te.getExercicio().getNome(),
                        te.getOrdem(),
                        te.getCarga(),
                        te.getSeries(),
                        te.getRep(),
                        te.getObs()
                ))
                .toList();
    }

    public TreinoExercicio buscarPorId(TreinoExercicioId id) {

        TreinoExercicio te = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Exercício do treino não encontrado"));
        return te;
    }

    public void deletar(TreinoExercicioId id, User logado){
        TreinoExercicio treinoExercicio = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Exercício do treino não encontrado"));

        validarPermissao(treinoExercicio.getTreino(), logado);

        repository.delete(treinoExercicio);
    }
}
