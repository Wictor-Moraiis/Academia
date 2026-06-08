package com.wictor.service;

import com.wictor.dto.categoria.CategoriaDto;
import com.wictor.dto.categoria.CategoriaResponseDto;
import com.wictor.dto.categoria.CategoriaUpdateDto;
import com.wictor.exception.NotFoundException;
import com.wictor.model.Categoria;
import com.wictor.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public CategoriaResponseDto cadastrar(CategoriaDto categoriaDTO) {

        Categoria categoria = categoriaRepository.save(
                Categoria.builder()
                        .nome(categoriaDTO.nome())
                        .salario(categoriaDTO.salario())
                        .build()
        );

        return toResponseDto(categoria);
    }

    public CategoriaResponseDto atualizar(Integer id, CategoriaUpdateDto dto) {
        Categoria categoria = buscarCategoriaPorId(id);

        if (dto.nome() != null && !dto.nome().isBlank()) {
            categoria.setNome(dto.nome());
        }

        if (dto.salario() != null) {
            categoria.setSalario(dto.salario());
        }

        categoriaRepository.save(categoria);

        return toResponseDto(categoria);
    }

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
                categoria.getSalario()
        );
    }

    public CategoriaResponseDto buscarPorId(Integer id) {

        return toResponseDto(buscarCategoriaPorId(id));
    }

    private Categoria buscarCategoriaPorId(Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));
    }

    public void deletar(Integer id) {

        Categoria categoria = buscarCategoriaPorId(id);

        categoriaRepository.delete(categoria);
    }
}