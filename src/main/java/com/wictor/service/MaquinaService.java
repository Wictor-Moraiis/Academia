package com.wictor.service;

import com.wictor.dto.maquina.MaquinaDto;
import com.wictor.dto.maquina.MaquinaResponseDto;
import com.wictor.dto.maquina.MaquinaUpdateDto;
import com.wictor.exception.NotFoundException;
import com.wictor.model.Maquina;
import com.wictor.repository.MaquinaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaquinaService {

    private final MaquinaRepository maquinaRepository;

    public MaquinaService(MaquinaRepository maquinaRepository) {
        this.maquinaRepository = maquinaRepository;
    }

    public MaquinaResponseDto cadastrar(MaquinaDto maquinaDTO) {

        Maquina maquina = maquinaRepository.save(
                Maquina.builder()
                        .nome(maquinaDTO.nome())
                        .ativa(true)
                        .build()
        );

        return toResponseDto(maquina);
    }

    public MaquinaResponseDto atualizar(Integer id, MaquinaUpdateDto dto) {
        Maquina maquina = buscarMaquinaPorId(id);

        if (dto.nome() != null && !dto.nome().isBlank()) {
            maquina.setNome(dto.nome());
        }

        if (dto.ativa() != null) {
            maquina.setAtiva(dto.ativa());
        }

        return toResponseDto(maquinaRepository.save(maquina));
    }

    public List<MaquinaResponseDto> listar() {
        return maquinaRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public MaquinaResponseDto buscarPorId(Integer id) {
        return toResponseDto(buscarMaquinaPorId(id));
    }

    public void desativar(Integer id) {
        alterarStatus(id, false);
    }

    public void reativar(Integer id) {
        alterarStatus(id, true);
    }

    private void alterarStatus(Integer id, boolean ativa) {
        Maquina maquina = buscarMaquinaPorId(id);

        maquina.setAtiva(ativa);

        maquinaRepository.save(maquina);
    }

    public void deletar(Integer id) {

        Maquina maquina = buscarMaquinaPorId(id);

        maquinaRepository.delete(maquina);
    }

    private MaquinaResponseDto toResponseDto(Maquina maquina) {
        return new MaquinaResponseDto(
                maquina.getId(),
                maquina.getNome(),
                maquina.isAtiva()
        );
    }

    private Maquina buscarMaquinaPorId(Integer id) {
        return maquinaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Máquina não encontrada"));
    }
}
