package com.wictor.service;

import com.wictor.dto.treinoexercicio.TreinoExercicioDto;
import com.wictor.dto.treinoexercicio.TreinoExercicioResponseDto;
import com.wictor.dto.treinoexercicio.TreinoExercicioUpdateDto;
import com.wictor.enums.Role;
import com.wictor.exception.NotFoundException;
import com.wictor.model.*;
import com.wictor.repository.ExercicioRepository;
import com.wictor.repository.TreinoExercicioRepository;
import com.wictor.repository.TreinoRepository;
import com.wictor.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TreinoExercicioService {

    private final TreinoExercicioRepository treinoexercicioRepository;
    private final TreinoRepository treinoRepository;
    private final ExercicioRepository exercicioRepository;
    private final UserRepository userRepository;

    public TreinoExercicioService(
            TreinoExercicioRepository treinoexercicioRepository,
            TreinoRepository treinoRepository,
            ExercicioRepository exercicioRepository,
            UserRepository userRepository) {

        this.treinoexercicioRepository = treinoexercicioRepository;
        this.treinoRepository = treinoRepository;
        this.exercicioRepository = exercicioRepository;
        this.userRepository = userRepository;
    }
    private void validarPermissao(Treino treino, User logado) {

        boolean admin = logado.getRole() == Role.ADMIN;
        boolean gerente = logado.getRole() == Role.GERENTE;
        boolean professor = logado.getRole() == Role.PROFESSOR;

        boolean alunoDono =
                logado.getAluno() != null &&
                        treino.getAluno() != null &&
                        treino.getAluno().getId().equals(logado.getAluno().getId());

        if (!(admin || gerente || professor || alunoDono)) {
            throw new AccessDeniedException("Sem permissão");
        }
    }

    public TreinoExercicioResponseDto cadastrar(TreinoExercicioDto dto, Integer userId) {

        User logado = getUser(userId);

        Treino treino = buscarTreinoAutorizado(dto.treinoId(), logado);

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

        TreinoExercicio salvo = treinoexercicioRepository.save(treinoExercicio);

        return toResponseDto(salvo);
    }

    public TreinoExercicioResponseDto atualizar(TreinoExercicioId id, TreinoExercicioUpdateDto dto, Integer userId) {

        User logado = getUser(userId);

        TreinoExercicio treinoExercicio = buscarTreinoExercicioPorId(id);

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

        return toResponseDto(treinoexercicioRepository.save(treinoExercicio));
    }

    public List<TreinoExercicioResponseDto> listar() {
        return treinoexercicioRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public TreinoExercicioResponseDto buscarPorId(TreinoExercicioId id, Integer userId) {

        User logado = getUser(userId);

        TreinoExercicio te = buscarTreinoExercicioPorId(id);

        validarPermissao(te.getTreino(), logado);

        return toResponseDto(te);
    }

    public void deletar(TreinoExercicioId id, Integer userId){

        User logado = getUser(userId);

        TreinoExercicio treinoExercicio = buscarTreinoExercicioPorId(id);

        validarPermissao(treinoExercicio.getTreino(), logado);

        treinoexercicioRepository.delete(treinoExercicio);
    }

    private TreinoExercicioResponseDto toResponseDto(TreinoExercicio treinoExercicio) {
        return new TreinoExercicioResponseDto(
                treinoExercicio.getTreino().getId(),
                treinoExercicio.getTreino().getNome(),
                treinoExercicio.getExercicio().getId(),
                treinoExercicio.getExercicio().getNome(),
                treinoExercicio.getOrdem(),
                treinoExercicio.getCarga(),
                treinoExercicio.getSeries(),
                treinoExercicio.getRep(),
                treinoExercicio.getObs()
        );
    }

    private TreinoExercicio buscarTreinoExercicioPorId(TreinoExercicioId id) {
        return treinoexercicioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Exercicio no treino não encontrado"));
    }

    private User getUser(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    private Treino buscarTreinoAutorizado(Integer treinoId, User logado) {

        Treino treino = treinoRepository.findById(treinoId)
                .orElseThrow(() -> new NotFoundException("Treino não encontrado"));

        validarPermissao(treino, logado);

        return treino;
    }


}
