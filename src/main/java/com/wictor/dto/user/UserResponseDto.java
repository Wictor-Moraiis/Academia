package com.wictor.dto.user;

import com.wictor.enums.Role;
import com.wictor.enums.Sexo;
import com.wictor.enums.Tipo;

import java.time.LocalDate;

public record UserResponseDto (
        String nome,
        String email1,
        String email2,
        String tel1,
        String tel2,
        Sexo sexo,
        String cep,
        String bairro,
        String rua,
        Integer numeroCasa,
        String comp,
        LocalDate datanasc,
        Role role,
        Tipo tipo
){}
