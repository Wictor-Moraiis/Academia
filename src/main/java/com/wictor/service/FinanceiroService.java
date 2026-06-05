package com.wictor.service;

import com.wictor.dto.financeiro.FinanceiroDto;
import com.wictor.dto.financeiro.FinanceiroResponseDto;
import com.wictor.dto.financeiro.FinanceiroUpdateDto;
import com.wictor.exception.NotFoundException;
import com.wictor.model.Financeiro;
import com.wictor.repository.FinanceiroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinanceiroService {

    private final FinanceiroRepository repository;

    public FinanceiroService(FinanceiroRepository repository) {
        this.repository = repository;
    }

    public Financeiro cadastrar(FinanceiroDto financeiroDTO) {

        Financeiro.FinanceiroBuilder builder = Financeiro.builder()
                .nome(financeiroDTO.nome())
                .data(financeiroDTO.data())
                .valor(financeiroDTO.valor());
        Financeiro financeiro = builder.build();

        return repository.save(financeiro);
    }

    public Financeiro atualizar(Integer id, FinanceiroUpdateDto dto) {
        Financeiro financeiro = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Atividade financeira não encontrada"));

        if (dto.nome() != null && !dto.nome().isBlank()) {
            financeiro.setNome(dto.nome());
        }

        if (dto.data() != null) {
            financeiro.setData(dto.data());
        }

        if (dto.valor() != null) {
            financeiro.setValor(dto.valor());
        }

        return repository.save(financeiro);
    }

    public List<FinanceiroResponseDto> listar() {
        return repository.findAll()
                .stream()
                .map(f -> new FinanceiroResponseDto(
                        f.getId(),
                        f.getNome(),
                        f.getData(),
                        f.getValor()
                ))
                .toList();
    }

    public Financeiro buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Atividade financeira não encontrada"));
    }

    public void deletar(Integer id) {

        if (!repository.existsById(id)) {
            throw new NotFoundException("Atividade financeira não encontrada");
        }
        repository.deleteById(id);
    }
}