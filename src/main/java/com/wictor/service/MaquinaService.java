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

    private final MaquinaRepository repository;

    public MaquinaService(MaquinaRepository repository) {
        this.repository = repository;
    }

    public Maquina cadastrar(MaquinaDto maquinaDTO) {

        Maquina.MaquinaBuilder builder = Maquina.builder()
                .nome(maquinaDTO.nome())
                .ativa(true);
        Maquina maquina = builder.build();

        return repository.save(maquina);
    }

    public Maquina atualizar(Integer id, MaquinaUpdateDto dto) {
        Maquina maquina = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Máquina não encontrada"));
        if (dto.nome() != null && !dto.nome().isBlank()) {
            maquina.setNome(dto.nome());
        }

        if (dto.ativa() != null) {
            maquina.setAtiva(dto.ativa());
        }

        return repository.save(maquina);
    }

    public List<MaquinaResponseDto> listar() {
        return repository.findAll()
                .stream()
                .map(m -> new MaquinaResponseDto(
                        m.getId(),
                        m.getNome(),
                        m.isAtiva()
                ))
                .toList();
    }

    public Maquina buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Máquina não encontrada"));
    }

    public void desativar(Integer id) {

        Maquina maquina = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Máquina não encontrada"));

        maquina.setAtiva(false);

        repository.save(maquina);
    }

    public void reativar(Integer id) {

        Maquina maquina = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Máquina não encontrada"));

        maquina.setAtiva(true);

        repository.save(maquina);
    }

    public void deletar(Integer id) {

        if (!repository.existsById(id)) {
            throw new NotFoundException("Máquina não encontrada");
        }
        repository.deleteById(id);
    }
}
