package com.wictor.Service;

import com.wictor.Dto.*;
import com.wictor.exception.*;
import com.wictor.model.Plano;
import com.wictor.repository.PlanoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PlanoService {

    private final PlanoRepository repository;

    public PlanoService(PlanoRepository repository) {
        this.repository = repository;
    }

    public Plano cadastrar(PlanoDto planoDTO) {

        Plano.PlanoBuilder builder = Plano.builder()
                .nome(planoDTO.nome())
                .valor(planoDTO.valor())
                .validade(planoDTO.validade());
        Plano plano = builder.build();

        return repository.save(plano);
    }

    public Plano atualizar(Integer id, PlanoDto dto) {
        Plano plano = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Plano não encontrado"));

        if (dto.nome() != null && !dto.nome().isBlank()) {
            plano.setNome(dto.nome());
        }

        if (dto.valor() != null) {
            plano.setValor(dto.valor());
        }

        if (dto.validade() != null) {
            plano.setValidade(dto.validade());
        }

        return repository.save(plano);
    }

    public List<PlanoResponseDto> listar() {
        return repository.findAll()
                .stream()
                .map(p -> new PlanoResponseDto(
                        p.getId(),
                        p.getNome(),
                        p.getValor()
                ))
                .toList();
    }

    public Plano buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Plano não encontrado"));
    }

    public void deletar(Integer id) {

        if (!repository.existsById(id)) {
            throw new NotFoundException("Plano não encontrado");
        }
        repository.deleteById(id);
    }
}