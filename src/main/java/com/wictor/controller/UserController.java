package com.wictor.controller;

import com.wictor.Dto.LoginDto;
import com.wictor.Dto.UserDto;
import com.wictor.Dto.UserRoleUpdateDto;
import com.wictor.Dto.UserUpdateDto;
import com.wictor.Security.JwtService;
import com.wictor.enums.Role;
import com.wictor.exception.RegraException;
import com.wictor.exception.UnauthorizedException;
import com.wictor.model.User;
import com.wictor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/users")
    public ResponseEntity<?> cadastrar(@RequestBody UserDto dto,
                                       @AuthenticationPrincipal User logado) {

        if (logado == null || !logado.getRole().equals(Role.ADMIN)) {
            throw new UnauthorizedException("Sem permissão");
        }

        User novo = userService.cadastrar(dto, true);
        return ResponseEntity.status(201).body(novo);
    }

    @PostMapping("/autocadastro")
    public ResponseEntity<?> cadastrarAluno(@RequestBody UserDto dto) {
        User novo = userService.cadastrar(dto, false);
        return ResponseEntity.status(201).body(novo);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        User user = userService.autenticar(dto.cpf(), dto.senha());
        if (user == null) throw new UnauthorizedException("CPF ou senha inválidos");

        String token = jwtService.gerarToken(user);
        return ResponseEntity.ok(Map.of("token", token, "id", user.getId()));
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id,
                                       @RequestBody UserUpdateDto dto,
                                       @AuthenticationPrincipal User logado) {

        if (!logado.getRole().equals(Role.ADMIN) && logado.getId().longValue() != id.longValue()) {
            throw new RegraException("Sem permissão");
        }
        User user = userService.atualizar(id, dto);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/users/{id}/role")
    public ResponseEntity<?> atualizarRole(@PathVariable Long id,
                                       @RequestBody UserRoleUpdateDto dto,
                                       @AuthenticationPrincipal User logado) {

        if (!logado.getRole().equals(Role.ADMIN)) {
            throw new RegraException("Sem permissão");
        }

        User user = userService.atualizarRole(id, dto);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/users/{id}/desativar")
    public ResponseEntity<?> desativar(@PathVariable Long id,
                                       @AuthenticationPrincipal User logado) {

        if (!logado.getRole().equals(Role.ADMIN) && logado.getId().longValue() != id.longValue()) {
            throw new RegraException("Sem permissão");
        }
        userService.desativar(id);
        return ResponseEntity.ok("Usuário desativado");
    }

    @PatchMapping("/users/{id}/reativar")
    public ResponseEntity<?> reativar(@PathVariable Long id,
                                       @AuthenticationPrincipal User logado) {

        if (!logado.getRole().equals(Role.ADMIN)) {
            throw new RegraException("Sem permissão");
        }
        userService.reativar(id);
        return ResponseEntity.ok("Usuário reativado");
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id,
                                       @AuthenticationPrincipal User logado) {

        if (!logado.getRole().equals(Role.ADMIN)) {
            throw new RegraException("Sem permissão");
        }
        userService.deletar(id);
        return ResponseEntity.ok("Usuário excluído");
    }


}
