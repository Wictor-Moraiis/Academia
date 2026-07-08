package com.wictor.service;

import com.wictor.dto.financeiro.FinanceiroDto;
import com.wictor.dto.financeiro.FinanceiroResponseDto;
import com.wictor.dto.financeiro.FinanceiroUpdateDto;
import com.wictor.dto.itemfinanceiro.ItemFinanceiroDto;
import com.wictor.dto.itemfinanceiro.ItemFinanceiroResponseDto;
import com.wictor.dto.venda.VendaDto;
import com.wictor.dto.venda.VendaResponseDto;
import com.wictor.enums.Role;
import com.wictor.enums.TipoFinanceiro;
import com.wictor.exception.NotFoundException;
import com.wictor.exception.RegraException;
import com.wictor.model.Financeiro;
import com.wictor.model.Funcionario;
import com.wictor.model.ItemFinanceiro;
import com.wictor.model.Produto;
import com.wictor.repository.FinanceiroRepository;
import com.wictor.repository.FuncionarioRepository;
import com.wictor.repository.ProdutoRepository;
import com.wictor.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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

    public FinanceiroResponseDto cadastrar(FinanceiroDto financeiroDTO, CustomUserDetails user) {

        Funcionario funcionario = funcionarioRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("Funcionário não encontrado"));

        Financeiro financeiro = financeiroRepository.save(
                Financeiro.builder()
                        .nome(financeiroDTO.nome())
                        .tipo(financeiroDTO.tipo())
                        .data(financeiroDTO.data())
                        .valor(financeiroDTO.valor())
                        .funcionario(funcionario)
                        .build()
        );

        return toResponseDto(financeiro);
    }

    @Transactional
    public VendaResponseDto vender(VendaDto dto, CustomUserDetails user) {

        Funcionario funcionario = funcionarioRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("Funcionário não encontrado"));

        Financeiro financeiro = Financeiro.builder()
                .nome("Venda de Produtos")
                .tipo(TipoFinanceiro.VENDA)
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

            BigDecimal desconto = itemDto.desconto() != null
                    ? itemDto.desconto()
                    : BigDecimal.ZERO;

            BigDecimal subtotal = valorUnitario
                    .multiply(BigDecimal.valueOf(itemDto.qtd()))
                    .subtract(desconto);

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

        return new VendaResponseDto(
                salvo.getId(),
                funcionario.getUser().getNome(),
                total,
                salvo.getData(),
                itensResponse
        );
    }

    @Transactional
    public FinanceiroResponseDto atualizar(Integer id, FinanceiroUpdateDto dto) {
        Financeiro financeiro = buscarFinanceiroPorId(id);

        if (dto.nome() != null && !dto.nome().isBlank()) {
            financeiro.setNome(dto.nome());
        }

        if (dto.tipo() != null) {
            financeiro.setTipo(dto.tipo());
        }

        if (dto.data() != null) {
            financeiro.setData(dto.data());
        }

        if (dto.valor() != null) {
            financeiro.setValor(dto.valor());
        }

        financeiroRepository.save(financeiro);

        return toResponseDto(financeiro);
    }

    public List<FinanceiroResponseDto> listar(CustomUserDetails user) {

        return financeiroRepository.findAll()
                .stream()
                .filter(financeiro -> podeVisualizar(user, financeiro))
                .map(this::toResponseDto)
                .toList();
    }

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

    public void deletar(Integer id) {

        Financeiro financeiro = buscarFinanceiroPorId(id);

        financeiroRepository.delete(financeiro);
    }

    private FinanceiroResponseDto toResponseDto(Financeiro financeiro) {
        return new FinanceiroResponseDto(
                financeiro.getId(),
                financeiro.getNome(),
                financeiro.getTipo(),
                financeiro.getData(),
                financeiro.getValor()
        );
    }

    private boolean podeVisualizar(CustomUserDetails user, Financeiro financeiro) {

        if (user.getUser().getRole() != Role.RECEPCIONISTA) {
            return true;
        }

        return financeiro.getTipo().equals("MENSALIDADE")
                || financeiro.getTipo().equals("PAGAMENTO");
    }
}