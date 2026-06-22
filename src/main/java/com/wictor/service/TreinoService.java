package com.wictor.service;

import com.wictor.dto.treino.TreinoDto;
import com.wictor.dto.treino.TreinoResponseDto;
import com.wictor.dto.treino.TreinoUpdateDto;
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

    private final TreinoRepository treinoRepository;
    private final AlunoRepository alunoRepository;

    public TreinoService(
            TreinoRepository treinoRepository,
            AlunoRepository alunoRepository) {
        this.treinoRepository = treinoRepository;
        this.alunoRepository = alunoRepository;

    }

    public TreinoResponseDto cadastrar(TreinoDto treinoDTO, User logado) {

        Aluno aluno = obterAluno(treinoDTO, logado);

        LocalDate criado;
        LocalDate modificado;
        Boolean ativo;

        if (treinoDTO.criado() == null) {
            criado = LocalDate.now();
        } else {
            criado = treinoDTO.criado();
        }
        if (treinoDTO.modificado() == null) {
            modificado = criado;
        } else {
            modificado = treinoDTO.modificado();
        }
        if (treinoDTO.ativo() == null) {
            ativo = true;
        } else {
            ativo = treinoDTO.ativo();
        }
        Treino treino = treinoRepository.save(
                Treino.builder()
                        .nome(treinoDTO.nome())
                        .ObjTreino(treinoDTO.ObjTreino())
                        .inicio(treinoDTO.inicio())
                        .fim(treinoDTO.fim())
                        .criado(criado)
                        .modificado(modificado)
                        .obs(treinoDTO.obs())
                        .ativo(ativo)
                        .aluno(aluno)
                        .build()
        );

        return toResponseDto(treino);

    }

    public TreinoResponseDto atualizar(Integer id, TreinoUpdateDto dto,  User logado) {

        boolean alterado = false;
        Treino treino = buscarTreinoPorId(id);
        validarPermissao(treino, logado);

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

        if (dto.ativo() != null &&
                !dto.ativo().equals(treino.isAtivo())) {

            treino.setAtivo(dto.ativo());
            alterado = true;
        }

        if (alterado) {
            treino.setModificado(LocalDate.now());
        }

        return toResponseDto(treinoRepository.save(treino));
    }

    public List<TreinoResponseDto> listar() {
        return treinoRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public TreinoResponseDto buscarPorId(Integer id) {
       return toResponseDto(buscarTreinoPorId(id));
    }

    public void desativar(Integer id, User logado) {

        Treino treino = buscarTreinoPorId(id);

        validarPermissao(treino, logado);

        alterarStatus(id, false);
    }

    public void reativar(Integer id) {
        alterarStatus(id, true);
    }

    private void alterarStatus(Integer id, boolean ativo) {
        Treino treino = buscarTreinoPorId(id);

        treino.setAtivo(ativo);

        treinoRepository.save(treino);
    }

    public void deletar(Integer id) {

       Treino treino = buscarTreinoPorId(id);

        treinoRepository.delete(treino);
    }

    private TreinoResponseDto toResponseDto(Treino treino) {
        return new TreinoResponseDto(
                treino.getId(),
                treino.getNome(),
                treino.getObjTreino(),
                treino.getInicio(),
                treino.getFim(),
                treino.getCriado(),
                treino.getModificado(),
                treino.isAtivo(),
                treino.getAluno() != null ? treino.getAluno().getUser().getNome() : null
        );
    }

    private Treino buscarTreinoPorId(Integer id) {
        return treinoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Treino não encontrado"));
    }

    private void validarPermissao(Treino treino, User logado) {

        boolean admin = logado.getRole().name().equals("ADMIN");

        boolean dono =
                treino.getAluno() != null &&
                        logado.getAluno() != null &&
                        treino.getAluno().getId().equals(logado.getAluno().getId());

        if (!admin && !dono) {
            throw new AccessDeniedException("Sem permissão");
        }
    }

    private Aluno obterAluno(TreinoDto dto, User logado) {

        boolean admin = logado.getRole().name().equals("ADMIN");

        if (admin && dto.alunoId() != null) {
            return alunoRepository.findById(dto.alunoId())
                    .orElseThrow(() ->
                            new NotFoundException("Aluno não encontrado"));
        }

        return logado.getAluno();
    }
}