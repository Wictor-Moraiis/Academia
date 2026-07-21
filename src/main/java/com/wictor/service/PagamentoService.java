package com.wictor.service;

import com.wictor.annotation.Auditar;
import com.wictor.audit.AuditoriaContext;
import com.wictor.audit.AuditoriaInfo;
import com.wictor.dto.pagamento.AlterarPlanoResponseDto;
import com.wictor.dto.pagamento.CheckoutPagamentoResponseDto;
import com.wictor.dto.pagamento.PagamentoPresencialDto;
import com.wictor.dto.pagamento.PagamentoResponseDto;
import com.wictor.enums.*;
import com.wictor.exception.NotFoundException;
import com.wictor.exception.RegraException;
import com.wictor.gateway.PagamentoGateway;
import com.wictor.integration.abacate.AbacateCustomerService;
import com.wictor.model.*;
import com.wictor.repository.AlunoRepository;
import com.wictor.repository.FuncionarioRepository;
import com.wictor.repository.PagamentoRepository;
import com.wictor.repository.PlanoRepository;
import com.wictor.security.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PlanoRepository planoRepository;
    private final AlunoRepository alunoRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final FinanceiroService financeiroService;
    private final AlunoService alunoService;
    private final PagamentoRepository pagamentoRepository;
    private final PagamentoGateway pagamentoGateway;
    private final AbacateCustomerService customerService;

    @Transactional
    public PagamentoResponseDto pagamentoPresencial(PagamentoPresencialDto dto, CustomUserDetails user
    ){

        Aluno aluno = alunoRepository.findById(dto.alunoId())
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado."));

        Plano plano = planoRepository.findById(dto.planoId())
                .orElseThrow(() -> new NotFoundException("Plano não encontrado."));

        Funcionario funcionario = funcionarioRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("Funcionário não encontrado."));

        Pagamento pagamento = Pagamento.builder()
                .aluno(aluno)
                .plano(plano)
                .status(StatusPagamento.PAGO)
                .data(LocalDateTime.now())
                .build();

        Pagamento salvo = registrarPagamento(pagamento, "Pagamento registrado presencialmente.");

        financeiroService.registrarLancamento(
                Financeiro.builder()
                        .nome("Mensalidade - " + aluno.getUser().getNome())
                        .tipo(TipoFinanceiro.MENSALIDADE)
                        .origem(OrigemFinanceiro.PAGAMENTO_PRESENCIAL)
                        .pagamento(salvo)
                        .funcionario(funcionario)
                        .data(LocalDate.now())
                        .valor(plano.getValor())
                        .build(),
                "Mensalidade registrada presencialmente."
        );

        alunoService.ativarPlano(aluno, plano);

        return toResponseDto(salvo);
    }

    @Transactional
    public CheckoutPagamentoResponseDto checkout(Integer planoId, CustomUserDetails user) {

        String externalId = UUID.randomUUID().toString();

        Plano plano = planoRepository.findById(planoId)
                .orElseThrow(() -> new EntityNotFoundException("Plano não encontrado."));

        Aluno aluno = alunoRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado."));

        if (aluno.getAbacateCustomerId() == null) {

            String customerId = customerService.criarCustomer(aluno);

            aluno.setAbacateCustomerId(customerId);

            alunoRepository.save(aluno);
        }

        CheckoutPagamentoResponseDto response = plano.isRecorrente()
                        ? pagamentoGateway.criarAssinatura(plano, aluno, externalId)
                        : pagamentoGateway.criarCheckout(plano, aluno, externalId);

        criarPagamentoPendente(aluno, plano, response, externalId);

        return response;
    }

    private void criarPagamentoPendente(Aluno aluno, Plano plano, CheckoutPagamentoResponseDto response,
                                        String externalId) {

        Pagamento pagamento = Pagamento.builder()
                .aluno(aluno)
                .plano(plano)
                .externalId(externalId)
                .abacateBillId(response.id())
                .status(StatusPagamento.PENDENTE)
                .data(LocalDateTime.now())
                .build();

        if (response.recorrente()) {pagamento.setAbacateSubscriptionId(response.id());}

        pagamentoRepository.save(pagamento);
    }

    public void cancelarAssinatura(CustomUserDetails user) {

        Pagamento pagamento = pagamentoRepository
                .findFirstByAlunoIdAndAbacateSubscriptionIdIsNotNullOrderByDataDesc(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Assinatura não encontrada."));

        pagamentoGateway.cancelarAssinatura(pagamento.getAbacateSubscriptionId());
    }

    public AlterarPlanoResponseDto alterarPlano(Integer planoId, CustomUserDetails user) {

        Plano novoPlano = planoRepository.findById(planoId)
                .orElseThrow(() -> new EntityNotFoundException("Plano não encontrado."));

        if (!novoPlano.isRecorrente()) {

            throw new IllegalArgumentException("Só é possível alterar para um plano recorrente.");
        }

        Pagamento pagamento = pagamentoRepository.findFirstByAlunoIdAndStatusOrderByDataDesc(
                        user.getId(), StatusPagamento.PAGO).orElseThrow(() ->
                        new EntityNotFoundException("Assinatura ativa não encontrada."));

        if (pagamento.getPlano().getId().equals(novoPlano.getId())) {

            throw new RegraException("O aluno já possui esse plano.");
        }

        return pagamentoGateway.alterarPlano(pagamento.getAbacateSubscriptionId(), novoPlano);
    }

    @Auditar(acao = AcaoLog.ALTERACAO)
    @Transactional
    public void confirmarPagamento(Pagamento pagamento, FormaPagamento formaPagamento){

        Pagamento antes = copiarPagamento(pagamento);
        pagamento.setStatus(StatusPagamento.PAGO);
        pagamento.setFormaPagamento(formaPagamento);

        Pagamento salvo = pagamentoRepository.save(pagamento);

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .antes(antes)
                        .depois(salvo)
                        .entidade("Pagamento")
                        .entidadeId(salvo.getId())
                        .descricao("Pagamento aprovado pela AbacatePay")
                        .build()
        );
    }

    @Auditar(acao = AcaoLog.ALTERACAO)
    @Transactional
    public void cancelarPagamento(Pagamento pagamento){

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .depois(pagamento)
                        .entidade("Pagamento")
                        .entidadeId(pagamento.getId())
                        .descricao(
                                "Assinatura cancelada pela AbacatePay"
                        )
                        .build()
        );
    }

    @Auditar(acao = AcaoLog.ALTERACAO)
    @Transactional
    public void atualizarPlanoPagamento(Pagamento pagamento, Plano novoPlano){

        Pagamento antes = copiarPagamento(pagamento);

        pagamento.setPlano(novoPlano);

        Pagamento salvo = pagamentoRepository.save(pagamento);


        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .antes(antes)
                        .depois(salvo)
                        .entidade("Pagamento")
                        .entidadeId(salvo.getId())
                        .descricao(
                                "Plano alterado pela AbacatePay"
                        )
                        .build()
        );
    }

    @Auditar(acao = AcaoLog.CADASTRO)
    @Transactional
    public Pagamento registrarPagamento(Pagamento pagamento, String descricao){

        Pagamento salvo = pagamentoRepository.save(pagamento);

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .depois(salvo)
                        .entidade("Pagamento")
                        .entidadeId(salvo.getId())
                        .descricao(descricao)
                        .build()
        );

        return salvo;
    }

    @Auditar(acao = AcaoLog.CONSULTA, descricao = "Listagem de pagamentos.")
    @Transactional(readOnly = true)
    public List<PagamentoResponseDto> listar(CustomUserDetails user) {

        boolean isAluno = user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ALUNO"));

        List<Pagamento> pagamentos = isAluno
                ? pagamentoRepository.findByAlunoIdOrderByDataDesc(user.getId())
                : pagamentoRepository.findAll();

        return pagamentos.stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Auditar(acao = AcaoLog.CONSULTA, descricao = "Consulta de pagamento por ID.")
    @Transactional(readOnly = true)
    public PagamentoResponseDto buscarPorId(Integer id, CustomUserDetails user) {

        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pagamento não encontrado."));

        boolean isAluno = user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ALUNO"));

        if (isAluno && !pagamento.getAluno().getId().equals(user.getId())) {

            throw new RegraException("Você não tem permissão para visualizar este pagamento.");
        }

        return toResponseDto(pagamento);
    }

    private Pagamento copiarPagamento(Pagamento pagamento) {

        return Pagamento.builder()
                .id(pagamento.getId())
                .aluno(pagamento.getAluno())
                .plano(pagamento.getPlano())
                .formaPagamento(pagamento.getFormaPagamento())
                .externalId(pagamento.getExternalId())
                .abacateBillId(pagamento.getAbacateBillId())
                .abacateSubscriptionId(pagamento.getAbacateSubscriptionId())
                .status(pagamento.getStatus())
                .data(pagamento.getData())
                .build();
    }

    private PagamentoResponseDto toResponseDto(Pagamento pagamento) {

        return new PagamentoResponseDto(
                pagamento.getId(),
                pagamento.getPlano().getNome(),
                pagamento.getPlano().getValor(),
                pagamento.getFormaPagamento(),
                pagamento.getStatus(),
                pagamento.getData()
        );
    }
}