package com.wictor.service;

import com.wictor.annotation.Auditar;
import com.wictor.audit.AuditoriaContext;
import com.wictor.audit.AuditoriaInfo;
import com.wictor.dto.plano.PlanoDto;
import com.wictor.dto.plano.PlanoResponseDto;
import com.wictor.dto.plano.PlanoUpdateDto;
import com.wictor.enums.AcaoLog;
import com.wictor.exception.NotFoundException;
import com.wictor.integration.abacate.AbacateProdutoService;
import com.wictor.model.Plano;
import com.wictor.repository.PlanoRepository;
import com.wictor.security.CustomUserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PlanoService {

    private final PlanoRepository planoRepository;
    private final AbacateProdutoService abacateProdutoService;

    public PlanoService(
            PlanoRepository planoRepository,
            AbacateProdutoService abacateProdutoService
    ){
        this.planoRepository = planoRepository;
        this.abacateProdutoService = abacateProdutoService;
    }

    @Auditar(acao = AcaoLog.CADASTRO)
    @Transactional
    public PlanoResponseDto cadastrar(PlanoDto planoDTO) {

        boolean recorrente;

        if(planoDTO.recorrente() == null){

             recorrente = false;
        }else{

            recorrente = planoDTO.recorrente();
        }

        Plano plano = planoRepository.save(
                Plano.builder()
                        .nome(planoDTO.nome())
                        .valor(planoDTO.valor())
                        .ciclo(planoDTO.ciclo())
                        .recorrente(recorrente)
                        .ativo(true)
                        .build()
        );

        String productId = abacateProdutoService.criarProdutoPlano(plano);

        plano.setAbacateProductId(productId);

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

        if (dto.nome() != null && !dto.nome().isBlank()) {plano.setNome(dto.nome());}

        if (dto.valor() != null) {plano.setValor(dto.valor());}

        if (dto.ciclo() != null) {plano.setCiclo(dto.ciclo());}

        if (dto.recorrente() != null){plano.setRecorrente(dto.recorrente());}

        if (dto.ativo() != null) {plano.setAtivo(dto.ativo());}

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

        return planoRepository.findById(id).orElseThrow(() -> new NotFoundException("Plano não encontrado"));
    }

    private PlanoResponseDto toResponseDto(Plano plano) {
        return new PlanoResponseDto(
                plano.getId(),
                plano.getNome(),
                plano.getValor(),
                plano.getCiclo(),
                plano.isRecorrente()
        );
    }

    private Plano copiarPlano(Plano plano) {

        return Plano.builder()
                .id(plano.getId())
                .nome(plano.getNome())
                .valor(plano.getValor())
                .ciclo(plano.getCiclo())
                .recorrente(plano.isRecorrente())
                .ativo(plano.isAtivo())
                .build();
    }
}