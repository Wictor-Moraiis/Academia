package com.wictor.service;

import com.wictor.dto.financeiro.FinanceiroDto;
import com.wictor.dto.financeiro.FinanceiroResponseDto;
import com.wictor.dto.financeiro.FinanceiroUpdateDto;
import com.wictor.exception.NotFoundException;
import com.wictor.model.Financeiro;
import com.wictor.repository.FinanceiroRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinanceiroService {

    private final FinanceiroRepository financeiroRepository;

    public FinanceiroService(FinanceiroRepository financeiroRepository) {
        this.financeiroRepository = financeiroRepository;
    }

    public FinanceiroResponseDto cadastrar(FinanceiroDto financeiroDTO) {

        Financeiro financeiro = financeiroRepository.save(
                Financeiro.builder()
                        .nome(financeiroDTO.nome())
                        .data(financeiroDTO.data())
                        .valor(financeiroDTO.valor())
                        .build()
        );

        return toResponseDto(financeiro);
    }

    @Transactional
    public FinanceiroResponseDto atualizar(Integer id, FinanceiroUpdateDto dto) {
        Financeiro financeiro = buscarFinanceiroPorId(id);

        if (dto.nome() != null && !dto.nome().isBlank()) {
            financeiro.setNome(dto.nome());
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

    public List<FinanceiroResponseDto> listar() {
        return financeiroRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public FinanceiroResponseDto buscarPorId(Integer id) {

        return toResponseDto(buscarFinanceiroPorId(id));
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
                financeiro.getData(),
                financeiro.getValor()
        );
    }
}