package com.wictor.service;

import com.wictor.annotation.Auditar;
import com.wictor.audit.AuditoriaContext;
import com.wictor.audit.AuditoriaInfo;
import com.wictor.dto.maquina.MaquinaDto;
import com.wictor.dto.maquina.MaquinaResponseDto;
import com.wictor.dto.maquina.MaquinaUpdateDto;
import com.wictor.enums.AcaoLog;
import com.wictor.exception.NotFoundException;
import com.wictor.model.Maquina;
import com.wictor.repository.MaquinaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaquinaService {

    private final MaquinaRepository maquinaRepository;

    public MaquinaService(MaquinaRepository maquinaRepository) {
        this.maquinaRepository = maquinaRepository;
    }

    @Auditar(acao = AcaoLog.CADASTRO)
    @Transactional
    public MaquinaResponseDto cadastrar(MaquinaDto maquinaDTO) {

        Maquina maquina = maquinaRepository.save(
                Maquina.builder()
                        .nome(maquinaDTO.nome())
                        .ativa(true)
                        .build()
        );

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .depois(maquina)
                        .entidade("Maquina")
                        .entidadeId(maquina.getId())
                        .build()
        );

        return toResponseDto(maquina);
    }

    @Auditar(acao = AcaoLog.ALTERACAO)
    @Transactional
    public MaquinaResponseDto atualizar(Integer id, MaquinaUpdateDto dto) {

        Maquina maquina = buscarMaquinaPorId(id);
        Maquina antes = copiarMaquina(maquina);

        if (dto.nome() != null && !dto.nome().isBlank()) {
            maquina.setNome(dto.nome());
        }

        if (dto.ativa() != null) {
            maquina.setAtiva(dto.ativa());
        }

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .antes(antes)
                        .depois(maquina)
                        .entidade("Maquina")
                        .entidadeId(maquina.getId())
                        .build()
        );

        return toResponseDto(maquinaRepository.save(maquina));
    }

    @Auditar(acao = AcaoLog.CONSULTA, descricao = "Listagem de maquinas.")
    public List<MaquinaResponseDto> listar() {
        return maquinaRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Auditar(acao = AcaoLog.CONSULTA, descricao = "Consulta de maquina por ID.")
    public MaquinaResponseDto buscarPorId(Integer id) {
        return toResponseDto(buscarMaquinaPorId(id));
    }

    @Auditar(acao = AcaoLog.ALTERACAO)
    @Transactional
    public void desativar(Integer id) {

        Maquina maquina = buscarMaquinaPorId(id);
        Maquina antes = copiarMaquina(maquina);
        maquina.setAtiva(false);

        Maquina depois = maquinaRepository.save(maquina);

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .antes(antes)
                        .depois(depois)
                        .entidade("Maquina")
                        .entidadeId(id)
                        .build()
        );
    }

    @Auditar(acao = AcaoLog.ALTERACAO)
    @Transactional
    public void reativar(Integer id) {

        Maquina maquina = buscarMaquinaPorId(id);
        Maquina antes = copiarMaquina(maquina);
        maquina.setAtiva(true);

        Maquina depois = maquinaRepository.save(maquina);

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .antes(antes)
                        .depois(depois)
                        .entidade("Maquina")
                        .entidadeId(id)
                        .build()
        );
    }

    @Auditar(acao = AcaoLog.EXCLUSAO)
    @Transactional
    public void deletar(Integer id) {

        Maquina maquina = buscarMaquinaPorId(id);

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .antes(maquina)
                        .entidade("Maquina")
                        .entidadeId(maquina.getId())
                        .build()
        );

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

    private Maquina copiarMaquina(Maquina maquina) {

        return Maquina.builder()
                .id(maquina.getId())
                .nome(maquina.getNome())
                .ativa(maquina.isAtiva())
                .build();
    }
}
