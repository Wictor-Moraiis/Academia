package com.wictor.controller;

import com.wictor.Dto.UserDto;
import com.wictor.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> cadastrar(@RequestBody UserDto dto) {

        userService.cadastrarUsuario(dto);
        return ResponseEntity.ok("Usuário cadastrado");

    }
}