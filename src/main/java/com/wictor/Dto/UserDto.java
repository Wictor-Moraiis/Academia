package com.wictor.Dto;

public record UserDto(
        String cpf,
        String senha,
         String nome,
        String email1,
         String email2,
        String tel1,
         String tel2,
        String sexo,
         String cep,
        String bairro,
         String rua,
        String num,
         String comp,
        java.time.LocalDate datanasc
) {}