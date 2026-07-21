package com.wictor.integration.abacate.webhook;

import com.wictor.enums.FormaPagamento;
import com.wictor.enums.OrigemFinanceiro;
import com.wictor.enums.StatusPagamento;
import com.wictor.enums.TipoFinanceiro;
import com.wictor.exception.NotFoundException;
import com.wictor.exception.RegraException;
import com.wictor.integration.abacate.dto.WebhookRequest;
import com.wictor.model.Aluno;
import com.wictor.model.Financeiro;
import com.wictor.model.Pagamento;
import com.wictor.model.Plano;
import com.wictor.repository.AlunoRepository;
import com.wictor.repository.PagamentoRepository;
import com.wictor.repository.PlanoRepository;
import com.wictor.service.AlunoService;
import com.wictor.service.FinanceiroService;
import com.wictor.service.PagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AbacateWebhookService {

    private final PagamentoRepository pagamentoRepository;
    private final AlunoService alunoService;
    private final AlunoRepository alunoRepository;
    private final PlanoRepository planoRepository;
    private final FinanceiroService financeiroService;
    private final PagamentoService pagamentoService;

    @Transactional
    public void processar(WebhookRequest request) {

        switch (request.event()) {

            case "checkout.completed" -> processarCheckout(request);

            case "subscription.completed" -> processarAssinatura(request);

            case "subscription.renewed" -> processarRenovacao(request);

            case "subscription.cancelled" -> cancelarAssinatura(request);

            case "subscription.plan_changed" -> alterarPlanoWebhook(request);

            default -> {}
        }
    }

    private void processarCheckout(WebhookRequest request) {

        Pagamento pagamento = pagamentoRepository.findByExternalId(request.data().checkout().externalId())
                .orElseThrow(() -> new NotFoundException("Pagamento não encontrado"));

        if (!"PAID".equals(request.data().checkout().status())) {return;}

        if (pagamento.getStatus() == StatusPagamento.PAGO) {return;}

        pagamentoService.confirmarPagamento(pagamento, converterFormaPagamento(request.data().payerInformation().method()));

        financeiroService.registrarLancamento(
                Financeiro.builder()
                        .nome("Mensalidade - " + pagamento.getAluno().getUser().getNome())
                        .tipo(TipoFinanceiro.MENSALIDADE)
                        .origem(OrigemFinanceiro.PAGAMENTO_ONLINE)
                        .pagamento(pagamento)
                        .data(LocalDate.now())
                        .valor(pagamento.getPlano().getValor())
                        .build(),
                "Mensalidade registrada automaticamente pelo pagamento."
        );

        alunoService.ativarPlano(pagamento.getAluno(), pagamento.getPlano());
    }

    private void processarAssinatura(WebhookRequest request) {

        var subscription = request.data().subscription();
        var checkout = request.data().checkout();

        Pagamento pagamento = pagamentoRepository.findByExternalId(checkout.externalId()).orElseThrow(() ->
                                new NotFoundException("Pagamento inicial não encontrado"));

        pagamento.setAbacateSubscriptionId(subscription.id());

        pagamentoRepository.save(pagamento);

        pagamentoService.confirmarPagamento(pagamento, converterFormaPagamento(request.data().payerInformation().method()));

        if (pagamento.getFinanceiro() == null) {
            financeiroService.registrarLancamento(
                    Financeiro.builder()
                            .nome("Mensalidade - " + pagamento.getAluno().getUser().getNome())
                            .tipo(TipoFinanceiro.MENSALIDADE)
                            .origem(OrigemFinanceiro.PAGAMENTO_ONLINE)
                            .pagamento(pagamento)
                            .data(LocalDate.now())
                            .valor(pagamento.getPlano().getValor())
                            .build(),
                    "Mensalidade registrada automaticamente pelo pagamento."
            );
        }

        Aluno aluno = pagamento.getAluno();

        aluno.setAssinaturaAtiva(true);

        alunoRepository.save(aluno);
    }

    private void processarRenovacao(WebhookRequest request) {

        var checkout = request.data().checkout();

        if(pagamentoRepository.findByAbacateBillId(checkout.id()).isPresent()) {return;}

        Pagamento anterior = pagamentoRepository.findFirstByAbacateSubscriptionIdOrderByDataDesc(
                                request.data().subscription().id())
                        .orElseThrow(() -> new NotFoundException("Assinatura não encontrada"));

        Plano plano = buscarPlano(checkout.items().get(0).id());

        Pagamento pagamento = Pagamento.builder()
                .aluno(anterior.getAluno())
                .plano(plano)
                .abacateSubscriptionId(anterior.getAbacateSubscriptionId())
                .abacateBillId(checkout.id())
                .externalId(request.data().payment().externalId())
                .formaPagamento(converterFormaPagamento(request.data().payerInformation().method()))
                .status(StatusPagamento.PAGO)
                .data(LocalDateTime.now())
                .build();

        pagamentoService.registrarPagamento(pagamento, "Pagamento criado pela renovação da assinatura.");

        financeiroService.registrarLancamento(
                Financeiro.builder()
                        .nome("Mensalidade - " + pagamento.getAluno().getUser().getNome())
                        .tipo(TipoFinanceiro.MENSALIDADE)
                        .origem(OrigemFinanceiro.PAGAMENTO_ONLINE)
                        .pagamento(pagamento)
                        .data(LocalDate.now())
                        .valor(pagamento.getPlano().getValor())
                        .build(),
                "Mensalidade registrada automaticamente pelo pagamento."
        );

        alunoService.ativarPlano(pagamento.getAluno(), plano);
    }

    private void cancelarAssinatura(WebhookRequest request) {

        Pagamento pagamento = pagamentoRepository.findFirstByAbacateSubscriptionIdAndStatusOrderByDataDesc(
                                request.data().subscription().id(), StatusPagamento.PAGO)
                        .orElseThrow(() -> new NotFoundException("Pagamento ativo não encontrado"));

        pagamentoService.cancelarPagamento(pagamento);
    }

    private Plano buscarPlano(String productId) {

        return planoRepository.findByAbacateProductId(productId).orElseThrow(() -> new NotFoundException("Plano não encontrado."));
    }

    private void alterarPlanoWebhook(WebhookRequest request){

        if (!"APPLIED".equals(request.data().status())) {return;}

        Pagamento pagamento = pagamentoRepository.findFirstByAbacateSubscriptionIdOrderByDataDesc(
                                request.data().subscription().id()).orElseThrow(() ->
                                new NotFoundException("Assinatura não encontrada"));


        Plano novoPlano = buscarPlano(request.data().checkout().items().get(0).id());

        pagamentoService.atualizarPlanoPagamento(pagamento, novoPlano);

        Aluno aluno = pagamento.getAluno();

        aluno.setPlano(novoPlano);

        alunoRepository.save(aluno);
    }

    private FormaPagamento converterFormaPagamento(String metodo) {

        return switch (metodo) {
            case "PIX" -> FormaPagamento.PIX;
            case "CARD" -> FormaPagamento.CARTAO;
            default -> throw new RegraException("Forma de pagamento inválida.");
        };
    }
}