package com.wictor.controller;

import com.wictor.Dto.LoginDto;
import com.wictor.model.User;
import com.wictor.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {

        User usuario = userService.autenticar(dto.cpf(), dto.senha());

        if (usuario != null) {
            return ResponseEntity.ok("Login efetuado com sucesso");
        } else {
            return ResponseEntity.status(401).body("CPF ou senha inválidos");
        }
    }
}