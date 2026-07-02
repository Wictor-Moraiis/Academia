package com.wictor.service;

import com.wictor.dto.produto.ProdutoDto;
import com.wictor.dto.produto.ProdutoResponseDto;
import com.wictor.dto.produto.ProdutoUpdateDto;
import com.wictor.exception.NotFoundException;
import com.wictor.exception.RegraException;
import com.wictor.model.Produto;
import com.wictor.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ImageService imageService;

    public ProdutoService(ProdutoRepository produtoRepository, ImageService imageService) {

        this.produtoRepository = produtoRepository;
        this.imageService = imageService;
    }

    @Value("${file.upload-dir-produto}")
    private String uploadDir;
    private static final String PREFIXO_IMAGEM  = "produto";

    @Transactional
    public ProdutoResponseDto cadastrar(ProdutoDto produtoDTO, MultipartFile foto) {

        validarNomeDuplicado(produtoDTO.nome(), null);

        Produto produto = produtoRepository.save(
                Produto.builder()
                        .nome(produtoDTO.nome())
                        .desc(produtoDTO.desc())
                        .preco(produtoDTO.preco())
                        .qtd(produtoDTO.qtd())
                        .qtd_min(produtoDTO.qtd_min())
                        .ativo(true)
                        .build()
        );

        atualizarFoto(produto, foto);

        return toResponseDto(produtoRepository.save(produto));

    }

    @Transactional
    public ProdutoResponseDto atualizar(Integer id, ProdutoUpdateDto dto , MultipartFile foto) {
        Produto produto = buscarProdutoPorId(id);
        validarAtivo(produto);

        if (dto.nome() != null && !dto.nome().isBlank()) {

            validarNomeDuplicado(dto.nome(), produto.getId());

            produto.setNome(dto.nome());
        }

        if (dto.desc() != null && !dto.desc().isBlank()) {
            produto.setDesc(dto.desc());
        }

        if (dto.preco() != null) {
            produto.setPreco(dto.preco());
        }

        if (dto.qtd() != null) {
            produto.setQtd(dto.qtd());
        }

        if (dto.qtd_min() != null) {
            produto.setQtd_min(dto.qtd_min());
        }

        atualizarFoto(produto, foto);

        return toResponseDto(produtoRepository.save(produto));
    }

    public void desativar(Integer id) {
        alterarStatus(id, false);
    }

    public void reativar(Integer id) {
        alterarStatus(id, true);
    }

    @Transactional
    private void alterarStatus(Integer id, boolean ativo) {
        Produto produto = buscarProdutoPorId(id);


        produto.setAtivo(ativo);
        produtoRepository.save(produto);
    }

    public List<ProdutoResponseDto> listar() {
        return produtoRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    private ProdutoResponseDto toResponseDto(Produto produto) {
        return new ProdutoResponseDto(
                produto.getId(),
                produto.getNome(),
                produto.getDesc(),
                produto.getPreco(),
                produto.getQtd(),
                produto.getQtd_min(),
                produto.getFoto(),
                produto.isAtivo()
        );
    }

    public ProdutoResponseDto buscarPorId(Integer id) {
        return toResponseDto(buscarProdutoPorId(id));
    }

    private Produto buscarProdutoPorId(Integer id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));
    }

    @Transactional
    public void deletar(Integer id) {

        Produto produto = buscarProdutoPorId(id);

        imageService.deletarImagem(uploadDir, produto.getFoto());

        produtoRepository.delete(produto);
    }

    private void atualizarFoto(Produto produto, MultipartFile foto) {

        if (foto == null || foto.isEmpty()) {
            return;
        }

        imageService.validarImagem(foto);

        produto.setFoto(
                imageService.salvarImagem(
                        foto,
                        produto.getId(),
                        uploadDir,
                        PREFIXO_IMAGEM
                )
        );
    }

    private void validarAtivo(Produto produto) {

        if (!produto.isAtivo()) {
            throw new RegraException("Produto desativado");
        }
    }

    private void validarNomeDuplicado(String nome, Integer idProduto) {

        produtoRepository.findByNomeIgnoreCase(nome)
                .filter(produto -> !produto.getId().equals(idProduto))
                .ifPresent(produto -> {
                    throw new RegraException("Já existe um produto com esse nome.");
                });
    }
}
