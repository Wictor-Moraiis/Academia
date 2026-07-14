package com.wictor.service;

import com.wictor.annotation.Auditar;
import com.wictor.dto.categoria.CategoriaDto;
import com.wictor.dto.categoria.CategoriaResponseDto;
import com.wictor.dto.categoria.CategoriaUpdateDto;
import com.wictor.enums.AcaoLog;
import com.wictor.exception.NotFoundException;
import com.wictor.model.Categoria;
import com.wictor.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import com.wictor.audit.AuditoriaContext;
import com.wictor.audit.AuditoriaInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Auditar(acao = AcaoLog.CADASTRO)
    @Transactional
    public CategoriaResponseDto cadastrar(CategoriaDto categoriaDTO) {

        Categoria categoria = categoriaRepository.save(
                Categoria.builder()
                        .nome(categoriaDTO.nome())
                        .salario(categoriaDTO.salario())
                        .role(categoriaDTO.role())
                        .build()
        );

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .depois(categoria)
                        .entidade("Categoria")
                        .entidadeId(categoria.getId())
                        .build()
        );

        return toResponseDto(categoria);
    }

    @Auditar(acao = AcaoLog.ALTERACAO)
    @Transactional
    public CategoriaResponseDto atualizar(Integer id, CategoriaUpdateDto dto) {
        Categoria categoria = buscarCategoriaPorId(id);
        Categoria categoriaAntes = copiarCategoria(categoria);

        if (dto.nome() != null && !dto.nome().isBlank()) {
            categoria.setNome(dto.nome());
        }

        if (dto.salario() != null) {
            categoria.setSalario(dto.salario());
        }

        if (dto.role() != null) {
            categoria.setRole(dto.role());
        }

        Categoria categoriaSalva = categoriaRepository.save(categoria);

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .antes(categoriaAntes)
                        .depois(categoriaSalva)
                        .entidade("Categoria")
                        .entidadeId(categoriaSalva.getId())
                        .build()
        );

        return toResponseDto(categoria);
    }

    @Auditar(acao = AcaoLog.CONSULTA, descricao = "Listagem de categorias.")
    public List<CategoriaResponseDto> listar() {

        return categoriaRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    private CategoriaResponseDto toResponseDto(Categoria categoria) {
        return new CategoriaResponseDto(
               categoria.getId(),
                categoria.getNome(),
                categoria.getSalario(),
                categoria.getRole()
        );
    }

    @Auditar(acao = AcaoLog.CONSULTA, descricao = "Consulta de categoria por ID.")
    public CategoriaResponseDto buscarPorId(Integer id) {

        return toResponseDto(buscarCategoriaPorId(id));
    }

    private Categoria buscarCategoriaPorId(Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));
    }

    @Auditar(acao = AcaoLog.EXCLUSAO)
    @Transactional
    public void deletar(Integer id) {

        Categoria categoria = buscarCategoriaPorId(id);

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .antes(categoria)
                        .entidade("Categoria")
                        .entidadeId(categoria.getId())
                        .build()
        );

        categoriaRepository.delete(categoria);
    }

    private Categoria copiarCategoria(Categoria categoria) {

        return Categoria.builder()
                .id(categoria.getId())
                .nome(categoria.getNome())
                .salario(categoria.getSalario())
                .role(categoria.getRole())
                .build();
    }
}