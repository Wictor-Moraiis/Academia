package com.wictor.service;

import com.wictor.annotation.Auditar;
import com.wictor.audit.AuditoriaContext;
import com.wictor.audit.AuditoriaInfo;
import com.wictor.dto.plano.PlanoDto;
import com.wictor.dto.plano.PlanoResponseDto;
import com.wictor.dto.plano.PlanoUpdateDto;
import com.wictor.enums.AcaoLog;
import com.wictor.exception.NotFoundException;
import com.wictor.model.Maquina;
import com.wictor.model.Plano;
import com.wictor.repository.PlanoRepository;
import com.wictor.security.CustomUserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PlanoService {

    private final PlanoRepository planoRepository;

    public PlanoService(PlanoRepository planoRepository) {
        this.planoRepository = planoRepository;
    }

    @Auditar(acao = AcaoLog.CADASTRO)
    @Transactional
    public PlanoResponseDto cadastrar(PlanoDto planoDTO) {

        Plano plano = planoRepository.save(
                Plano.builder()
                        .nome(planoDTO.nome())
                        .valor(planoDTO.valor())
                        .validade(planoDTO.validade())
                        .ativo(true)
                        .build()
        );

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .depois(plano)
                        .entidade("Plano")
                        .entidadeId(plano.getId())
                        .build()
        );

        return toResponseDto(plano);

    }

    @Auditar(acao = AcaoLog.ALTERACAO)
    @Transactional
    public PlanoResponseDto atualizar(Integer id, PlanoUpdateDto dto) {

        Plano plano = buscarPlanoPorId(id);
        Plano antes = copiarPlano(plano);

        if (dto.nome() != null && !dto.nome().isBlank()) {
            plano.setNome(dto.nome());
        }

        if (dto.valor() != null) {
            plano.setValor(dto.valor());
        }

        if (dto.validade() != null) {
            plano.setValidade(dto.validade());
        }

        if (dto.ativo() != null) {
            plano.setAtivo(dto.ativo());
        }

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .antes(antes)
                        .depois(plano)
                        .entidade("Plano")
                        .entidadeId(plano.getId())
                        .build()
        );

        return toResponseDto(planoRepository.save(plano));
    }

    @Auditar(acao = AcaoLog.CONSULTA, descricao = "Listagem de planos.")
    public List<PlanoResponseDto> listar(CustomUserDetails user) {

        List<Plano> planos = planoRepository.findAll();

        if (user.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ALUNO"))) {

            planos = planos.stream()
                    .filter(Plano::isAtivo)
                    .toList();
        }

        return planos.stream()
                .map(this::toResponseDto)
                .toList();
    }

    private PlanoResponseDto toResponseDto(Plano plano) {
        return new PlanoResponseDto(
                plano.getId(),
                plano.getNome(),
                plano.getValor(),
                plano.getValidade()
        );
    }

    @Auditar(acao = AcaoLog.CONSULTA, descricao = "Consulta de plano por ID.")
    public PlanoResponseDto buscarPorId(Integer id, CustomUserDetails user) {

        Plano plano = buscarPlanoPorId(id);

        boolean isAluno = user.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ALUNO"));

        if (isAluno && !plano.isAtivo()) {
            throw new NotFoundException("Plano não encontrado");
        }

        return toResponseDto(plano);
    }

    private Plano buscarPlanoPorId(Integer id) {
        return planoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Plano não encontrado"));
    }

    @Auditar(acao = AcaoLog.ALTERACAO)
    @Transactional
    public void desativar(Integer id) {

        Plano plano = buscarPlanoPorId(id);
        Plano antes = copiarPlano(plano);
        plano.setAtivo(false);

        Plano depois = planoRepository.save(plano);

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .antes(antes)
                        .depois(depois)
                        .entidade("Plano")
                        .entidadeId(id)
                        .build()
        );
    }

    @Auditar(acao = AcaoLog.ALTERACAO)
    @Transactional
    public void reativar(Integer id) {

        Plano plano = buscarPlanoPorId(id);
        Plano antes = copiarPlano(plano);
        plano.setAtivo(true);

        Plano depois = planoRepository.save(plano);

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .antes(antes)
                        .depois(depois)
                        .entidade("Plano")
                        .entidadeId(id)
                        .build()
        );
    }

    @Auditar(acao = AcaoLog.EXCLUSAO)
    @Transactional
    public void deletar(Integer id) {

        Plano plano = buscarPlanoPorId(id);

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .antes(plano)
                        .entidade("Plano")
                        .entidadeId(plano.getId())
                        .build()
        );

        planoRepository.delete(plano);
    }

    private Plano copiarPlano(Plano plano) {

        return Plano.builder()
                .id(plano.getId())
                .nome(plano.getNome())
                .valor(plano.getValor())
                .validade(plano.getValidade())
                .ativo(plano.isAtivo())
                .build();
    }
}