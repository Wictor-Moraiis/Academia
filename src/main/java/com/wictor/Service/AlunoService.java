package com.wictor.Service;

import com.wictor.Dto.AlunoDto;
import com.wictor.Dto.AlunoResponseDto;
import com.wictor.Dto.AlunoUpdateDto;
import com.wictor.exception.ConflitoException;
import com.wictor.exception.NotFoundException;
import com.wictor.exception.RegraException;
import com.wictor.model.Aluno;
import com.wictor.model.Plano;
import com.wictor.model.User;
import com.wictor.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AlunoService {

    private final AlunoRepository repository;
    private final UserRepository userRepository;
    private final PlanoRepository planoRepository;
    private final FuncionarioRepository funcionarioRepository;

    public AlunoService(AlunoRepository repository,
                              UserRepository userRepository,
                              PlanoRepository planoRepository,
                        FuncionarioRepository funcionarioRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.planoRepository = planoRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    @Transactional
    public Aluno cadastrar(AlunoDto dto) {

        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
        if (repository.existsByUserId(user.getId())) {
            throw new ConflitoException("Usuário já é um aluno");
        }
        if (!user.isAtivo()) {
            throw new RegraException("Usuário desativado");
        }
        if (funcionarioRepository.existsByUserId(user.getId())) {
        throw new RegraException("Usuário já é um funcionário");
        }
        Plano plano = planoRepository.findById(dto.planoId())
                .orElseThrow(() -> new NotFoundException("Plano não encontrado"));

        Aluno.AlunoBuilder builder = Aluno.builder()
                .user(user)
                .saude(dto.saude())
                .obs(dto.obs())
                .altura(dto.altura())
                .peso(dto.peso())
                .objetivo(dto.objetivo())
                .plano(plano)
                .vencimento(dto.vencimento())
                .vencido(dto.vencido());
        Aluno aluno = builder.build();

        return repository.save(aluno);
    }

    @Transactional
    public AlunoResponseDto atualizar(Integer id, AlunoUpdateDto dto) {
        Aluno aluno = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado"));

        if (!aluno.getUser().isAtivo()) {
            throw new RegraException("Usuário desativado");
        }

        if (dto.saude() != null && !dto.saude().isBlank()) {
            aluno.setSaude(dto.saude());
        }

        if (dto.obs() != null && !dto.obs().isBlank()) {
            aluno.setObs(dto.obs());
        }

        if (dto.altura() != null) {
            aluno.setAltura(dto.altura());
        }

        if (dto.peso() != null) {
            aluno.setPeso(dto.peso());
        }

        if (dto.objetivo() != null && !dto.objetivo().isBlank()) {
            aluno.setObjetivo(dto.objetivo());
        }

        if (dto.planoId() != null) {
            Plano plano = planoRepository.findById(dto.planoId())
                    .orElseThrow(() -> new NotFoundException("Plano não encontrado"));
            aluno.setPlano(plano);
        }

        if (dto.vencimento() != null) {
            aluno.setVencimento(dto.vencimento());
        }

        if (dto.vencido() != null) {
            aluno.setVencido(dto.vencido());
        }

        repository.save(aluno);

        return repository.buscarAlunoCompleto(aluno.getId());
    }

    @Transactional
    public void deletar(Integer id) {

        Aluno aluno = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado"));

        repository.delete(aluno);
    }
}
