package com.wictor.controller;

import com.wictor.config.swagger.annotation.ApiErrorResponses;
import com.wictor.config.swagger.annotation.ApiPublicResponses;
import com.wictor.dto.login.LoginDto;
import com.wictor.dto.login.LoginResponseDto;
import com.wictor.dto.user.UserResponseDto;
import com.wictor.dto.user.UserUpdateDto;
import com.wictor.model.User;
import com.wictor.security.CustomUserDetails;
import com.wictor.service.JwtService;
import com.wictor.service.UserService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Tag(
        name = "Usuários",
        description = """
                Endpoints responsáveis pelo gerenciamento dos usuários do sistema.

                Permissões:
                • ADMIN: acesso completo.
                • GERENTE: gerenciamento de usuários.
                • PROFESSOR: consulta e atualização própria.
                • RECEPCIONISTA: consulta e atualização própria.
                • ALUNO: consulta e atualização própria.
                """
)
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Operation(
            summary = "Realizar login",
            description = "Autentica um usuário utilizando CPF e senha e retorna um token JWT."
    )
    @ApiPublicResponses
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso.",
            content = @Content(schema = @Schema(implementation = LoginResponseDto.class)))
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginDto dto) {

        User user = userService.autenticar(dto.cpf(), dto.senha());
        String token = jwtService.gerarToken(user);
        return ResponseEntity.ok(new LoginResponseDto(token, user.getId()));
    }

    @Operation(
            summary = "Atualizar usuário",
            description = """
                    Atualiza os dados de um usuário.

                    Usuários comuns podem atualizar apenas seus próprios dados.
                    Administradores possuem acesso completo.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso.",
            content = @Content(schema = @Schema(implementation = UserResponseDto.class)))
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','ALUNO','PROFESSOR','GERENTE','RECEPCIONISTA')")
    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDto> atualizar(
            @Parameter(description = "Identificador do usuário.", example = "1")
            @PathVariable Integer userId,
            @RequestPart("dados") @Valid UserUpdateDto dto,
            @RequestPart(value = "foto", required = false)
            MultipartFile foto,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(userService.atualizar(userId, dto, foto, user.getId()));
    }

    @Operation(
            summary = "Desativar usuário",
            description = "Desativa um usuário do sistema."
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Usuário desativado com sucesso.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE') or #userId == authentication.principal.id")
    @PatchMapping("/desativar/{userId}")
    public ResponseEntity<Map<String, String>> desativar(
            @Parameter(description = "Identificador do usuário.", example = "1")
            @PathVariable Integer userId,
            @AuthenticationPrincipal CustomUserDetails user) {

        userService.desativar(userId, user.getId());
        return ResponseEntity.ok(Map.of("mensagem", "Usuário desativado"));
    }

    @Operation(summary = "Reativar usuário", description = "Reativa um usuário desativado.")
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Usuário reativado com sucesso.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PatchMapping("/reativar/{userId}")
    public ResponseEntity<Map<String, String>> reativar(
            @Parameter(description = "Identificador do usuário.", example = "1")
            @PathVariable Integer userId) {

        userService.reativar(userId);
        return ResponseEntity.ok(Map.of("mensagem", "Usuário reativado"));
    }

    @Operation(summary = "Excluir usuário", description = "Remove um usuário do sistema.")
    @ApiErrorResponses
    @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "Identificador do usuário.", example = "1")
            @PathVariable Integer userId) {

        userService.deletar(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar usuários",
            description = "Retorna todos os usuários cadastrados."
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Usuários retornados com sucesso.",
            content = @Content(mediaType = "application/json",array = @ArraySchema(
                    schema = @Schema(implementation = UserResponseDto.class))))
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> listar() {

        return ResponseEntity.ok(userService.listar());
    }

    @Operation(
            summary = "Buscar usuário por ID",
            description = "Retorna os dados de um usuário específico."
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso.",
            content = @Content(schema = @Schema(implementation = UserResponseDto.class)))
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','ALUNO','PROFESSOR','GERENTE','RECEPCIONISTA')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> buscarPorId(
            @Parameter(description = "Identificador do usuário.", example = "1")
            @PathVariable Integer userId,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(userService.buscarPorId(userId, user.getId()));
    }
}