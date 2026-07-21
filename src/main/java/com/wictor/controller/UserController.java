package com.wictor.controller;

import com.wictor.dto.login.LoginDto;
import com.wictor.dto.login.LoginResponseDto;
import com.wictor.dto.user.UserResponseDto;
import com.wictor.dto.user.UserUpdateDto;
import com.wictor.security.CustomUserDetails;
import com.wictor.service.JwtService;
import com.wictor.model.User;
import com.wictor.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto dto) {

        User user = userService.autenticar(dto.cpf(), dto.senha());
        String token = jwtService.gerarToken(user);
        return ResponseEntity.ok(new LoginResponseDto(token, user.getId()));
    }

    @PreAuthorize("hasAnyRole('ADMIN','ALUNO','PROFESSOR','GERENTE','RECEPCIONISTA')")
    @PatchMapping(value = "/{userId}", consumes = "multipart/form-data")
    public ResponseEntity<UserResponseDto> atualizar(@PathVariable Integer userId,
                                                     @RequestPart("dados") @Valid UserUpdateDto dto,
                                                     @RequestPart(value = "foto", required = false) MultipartFile foto,
                                                     @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(userService.atualizar(userId, dto, foto, user.getId()));
    }

    @PreAuthorize("hasAnyRole('ADMIN','ALUNO','GERENTE')")
    @PatchMapping("/desativar/{userId}")
    public ResponseEntity<Map<String, String>> desativar(@PathVariable Integer userId,
                                                         @AuthenticationPrincipal CustomUserDetails user) {
        userService.desativar(userId, user.getId());
        return ResponseEntity.ok(Map.of("mensagem", "Usuário desativado"));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PatchMapping("/reativar/{userId}")
    public ResponseEntity<Map<String, String>> reativar(@PathVariable Integer userId) {

        userService.reativar(userId);
        return ResponseEntity.ok(Map.of("mensagem", "Usuário reativado"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deletar(@PathVariable Integer userId) {

        userService.deletar(userId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> listar() {

        return ResponseEntity.ok(userService.listar());
    }

    @PreAuthorize("hasAnyRole('ADMIN','ALUNO','PROFESSOR','GERENTE','RECEPCIONISTA')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> buscarPorId(@PathVariable Integer userId,
                                                       @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(userService.buscarPorId(userId, user.getId()));
    }

}
