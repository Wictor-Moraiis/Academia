package com.wictor.service;

import com.wictor.dto.plano.PlanoDto;
import com.wictor.dto.plano.PlanoResponseDto;
import com.wictor.dto.plano.PlanoUpdateDto;
import com.wictor.exception.NotFoundException;
import com.wictor.model.Plano;
import com.wictor.repository.PlanoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PlanoService {

    private final PlanoRepository planoRepository;

    public PlanoService(PlanoRepository planoRepository) {
        this.planoRepository = planoRepository;
    }

    @Transactional
    public PlanoResponseDto cadastrar(PlanoDto planoDTO) {

        Plano plano = planoRepository.save(
                Plano.builder()
                        .nome(planoDTO.nome())
                        .valor(planoDTO.valor())
                        .validade(planoDTO.validade())
                        .build()
        );

        return toResponseDto(plano);

    }

    @Transactional
    public PlanoResponseDto atualizar(Integer id, PlanoUpdateDto dto) {
       Plano plano = buscarPlanoPorId(id);

        if (dto.nome() != null && !dto.nome().isBlank()) {
            plano.setNome(dto.nome());
        }

        if (dto.valor() != null) {
            plano.setValor(dto.valor());
        }

        if (dto.validade() != null) {
            plano.setValidade(dto.validade());
        }

        return toResponseDto(planoRepository.save(plano));
    }

    public List<PlanoResponseDto> listar() {
        return planoRepository.findAll()
                .stream()
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

    public PlanoResponseDto buscarPorId(Integer id) {
        return toResponseDto(buscarPlanoPorId(id));
    }

    private Plano buscarPlanoPorId(Integer id) {
        return planoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Plano não encontrado"));
    }

    @Transactional
    public void deletar(Integer id) {

        Plano plano = buscarPlanoPorId(id);

        planoRepository.delete(plano);
    }
}