package com.wictor.controller;

import com.wictor.Dto.LoginDto;
import com.wictor.Dto.UserDto;
import com.wictor.Dto.UserRoleUpdateDto;
import com.wictor.Dto.UserUpdateDto;
import com.wictor.Security.JwtService;
import com.wictor.exception.UnauthorizedException;
import com.wictor.model.User;
import com.wictor.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private JwtService jwtService;

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/users", consumes = "multipart/form-data")
    public ResponseEntity<?> cadastrar(
            @RequestPart("dados") @Valid UserDto dto,
            @RequestPart(value = "foto", required = false) MultipartFile foto,
            @AuthenticationPrincipal User logado) {

        User novo = userService.cadastrar(dto, foto, logado);
        return ResponseEntity.status(201).body(novo);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        User user = userService.autenticar(dto.cpf(), dto.senha());
        if (user == null) throw new UnauthorizedException("CPF ou senha inválidos");

        String token = jwtService.gerarToken(user);
        return ResponseEntity.ok(Map.of("token", token, "id", user.getId()));
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @PatchMapping(value = "/users/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> atualizar(@PathVariable Integer id,
                                       @RequestPart("dados") @Valid UserUpdateDto dto,
                                       @RequestPart(value = "foto", required = false) MultipartFile foto) {

        User user = userService.atualizar(id, dto, foto);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/users/{id}/role")
    public ResponseEntity<?> atualizarRole(@PathVariable Integer id,
                                           @RequestBody UserRoleUpdateDto dto) {

        User user = userService.atualizarRole(id, dto);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @PatchMapping("/users/{id}/desativar")
    public ResponseEntity<?> desativar(@PathVariable Integer id) {

        userService.desativar(id);
        return ResponseEntity.ok(Map.of("mensagem", "Usuário desativado"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/users/{id}/reativar")
    public ResponseEntity<?> reativar(@PathVariable Integer id) {
        userService.reativar(id);
        return ResponseEntity.ok(Map.of("mensagem", "Usuário reativado"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deletar(@PathVariable Integer id) {

        userService.deletar(id);
        return ResponseEntity.ok(Map.of("mensagem", "Usuário excluído"));
    }

}
