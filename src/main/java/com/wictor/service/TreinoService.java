package com.wictor.service;

import com.wictor.annotation.Auditar;
import com.wictor.audit.AuditoriaContext;
import com.wictor.audit.AuditoriaInfo;
import com.wictor.dto.treino.TreinoDto;
import com.wictor.dto.treino.TreinoResponseDto;
import com.wictor.dto.treino.TreinoUpdateDto;
import com.wictor.enums.AcaoLog;
import com.wictor.enums.Role;
import com.wictor.exception.NotFoundException;
import com.wictor.model.*;
import com.wictor.repository.AlunoRepository;
import com.wictor.repository.TreinoRepository;
import com.wictor.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TreinoService {

    private final TreinoRepository treinoRepository;
    private final AlunoRepository alunoRepository;
    private final UserRepository userRepository;

    public TreinoService(
            TreinoRepository treinoRepository,
            AlunoRepository alunoRepository,
            UserRepository userRepository) {

        this.treinoRepository = treinoRepository;
        this.alunoRepository = alunoRepository;
        this.userRepository = userRepository;
    }

    @Auditar(acao = AcaoLog.CADASTRO)
    @Transactional
    public TreinoResponseDto cadastrar(TreinoDto treinoDTO, Integer userId) {

        Aluno aluno = obterAluno(treinoDTO, userId);

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

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .depois(treino)
                        .entidade("Treino")
                        .entidadeId(treino.getId())
                        .build()
        );

        return toResponseDto(treino);
    }

    @Auditar(acao = AcaoLog.ALTERACAO)
    @Transactional
    public TreinoResponseDto atualizar(Integer id, TreinoUpdateDto dto,  Integer userId) {

        boolean alterado = false;
        Treino treino = buscarTreinoPorId(id);
        Treino antes = copiarTreino(treino);
        validarPermissao(treino, userId);

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

        if (dto.ativo() != null && !dto.ativo().equals(treino.isAtivo())) {

            treino.setAtivo(dto.ativo());
            alterado = true;
        }

        if (alterado) {treino.setModificado(LocalDate.now());}

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .antes(antes)
                        .depois(treino)
                        .entidade("Treino")
                        .entidadeId(treino.getId())
                        .build()
        );

        return toResponseDto(treinoRepository.save(treino));
    }

    @Auditar(acao = AcaoLog.ALTERACAO)
    @Transactional
    public void desativar(Integer id, Integer userId) {

        Treino treino = buscarTreinoPorId(id);
        Treino antes = copiarTreino(treino);
        validarPermissao(treino, userId);
        treino.setAtivo(false);

        Treino depois = treinoRepository.save(treino);

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .antes(antes)
                        .depois(depois)
                        .entidade("Treino")
                        .entidadeId(id)
                        .build()
        );
    }

    @Auditar(acao = AcaoLog.ALTERACAO)
    @Transactional
    public void reativar(Integer id) {

        Treino treino = buscarTreinoPorId(id);
        Treino antes = copiarTreino(treino);
        treino.setAtivo(true);

        Treino depois = treinoRepository.save(treino);

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .antes(antes)
                        .depois(depois)
                        .entidade("Treino")
                        .entidadeId(id)
                        .build()
        );
    }

    @Auditar(acao = AcaoLog.EXCLUSAO)
    @Transactional
    public void deletar(Integer id) {

       Treino treino = buscarTreinoPorId(id);

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .antes(treino)
                        .entidade("Treino")
                        .entidadeId(treino.getId())
                        .build()
        );

        treinoRepository.delete(treino);
    }

    @Auditar(acao = AcaoLog.CONSULTA, descricao = "Listagem de treinos.")
    public List<TreinoResponseDto> listar() {

        return treinoRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Auditar(acao = AcaoLog.CONSULTA, descricao = "Consulta de treino por ID.")
    public TreinoResponseDto buscarPorId(Integer id, Integer userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        Treino treino = buscarTreinoPorId(id);

        if (user.getRole() == Role.ALUNO &&
                !treino.getAluno().getUser().getId().equals(userId)) {

            throw new AccessDeniedException("Você não tem permissão para acessar este treino.");
        }

        return toResponseDto(treino);
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

        return treinoRepository.findById(id).orElseThrow(() -> new NotFoundException("Treino não encontrado"));
    }

    private void validarPermissao(Treino treino, Integer userId) {

        User logado = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        boolean admin = logado.getRole().name().equals("ADMIN");

        boolean dono = treino.getAluno() != null &&
                treino.getAluno().getUser().getId().equals(userId);

        if (!admin && !dono) {throw new AccessDeniedException("Sem permissão");}
    }

    private Aluno obterAluno(TreinoDto dto, Integer userId) {

        User logado = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        boolean admin = logado.getRole().name().equals("ADMIN");

        if (admin && dto.alunoId() != null) {

            return alunoRepository.findById(dto.alunoId()).orElseThrow(() -> new NotFoundException("Aluno não encontrado"));
        }

        return logado.getAluno();
    }

    private Treino copiarTreino(Treino treino) {

        return Treino.builder()
                .id(treino.getId())
                .nome(treino.getNome())
                .ObjTreino(treino.getObjTreino())
                .inicio(treino.getInicio())
                .fim(treino.getFim())
                .criado(treino.getCriado())
                .modificado(treino.getModificado())
                .obs(treino.getObs())
                .ativo(treino.isAtivo())
                .aluno(treino.getAluno())
                .build();
    }
}