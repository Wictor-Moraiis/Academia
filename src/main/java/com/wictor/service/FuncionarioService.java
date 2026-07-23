package com.wictor.service;

import com.wictor.annotation.Auditar;
import com.wictor.audit.AuditoriaContext;
import com.wictor.audit.AuditoriaInfo;
import com.wictor.dto.funcionario.FuncionarioDto;
import com.wictor.dto.funcionario.FuncionarioResponseDto;
import com.wictor.dto.funcionario.FuncionarioUpdateDto;
import com.wictor.dto.user.UserResponseDto;
import com.wictor.enums.AcaoLog;
import com.wictor.enums.Role;
import com.wictor.exception.*;
import com.wictor.model.Categoria;
import com.wictor.model.Funcionario;
import com.wictor.model.User;
import com.wictor.repository.CategoriaRepository;
import com.wictor.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.List;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final UserService userService;
    private final CategoriaRepository categoriaRepository;

    public FuncionarioService(FuncionarioRepository funcionarioRepository,
                              UserService userService,
                              CategoriaRepository categoriaRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.userService= userService;
        this.categoriaRepository = categoriaRepository;
    }

    @Auditar(acao = AcaoLog.CADASTRO)
    @Transactional
    public FuncionarioResponseDto cadastrar(FuncionarioDto dto, MultipartFile foto) {

        Categoria categoria = buscarCategoriaPorId(dto.categoriaId());
        Role role = categoria.getRole();

        User user = userService.cadastrar(dto.user(), foto, role);

        if (dto.cref() != null && !dto.cref().isBlank()) {validarFuncionarioPorCref(dto.cref());}

        if (dto.turnoIni() == null || dto.turnoFim() == null ||
                dto.turnoFim().isBefore(dto.turnoIni())) {
            throw new RegraException("Turno inválido");
        }

        Funcionario funcionario = funcionarioRepository.save(
                Funcionario.builder()
                        .user(user)
                        .cref(dto.cref() != null && !dto.cref().isBlank() ? dto.cref() : null)
                        .tipoContrato(dto.tipoContrato())
                        .turnoIni(dto.turnoIni())
                        .turnoFim(dto.turnoFim())
                        .banco(dto.banco())
                        .agencia(dto.agencia())
                        .conta(dto.conta())
                        .tipoConta(dto.tipoConta())
                        .categoria(categoria)
                        .build()
        );

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .depois(funcionario)
                        .entidade("Funcionario")
                        .entidadeId(funcionario.getId())
                        .build()
        );

        return toResponseDto(funcionario);
    }

    @Auditar(acao = AcaoLog.ALTERACAO)
    @Transactional
    public FuncionarioResponseDto atualizar(Integer id, FuncionarioUpdateDto dto) {

        Funcionario funcionario = buscarFuncionarioPorId(id);
        Funcionario antes = copiarFuncionario(funcionario);

        if (!funcionario.getUser().isAtivo()) {throw new RegraException("Usuário desativado");}

        if (dto.cref() != null && !dto.cref().isBlank()) {

            if (!dto.cref().equals(funcionario.getCref())) {

                validarFuncionarioPorCref(dto.cref());
            }
            funcionario.setCref(dto.cref());
        }

        if (dto.tipoContrato() != null) {funcionario.setTipoContrato(dto.tipoContrato());}

        LocalTime turnoIni = dto.turnoIni() != null ? dto.turnoIni() : funcionario.getTurnoIni();
        LocalTime turnoFim = dto.turnoFim() != null ? dto.turnoFim() : funcionario.getTurnoFim();

        if (turnoFim.isBefore(turnoIni)) {throw new RegraException("Turno inválido");}

        funcionario.setTurnoIni(turnoIni);
        funcionario.setTurnoFim(turnoFim);

        if (dto.banco() != null && !dto.banco().isBlank()) {funcionario.setBanco(dto.banco());}

        if (dto.agencia() != null && !dto.agencia().isBlank()) {funcionario.setAgencia(dto.agencia());}

        if (dto.conta() != null && !dto.conta().isBlank()) {funcionario.setConta(dto.conta());}

        if (dto.tipoConta() != null) {funcionario.setTipoConta(dto.tipoConta());}

        if (dto.categoriaId() != null) {

            Categoria categoria = buscarCategoriaPorId(dto.categoriaId());

            funcionario.setCategoria(categoria);

            sincronizarRole(funcionario.getUser(), categoria);
        }

        funcionarioRepository.save(funcionario);

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .antes(antes)
                        .depois(funcionario)
                        .entidade("Funcionario")
                        .entidadeId(funcionario.getId())
                        .build()
        );

        return toResponseDto(funcionario);
    }

    @Auditar(acao = AcaoLog.EXCLUSAO)
    @Transactional
    public void deletar(Integer id) {

        Funcionario funcionario = buscarFuncionarioPorId(id);

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .antes(funcionario)
                        .entidade("Funcionario")
                        .entidadeId(funcionario.getId())
                        .build()
        );

        funcionarioRepository.delete(funcionario);
    }

    @Auditar(acao = AcaoLog.CONSULTA, descricao = "Listagem de Funcionarios.")
    public List<FuncionarioResponseDto> listar() {
        return funcionarioRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Auditar(acao = AcaoLog.CONSULTA, descricao = "Consulta de Funcionario por ID.")
    public FuncionarioResponseDto buscarPorId(Integer id) {

        return toResponseDto(buscarFuncionarioPorId(id));
    }

    private Funcionario buscarFuncionarioPorId(Integer id) {

        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Funcionario não encontrado"));
    }

    private void validarFuncionarioPorCref(String cref) {

        if (funcionarioRepository.existsByCref(cref)) {

            throw new ConflitoException("Cref já cadastrado");
        }
    }

    private Categoria buscarCategoriaPorId(Integer id) {

        return categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));
    }

    private FuncionarioResponseDto toResponseDto(Funcionario funcionario) {

        return new FuncionarioResponseDto(
                new UserResponseDto(
                        funcionario.getUser().getId(),
                        funcionario.getUser().getNome(),
                        funcionario.getUser().getEmail1(),
                        funcionario.getUser().getEmail2(),
                        funcionario.getUser().getTel1(),
                        funcionario.getUser().getTel2(),
                        funcionario.getUser().getSexo(),
                        funcionario.getUser().getCep(),
                        funcionario.getUser().getBairro(),
                        funcionario.getUser().getRua(),
                        funcionario.getUser().getNumeroCasa(),
                        funcionario.getUser().getComp(),
                        funcionario.getUser().getDatanasc(),
                        funcionario.getUser().getRole()
                ),
                funcionario.getCref(),
                funcionario.getTipoContrato(),
                funcionario.getTurnoIni(),
                funcionario.getTurnoFim(),
                funcionario.getCategoria().getNome(),
                funcionario.getCategoria().getSalario()
        );
    }

    private void sincronizarRole(User user, Categoria categoria) {
        user.setRole(categoria.getRole());
    }

    private Funcionario copiarFuncionario(Funcionario funcionario) {

        return Funcionario.builder()
                .user(funcionario.getUser())
                .id(funcionario.getId())
                .cref(funcionario.getCref())
                .tipoContrato(funcionario.getTipoContrato())
                .turnoIni(funcionario.getTurnoIni())
                .turnoFim(funcionario.getTurnoFim())
                .banco(funcionario.getBanco())
                .agencia(funcionario.getAgencia())
                .conta(funcionario.getConta())
                .tipoConta(funcionario.getTipoConta())
                .categoria(funcionario.getCategoria())
                .build();
    }
}