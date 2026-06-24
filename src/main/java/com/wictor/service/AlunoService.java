package com.wictor.service;

import com.wictor.dto.aluno.AlunoAdminDto;
import com.wictor.dto.aluno.AlunoDto;
import com.wictor.dto.aluno.AlunoResponseDto;
import com.wictor.dto.aluno.AlunoUpdateDto;
import com.wictor.exception.ConflitoException;
import com.wictor.exception.NotFoundException;
import com.wictor.exception.RegraException;
import com.wictor.model.Aluno;
import com.wictor.model.Plano;
import com.wictor.model.User;
import com.wictor.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final UserRepository userRepository;
    private final PlanoRepository planoRepository;
    private final FuncionarioRepository funcionarioRepository;

    public AlunoService(AlunoRepository alunoRepository,
                              UserRepository userRepository,
                              PlanoRepository planoRepository,
                        FuncionarioRepository funcionarioRepository) {
        this.alunoRepository = alunoRepository;
        this.userRepository = userRepository;
        this.planoRepository = planoRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    @Transactional
    public AlunoResponseDto cadastrar(AlunoAdminDto dto) {

        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        AlunoDto alunoDto = new AlunoDto(
                dto.saude(),
                dto.obs(),
                dto.altura(),
                dto.peso(),
                dto.objetivo(),
                dto.planoId(),
                dto.vencimento(),
                dto.vencido()
        );

        return cadastrarInterno(user, alunoDto);
    }
    @Transactional
    private AlunoResponseDto cadastrarInterno(User user, AlunoDto dto) {

        if (alunoRepository.existsByUserId(user.getId())) {
            throw new ConflitoException("Usuário já é um aluno");
        }

        if (!user.isAtivo()) {
            throw new RegraException("Usuário desativado");
        }

        if (funcionarioRepository.existsByUserId(user.getId())) {
            throw new RegraException("Usuário já é um funcionário");
        }

        Plano plano = buscarPlanoPorId(dto.planoId());

        Aluno aluno = Aluno.builder()
                .user(user)
                .saude(dto.saude())
                .obs(dto.obs())
                .altura(dto.altura())
                .peso(dto.peso())
                .objetivo(dto.objetivo())
                .plano(plano)
                .vencimento(dto.vencimento())
                .vencido(dto.vencido())
                .build();

        aluno = alunoRepository.save(aluno);

        return toResponseDto(aluno);
    }
    public AlunoResponseDto cadastrarMe(AlunoDto dto, Integer userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        return cadastrarInterno(user, dto);
    }

    @Transactional
    public AlunoResponseDto atualizar(Integer id, AlunoUpdateDto dto) {
        Aluno aluno = buscarAlunoPorId(id);

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
            Plano plano = buscarPlanoPorId(dto.planoId());
            aluno.setPlano(plano);
        }

        if (dto.vencimento() != null) {
            aluno.setVencimento(dto.vencimento());
        }

        if (dto.vencido() != null) {
            aluno.setVencido(dto.vencido());
        }

        alunoRepository.save(aluno);

        return toResponseDto(aluno);
    }

    @Transactional
    public void deletar(Integer id) {

        Aluno aluno = buscarAlunoPorId(id);

        alunoRepository.delete(aluno);
    }

    public AlunoResponseDto buscarPorId(Integer id) {

        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado"));

        return toResponseDto(aluno);
    }

    public List<AlunoResponseDto> listar() {

        return alunoRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    private Aluno buscarAlunoPorId(Integer id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado"));
    }

    private Plano buscarPlanoPorId(Integer id) {
        return planoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Plano não encontrado"));
    }

    private AlunoResponseDto toResponseDto(Aluno aluno) {
        return new AlunoResponseDto(
                aluno.getId(),
                aluno.getUser().getNome(),
                aluno.getAltura(),
                aluno.getPeso(),
                aluno.getObjetivo(),
                aluno.getPlano().getNome(),
                aluno.getVencimento(),
                aluno.isVencido()
        );
    }
}
