package com.wictor.controller;

import com.wictor.config.swagger.annotation.ApiErrorResponses;
import com.wictor.config.swagger.annotation.ApiPublicResponses;
import com.wictor.dto.aluno.AlunoDto;
import com.wictor.dto.aluno.AlunoResponseDto;
import com.wictor.dto.aluno.AlunoUpdateDto;
import com.wictor.service.AlunoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Tag(
        name = "Alunos",
        description = """
                Endpoints responsáveis pelo gerenciamento dos alunos da academia.
                
                Permissões:
                • ADMIN: acesso completo.
                • GERENTE: cadastro, consulta e atualização.
                • RECEPCIONISTA: cadastro, consulta e atualização.
                • PROFESSOR: cadastro e consulta.
                • ALUNO: pode cadastrar, consultar e atualizar apenas os próprios dados.
                """
)
@RestController
@RequestMapping("/alunos")
public class  AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @Operation(
            summary = "Cadastrar aluno",
            description = """
                    Realiza o cadastro de um novo aluno.
                    
                    A requisição deve ser enviada no formato multipart/form-data,
                    contendo:
                    
                    • dados: informações do aluno.
                    • foto: imagem de perfil (opcional).
                    
                    Em caso de sucesso, retorna os dados completos do aluno cadastrado.
                    """
    )
    @ApiPublicResponses
    @ApiResponse(responseCode = "201", description = "Aluno cadastrado com sucesso.",
            content = @Content(schema = @Schema(implementation = AlunoResponseDto.class)))
    @ApiResponse(responseCode = "409", description = "Conflito de dados. Já existe um registro com informações únicas.")
    @ApiResponse(responseCode = "413", description = "Imagem excede o tamanho permitido.")
    @ApiResponse(responseCode = "422", description = "Formato de imagem inválido.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AlunoResponseDto> cadastrar(@RequestPart("dados")
                                                          @Parameter(description = "Dados do aluno em formato JSON.")
                                                          @Valid AlunoDto dto,
                                                      @RequestPart(value = "foto", required = false)
                                                      @Parameter(description = "Foto de perfil do aluno.")
                                                      MultipartFile foto) {

        return ResponseEntity.status(201).body(alunoService.cadastrar(dto, foto));
    }

    @Operation(
            summary = "Atualizar aluno",
            description = """
                    Atualiza os dados de um aluno.
                    
                    Administradores, gerentes e recepcionistas podem atualizar
                    qualquer aluno.
                    
                    O próprio aluno pode atualizar apenas seus próprios dados.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "201", description = "Aluno alterado com sucesso.",
            content = @Content(schema = @Schema(implementation = AlunoResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Aluno não encontrado.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA') or #alunoId == authentication.principal.alunoId")
    @PatchMapping("/{alunoId}")
    public ResponseEntity<AlunoResponseDto> atualizar(@Parameter(description =
                                                                  "Identificador único do aluno.", example = "1")
                                                          @PathVariable Integer alunoId,
                                                      @RequestBody @Valid AlunoUpdateDto dto) {

        return ResponseEntity.ok(alunoService.atualizar(alunoId, dto));
    }

    @Operation(
            summary = "Excluir aluno",
            description = """
                Remove um aluno do sistema.

                Esta operação é exclusiva para administradores.

                Após a exclusão, o aluno deixa de estar disponível
                para consultas e demais operações.
                """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "204", description = "Aluno excluído com sucesso.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Aluno não encontrado.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{alunoId}")
    public ResponseEntity<Void> deletar(@Parameter(description = "Identificador único do aluno.", example = "1")
                                            @PathVariable Integer alunoId) {

        alunoService.deletar(alunoId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar alunos",
            description = """
                Retorna uma lista com todos os alunos cadastrados.

                Apenas usuários com perfil ADMIN, GERENTE,
                RECEPCIONISTA ou PROFESSOR possuem acesso
                a esta operação.
                """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Lista de alunos retornada com sucesso.",
            content = @Content(mediaType = "application/json", array = @ArraySchema(
                    schema = @Schema(implementation = AlunoResponseDto.class))
            )
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA','PROFESSOR')")
    @GetMapping
    public ResponseEntity<List<AlunoResponseDto>> listar() {

        return ResponseEntity.ok(alunoService.listar());
    }

    @Operation(
            summary = "Buscar aluno por ID",
            description = """
                Retorna os dados completos de um aluno.

                Administradores, gerentes, recepcionistas e professores
                podem consultar qualquer aluno.

                O aluno pode consultar apenas seus próprios dados.
                """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Aluno encontrado com sucesso.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AlunoResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Aluno não encontrado.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA', 'PROFESSOR') or #alunoId == authentication.principal.alunoId")
    @GetMapping("/{alunoId}")
    public ResponseEntity<AlunoResponseDto> buscarPorId(@Parameter(description = "Identificador único do aluno.", example = "1"
    )@PathVariable Integer alunoId) {

        return ResponseEntity.ok(alunoService.buscarPorId(alunoId));
    }
}

