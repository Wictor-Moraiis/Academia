package com.wictor.service;

import com.wictor.annotation.Auditar;
import com.wictor.audit.AuditoriaContext;
import com.wictor.audit.AuditoriaInfo;
import com.wictor.dto.financeiro.FinanceiroDto;
import com.wictor.dto.financeiro.FinanceiroResponseDto;
import com.wictor.dto.financeiro.FinanceiroUpdateDto;
import com.wictor.dto.itemfinanceiro.ItemFinanceiroDto;
import com.wictor.dto.itemfinanceiro.ItemFinanceiroResponseDto;
import com.wictor.dto.venda.VendaDto;
import com.wictor.dto.venda.VendaResponseDto;
import com.wictor.enums.AcaoLog;
import com.wictor.enums.OrigemFinanceiro;
import com.wictor.enums.Role;
import com.wictor.enums.TipoFinanceiro;
import com.wictor.exception.NotFoundException;
import com.wictor.exception.RegraException;
import com.wictor.model.*;
import com.wictor.repository.FinanceiroRepository;
import com.wictor.repository.FuncionarioRepository;
import com.wictor.repository.ProdutoRepository;
import com.wictor.security.CustomUserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class FinanceiroService {

    private final FinanceiroRepository financeiroRepository;
    private final ProdutoRepository produtoRepository;
    private final FuncionarioRepository funcionarioRepository;

    public FinanceiroService(FinanceiroRepository financeiroRepository, ProdutoRepository produtoRepository, FuncionarioRepository funcionarioRepository
    ){
        this.financeiroRepository = financeiroRepository;
        this.produtoRepository = produtoRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    @Auditar(acao = AcaoLog.CADASTRO)
    @Transactional
    public FinanceiroResponseDto cadastrar(FinanceiroDto dto, CustomUserDetails user) {

        Funcionario funcionario = funcionarioRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("Funcionário não encontrado"));

        Financeiro financeiro = Financeiro.builder()
                .nome(dto.nome())
                .tipo(dto.tipo())
                .origem(OrigemFinanceiro.LANCAMENTO_MANUAL)
                .data(dto.data())
                .valor(dto.valor())
                .funcionario(funcionario)
                .build();

        return toResponseDto(registrarLancamento(financeiro, "Lançamento manual."));
    }

    @Auditar(acao = AcaoLog.CADASTRO)
    @Transactional
    public VendaResponseDto vender(VendaDto dto, CustomUserDetails user) {

        Funcionario funcionario = funcionarioRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("Funcionário não encontrado"));

        Financeiro financeiro = Financeiro.builder()
                .nome("Venda de Produtos")
                .tipo(TipoFinanceiro.VENDA)
                .origem(OrigemFinanceiro.VENDA_PRODUTO)
                .data(LocalDate.now())
                .funcionario(funcionario)
                .build();

        List<ItemFinanceiro> itens = new ArrayList<>();
        List<ItemFinanceiroResponseDto> itensResponse = new ArrayList<>();

        BigDecimal total = BigDecimal.ZERO;

        for (ItemFinanceiroDto itemDto : dto.itens()) {

            Produto produto = produtoRepository.findById(itemDto.produtoId())
                    .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

            if (produto.getQtd() < itemDto.qtd()) {

                throw new RegraException("Estoque insuficiente: " + produto.getNome());
            }

            BigDecimal valorUnitario = produto.getPreco();

            BigDecimal desconto = itemDto.desconto() != null ? itemDto.desconto() : BigDecimal.ZERO;

            BigDecimal subtotal = valorUnitario.multiply(BigDecimal.valueOf(itemDto.qtd())).subtract(desconto);

            total = total.add(subtotal);

            ItemFinanceiro item = ItemFinanceiro.builder()
                    .financeiro(financeiro)
                    .produto(produto)
                    .quantidade(itemDto.qtd())
                    .valorUnitario(valorUnitario)
                    .desconto(desconto)
                    .build();

            itens.add(item);

            itensResponse.add(new ItemFinanceiroResponseDto(
                    produto.getNome(),
                    itemDto.qtd(),
                    valorUnitario,
                    desconto,
                    subtotal
            ));

            produto.setQtd(produto.getQtd() - itemDto.qtd());
            produtoRepository.save(produto);
        }

        financeiro.setValor(total);
        financeiro.setItens(itens);

        Financeiro salvo = financeiroRepository.save(financeiro);

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .depois(salvo)
                        .entidade("Financeiro")
                        .entidadeId(salvo.getId())
                        .build()
        );

        return new VendaResponseDto(
                salvo.getId(),
                funcionario.getUser().getNome(),
                total,
                salvo.getData(),
                itensResponse
        );
    }

    @Auditar(acao = AcaoLog.CADASTRO)
    @Transactional
    public Financeiro registrarLancamento(Financeiro financeiro, String descricaoAuditoria) {

        Financeiro salvo = financeiroRepository.save(financeiro);

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .depois(salvo)
                        .entidade("Financeiro")
                        .entidadeId(salvo.getId())
                        .descricao(descricaoAuditoria)
                        .build()
        );

        return salvo;
    }

    @Auditar(acao = AcaoLog.ALTERACAO)
    @Transactional
    public FinanceiroResponseDto atualizar(Integer id, FinanceiroUpdateDto dto) {

        Financeiro financeiro = buscarFinanceiroPorId(id);
        Financeiro financeiroAntes = copiarFinanceiro(financeiro);

        if (financeiro.getTipo() == TipoFinanceiro.VENDA) {throw new RegraException("Vendas não podem ser alteradas");}

        if (dto.nome() != null && !dto.nome().isBlank()) {financeiro.setNome(dto.nome());}

        if (dto.tipo() != null) {financeiro.setTipo(dto.tipo());}

        if (dto.data() != null) {financeiro.setData(dto.data());}

        if (dto.valor() != null) {financeiro.setValor(dto.valor());}

        Financeiro financeiroSalvo = financeiroRepository.save(financeiro);

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .antes(financeiroAntes)
                        .depois(financeiroSalvo)
                        .entidade("Financeiro")
                        .entidadeId(financeiroSalvo.getId())
                        .build()
        );

        return toResponseDto(financeiro);
    }

    @Auditar(acao = AcaoLog.EXCLUSAO)
    @Transactional
    public void deletar(Integer id) {

        Financeiro financeiro = buscarFinanceiroPorId(id);

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .antes(financeiro)
                        .entidade("Financeiro")
                        .entidadeId(financeiro.getId())
                        .build()
        );

        financeiroRepository.delete(financeiro);
    }

    @Auditar(acao = AcaoLog.CONSULTA, descricao = "Listagem de atividades financeiras.")
    public List<FinanceiroResponseDto> listar(CustomUserDetails user) {

        return financeiroRepository.findAll()
                .stream()
                .filter(financeiro -> podeVisualizar(user, financeiro))
                .map(this::toResponseDto)
                .toList();
    }

    @Auditar(acao = AcaoLog.CONSULTA, descricao = "Consulta de atividade financeira por ID.")
    public FinanceiroResponseDto buscarPorId(Integer id, CustomUserDetails user){

        Financeiro financeiro = buscarFinanceiroPorId(id);

        if (!podeVisualizar(user, financeiro)) {
            throw new RegraException("Você não possui acesso a este lançamento.");
        }

        return toResponseDto(financeiro);
    }

    private Financeiro buscarFinanceiroPorId(Integer id) {
        return financeiroRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Atividade financeira não encontrada"));
    }

    private FinanceiroResponseDto toResponseDto(Financeiro financeiro) {
        return new FinanceiroResponseDto(
                financeiro.getId(),
                financeiro.getNome(),
                financeiro.getTipo(),
                financeiro.getOrigem(),
                financeiro.getData(),
                financeiro.getValor(),
                financeiro.getFuncionario() != null ? financeiro.getFuncionario().getUser().getNome() : null
        );
    }

    private boolean podeVisualizar(CustomUserDetails user, Financeiro financeiro) {

        if (user.getUser().getRole() != Role.RECEPCIONISTA) {
            return true;
        }

        return financeiro.getTipo().equals(TipoFinanceiro.MENSALIDADE)
                || financeiro.getTipo().equals(TipoFinanceiro.PAGAMENTO);
    }

    private Financeiro copiarFinanceiro(Financeiro financeiro) {

        return Financeiro.builder()
                .id(financeiro.getId())
                .nome(financeiro.getNome())
                .tipo(financeiro.getTipo())
                .data(financeiro.getData())
                .valor(financeiro.getValor())
                .funcionario(financeiro.getFuncionario())
                .build();
    }
}