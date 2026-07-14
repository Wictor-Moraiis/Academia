package com.wictor.service;

import com.wictor.dto.produto.ProdutoDto;
import com.wictor.dto.produto.ProdutoResponseDto;
import com.wictor.dto.produto.ProdutoUpdateDto;
import com.wictor.enums.AcaoLog;
import com.wictor.exception.NotFoundException;
import com.wictor.exception.RegraException;
import com.wictor.model.Produto;
import com.wictor.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.wictor.annotation.Auditar;
import com.wictor.audit.AuditoriaContext;
import com.wictor.audit.AuditoriaInfo;
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

    @Auditar(acao = AcaoLog.CADASTRO)
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
        produtoRepository.flush();

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .depois(produto)
                        .entidade("Produto")
                        .entidadeId(produto.getId())
                        .build()
        );
        return toResponseDto(produto);
    }

    @Auditar(acao = AcaoLog.ALTERACAO)
    @Transactional
    public ProdutoResponseDto atualizar(Integer id, ProdutoUpdateDto dto , MultipartFile foto) {

        Produto produto = buscarProdutoPorId(id);
        Produto antes = copiarProduto(produto);

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

        Produto produtoSalvo = produtoRepository.save(produto);

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .antes(antes)
                        .depois(produtoSalvo)
                        .entidade("Produto")
                        .entidadeId(produtoSalvo.getId())
                        .build()
        );

        return toResponseDto(produtoSalvo);

    }

    @Auditar(acao = AcaoLog.ALTERACAO)
    @Transactional
    public void desativar(Integer id) {

        Produto produto = buscarProdutoPorId(id);
        Produto antes = copiarProduto(produto);

        produto.setAtivo(false);

        Produto depois = produtoRepository.save(produto);


        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .antes(antes)
                        .depois(depois)
                        .entidade("Produto")
                        .entidadeId(id)
                        .build()
        );
    }

    @Auditar(acao = AcaoLog.ALTERACAO)
    @Transactional
    public void reativar(Integer id) {

        Produto produto = buscarProdutoPorId(id);
        Produto antes = copiarProduto(produto);

        produto.setAtivo(true);

        Produto depois = produtoRepository.save(produto);


        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .antes(antes)
                        .depois(depois)
                        .entidade("Produto")
                        .entidadeId(id)
                        .build()
        );
    }

    @Auditar(acao = AcaoLog.CONSULTA, descricao = "Listagem de produtos.")
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

    @Auditar(acao = AcaoLog.CONSULTA, descricao = "Consulta de produto por ID.")
    public ProdutoResponseDto buscarPorId(Integer id) {return toResponseDto(buscarProdutoPorId(id));}

    private Produto buscarProdutoPorId(Integer id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));
    }

    @Auditar(acao = AcaoLog.EXCLUSAO)
    @Transactional
    public void deletar(Integer id) {

        Produto produto = buscarProdutoPorId(id);

        AuditoriaContext.registrar(
                AuditoriaInfo.builder()
                        .antes(produto)
                        .entidade("Produto")
                        .entidadeId(id)
                        .build()
        );

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

    private Produto copiarProduto(Produto produto) {

        return Produto.builder()
                .id(produto.getId())
                .nome(produto.getNome())
                .desc(produto.getDesc())
                .preco(produto.getPreco())
                .qtd(produto.getQtd())
                .qtd_min(produto.getQtd_min())
                .foto(produto.getFoto())
                .ativo(produto.isAtivo())
                .build();
    }
}
