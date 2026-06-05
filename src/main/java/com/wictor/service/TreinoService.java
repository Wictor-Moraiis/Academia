package com.wictor.service;

import com.wictor.dto.treino.TreinoDto;
import com.wictor.dto.treinoexercicio.TreinoResponseDto;
import com.wictor.dto.treinoexercicio.TreinoUpdateDto;
import com.wictor.exception.NotFoundException;
import com.wictor.model.Aluno;
import com.wictor.model.Treino;
import com.wictor.model.User;
import com.wictor.repository.AlunoRepository;
import com.wictor.repository.TreinoRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.util.List;

@Service
public class TreinoService {

    private final TreinoRepository repository;
    private final AlunoRepository alunoRepository;

    public TreinoService(
            TreinoRepository repository,
            AlunoRepository alunoRepository) {
        this.repository = repository;
        this.alunoRepository = alunoRepository;

    }

    public Treino cadastrar(TreinoDto treinoDTO, User logado) {

        Aluno aluno = null;

        boolean admin = logado.getRole().name().equals("ADMIN");

        if (admin) {

            if (treinoDTO.alunoId() != null) {

                aluno = alunoRepository.findById(treinoDTO.alunoId())
                        .orElseThrow(() ->
                                new NotFoundException("Aluno não encontrado"));
            }
        } else {

            aluno = logado.getAluno();
        }
        LocalDate criado;
        LocalDate modificado;
        Boolean ativo;

        if (treinoDTO.criado() == null) {
            criado = LocalDate.now();
        } else {
            criado = treinoDTO.criado();
        }
        if (treinoDTO.modificado() == null) {
            modificado = treinoDTO.criado();
        } else {
            modificado = treinoDTO.modificado();
        }
        if (treinoDTO.ativo() == null) {
            ativo = true;
        } else {
            ativo = treinoDTO.ativo();
        }

        Treino.TreinoBuilder builder = Treino.builder()
                .nome(treinoDTO.nome())
                .ObjTreino(treinoDTO.ObjTreino())
                .inicio(treinoDTO.inicio())
                .fim(treinoDTO.fim())
                .criado(criado)
                .modificado(modificado)
                .obs(treinoDTO.obs())
                .inicio(treinoDTO.inicio())
                .ativo(ativo)
                .aluno(aluno);

        Treino treino = builder.build();

        return repository.save(treino);
    }

    public Treino atualizar(Integer id, TreinoUpdateDto dto,  User logado) {

        boolean alterado = false;
        Treino treino = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Treino não encontrado"));
        boolean admin = logado.getRole().name().equals("ADMIN");

        boolean dono = treino.getAluno() != null && logado.getAluno() != null && treino.getAluno().getId().equals(logado.getAluno().getId());

        if (!admin && !dono) {
            throw new AccessDeniedException("Sem permissão");
        }
        if (dto.nome() != null && !dto.nome().isBlank()) {
            treino.setNome(dto.nome());
            alterado = true;
        }

        if (dto.ObjTreino() != null) {
            treino.setObjTreino(dto.ObjTreino());
            alterado = true;
        }

        if (dto.inicio() != null && !dto.inicio().equals(treino.getInicio())) {
            treino.setInicio(dto.inicio());
            alterado = true;
        }

        if (dto.fim() != null && !dto.fim().equals(treino.getFim())) {
            treino.setFim(dto.fim());
            alterado = true;
        }

        if (dto.criado() != null && !dto.criado().equals(treino.getCriado())) {
            treino.setCriado(dto.criado());
            alterado = true;
        }

        if (dto.modificado() != null && !dto.modificado().equals(treino.getModificado())) {
            treino.setModificado(dto.modificado());
            alterado = true;
        }

        if (dto.obs() != null && !dto.obs().isBlank()) {
            treino.setObs(dto.obs());
            alterado = true;
        }

        if (dto.ativo() != null) {
            treino.setAtivo(dto.ativo());
        }

        if (alterado) {
            treino.setModificado(LocalDate.now());
        }

        return repository.save(treino);
    }

    public List<TreinoResponseDto> listar() {
        return repository.findAll()
                .stream()
                .map(t -> new TreinoResponseDto(
                        t.getId(),
                        t.getNome(),
                        t.getObjTreino(),
                        t.getInicio(),
                        t.getFim(),
                        t.getCriado(),
                        t.getModificado(),
                        t.isAtivo(),
                        t.getAluno() != null ? t.getAluno().getUser().getNome() : null
                ))
                .toList();
    }

    public Treino buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Treino não encontrado"));
    }

    public void desativar(Integer id, User logado) {

        Treino treino = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Treino não encontrado"));

        boolean admin = logado.getRole().name().equals("ADMIN");

        boolean dono = treino.getAluno() != null && logado.getAluno() != null && treino.getAluno().getId().equals(logado.getAluno().getId());

        if (!admin && !dono) {
            throw new AccessDeniedException("Sem permissão");
        }

        treino.setAtivo(false);
        treino.setAluno(null);

        repository.save(treino);
    }

    public void reativar(Integer id) {

        Treino treino = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Treino não encontrado"));

        treino.setAtivo(true);

        repository.save(treino);
    }

    public void deletar(Integer id) {

        if (!repository.existsById(id)) {
            throw new NotFoundException("Treino não encontrado");
        }
        repository.deleteById(id);
    }
}