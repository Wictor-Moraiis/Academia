package com.wictor.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UserUpdateDto(
        @Pattern(regexp = "\\d{11}|\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}",
                message = "CPF deve possuir 11 dígitos")
        String cpf,
        @Pattern(regexp = "\\d{6,}",
                message = "Senha deve possuir no mínimo 6 dígitos")
        String senha,
        String nome,
        @Email(message = "Email primário inválido")
        String email1,
        @Email(message = "Email segundário inválido")
        String email2,
        @Pattern(
                regexp = "\\d{2}9\\d{8}|\\(\\d{2}\\)\\s?9\\d{4}-\\d{4}",
                message = "Telefone deve estar no formato 11987654321 ou (11) 98765-4321"
        )
        String tel1,
        @Pattern(
                regexp = "\\d{2}9\\d{8}|\\(\\d{2}\\)\\s?9\\d{4}-\\d{4}",
                message = "Telefone deve estar no formato 11987654321 ou (11) 98765-4321"
        )
        String tel2,
        String sexo,
        @Pattern(
                regexp = "\\d{5}-?\\d{3}",
                message = "CEP inválido"
        )
        String cep,
        String bairro,
        String rua,
        String num,
        String comp,
        @JsonFormat(pattern = "dd/MM/yyyy")
        java.time.LocalDate datanasc
) {}