package com.wictor.controller;

import com.wictor.dto.login.LoginDto;
import com.wictor.dto.login.LoginResponseDto;
import com.wictor.dto.user.UserDto;
import com.wictor.dto.user.UserResponseDto;
import com.wictor.dto.user.UserRoleUpdateDto;
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

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<UserResponseDto> cadastrar(
            @RequestPart("dados") @Valid UserDto dto,
            @RequestPart(value = "foto", required = false) MultipartFile foto,
            @AuthenticationPrincipal CustomUserDetails userAdm) {

        return ResponseEntity.status(201).body(userService.cadastrar(dto,foto,userAdm));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto dto) {
        User user = userService.autenticar(dto.cpf(), dto.senha());
        String token = jwtService.gerarToken(user);
        return ResponseEntity.ok(new LoginResponseDto(token, user.getId()));
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @PatchMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<UserResponseDto> atualizar(@PathVariable Integer id,
                                       @RequestPart("dados") @Valid UserUpdateDto dto,
                                       @RequestPart(value = "foto", required = false) MultipartFile foto) {

        return ResponseEntity.ok(userService.atualizar(id, dto, foto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/role/{id}")
    public ResponseEntity<UserResponseDto> atualizarRole(@PathVariable Integer id,
                                           @RequestBody UserRoleUpdateDto dto) {

        return ResponseEntity.ok(userService.atualizarRole(id,dto));
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @PatchMapping("/desativar/{id}")
    public ResponseEntity<Map<String, String>> desativar(@PathVariable Integer id) {

        userService.desativar(id);
        return ResponseEntity.ok(Map.of("mensagem", "Usuário desativado"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/reativar/{id}")
    public ResponseEntity<Map<String, String>> reativar(@PathVariable Integer id) {
        userService.reativar(id);
        return ResponseEntity.ok(Map.of("mensagem", "Usuário reativado"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {

        userService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> listar() {
        return ResponseEntity.ok(userService.listar());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.buscarPorId(id));
    }

}
