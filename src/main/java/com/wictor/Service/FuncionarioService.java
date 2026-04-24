package com.wictor.Service;

import com.wictor.Dto.AlunoResponseDto;
import com.wictor.Dto.FuncionarioDto;
import com.wictor.Dto.FuncionarioResponseDto;
import com.wictor.Dto.FuncionarioUpdateDto;
import com.wictor.exception.*;
import com.wictor.model.Categoria;
import com.wictor.model.Funcionario;
import com.wictor.model.User;
import com.wictor.repository.AlunoRepository;
import com.wictor.repository.CategoriaRepository;
import com.wictor.repository.FuncionarioRepository;
import com.wictor.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class FuncionarioService {

    private final FuncionarioRepository repository;
    private final UserRepository userRepository;
    private final CategoriaRepository categoriaRepository;
    private final AlunoRepository alunoRepository;

    public FuncionarioService(FuncionarioRepository repository,
                              UserRepository userRepository,
                              CategoriaRepository categoriaRepository,
                              AlunoRepository alunoRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.categoriaRepository = categoriaRepository;
        this.alunoRepository = alunoRepository;
    }

    @Transactional
    public Funcionario cadastrar(FuncionarioDto dto) {

        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
        if (repository.existsByUserId(user.getId())) {
            throw new ConflitoException("Usuário já é um funcionário");
        }
        if (!user.isAtivo()) {
            throw new RegraException("Usuário desativado");
        }
        if (alunoRepository.existsByUserId(user.getId())) {
            throw new RegraException("Usuário já é aluno");
        }
        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));
        if (dto.cref() != null && !dto.cref().isBlank() && repository.existsByCref(dto.cref())) {
            throw new ConflitoException("Cref já cadastrado");
        }
        if (dto.turnoIni() == null || dto.turnoFim() == null ||
                dto.turnoFim().isBefore(dto.turnoIni())) {
            throw new RegraException("Turno inválido");
        }
        Funcionario.FuncionarioBuilder builder = Funcionario.builder()
                .user(user)
                .cref(dto.cref() != null && !dto.cref().isBlank() ? dto.cref() : null)
                .tipoContrato(dto.tipoContrato())
                .turnoIni(dto.turnoIni())
                .turnoFim(dto.turnoFim())
                .banco(dto.banco())
                .agencia(dto.agencia())
                .conta(dto.conta())
                .tipoConta(dto.tipoConta())
                .categoria(categoria);
        Funcionario funcionario = builder.build();

        return repository.save(funcionario);
    }

    @Transactional
    public FuncionarioResponseDto atualizar(Integer id, FuncionarioUpdateDto dto) {
        Funcionario funcionario = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Funcionário não encontrado"));

        if (!funcionario.getUser().isAtivo()) {
            throw new RegraException("Usuário desativado");
        }

        if (dto.cref() != null && !dto.cref().isBlank()) {

            if (repository.existsByCref(dto.cref()) &&
                    !funcionario.getCref().equals(dto.cref())) {
                throw new ConflitoException("Cref já cadastrado");
            }
            funcionario.setCref(dto.cref());
        }

        if (dto.tipoContrato() != null) {
            funcionario.setTipoContrato(dto.tipoContrato());
        }

        LocalTime turnoIni = dto.turnoIni() != null ? dto.turnoIni() : funcionario.getTurnoIni();
        LocalTime turnoFim = dto.turnoFim() != null ? dto.turnoFim() : funcionario.getTurnoFim();

        if (turnoFim.isBefore(turnoIni)) {
            throw new RegraException("Turno inválido");
        }

        funcionario.setTurnoIni(turnoIni);
        funcionario.setTurnoFim(turnoFim);

        if (dto.banco() != null && !dto.banco().isBlank()) {
            funcionario.setBanco(dto.banco());
        }

        if (dto.agencia() != null && !dto.agencia().isBlank()) {
            funcionario.setAgencia(dto.agencia());
        }

        if (dto.conta() != null && !dto.conta().isBlank()) {
            funcionario.setConta(dto.conta());
        }

        if (dto.tipoConta() != null && !dto.tipoConta().isBlank()) {
            funcionario.setTipoConta(dto.tipoConta());
        }

        if (dto.categoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                    .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));
            funcionario.setCategoria(categoria);
        }

        repository.save(funcionario);

        return repository.buscarFuncionarioCompleto(funcionario.getId());

    }

    @Transactional
    public void deletar(Integer id) {

        Funcionario funcionario = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Funcionário não encontrado"));

        repository.delete(funcionario);
    }
}