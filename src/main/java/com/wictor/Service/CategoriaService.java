package com.wictor.Service;

import com.wictor.Dto.CategoriaDto;
import com.wictor.Dto.CategoriaResponseDto;
import com.wictor.Dto.CategoriaUpdateDto;
import com.wictor.exception.NotFoundException;
import com.wictor.model.Categoria;
import com.wictor.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public Categoria cadastrar(CategoriaDto categoriaDTO) {

        Categoria.CategoriaBuilder builder = Categoria.builder()
                .nome(categoriaDTO.nome())
                .salario(categoriaDTO.salario());
        Categoria categoria = builder.build();

        return repository.save(categoria);
    }

    public Categoria atualizar(Integer id, CategoriaUpdateDto dto) {
        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        if (dto.nome() != null && !dto.nome().isBlank()) {
            categoria.setNome(dto.nome());
        }

        if (dto.salario() != null) {
            categoria.setSalario(dto.salario());
        }

        return repository.save(categoria);
    }

    public List<CategoriaResponseDto> listar() {
        return repository.findAll()
                .stream()
                .map(p -> new CategoriaResponseDto(
                        p.getId(),
                        p.getNome(),
                        p.getSalario()
                ))
                .toList();
    }

    public Categoria buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));
    }

    public void deletar(Integer id) {

        if (!repository.existsById(id)) {
            throw new NotFoundException("Categoria não encontrada");
        }
        repository.deleteById(id);
    }
}