package com.wictor.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wictor.enums.Role;
import com.wictor.enums.Sexo;
import com.wictor.enums.Tipo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserDto(
        @NotBlank(message = "Cpf é obrigatório")
        @Pattern(regexp = "\\d{11}|\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}",
                message = "CPF deve possuir 11 dígitos")
        String cpf,
        @Pattern(regexp = "\\d{6,}",
                message = "Senha deve possuir no mínimo 6 dígitos")
        String senha,
        @NotBlank(message = "Nome é obrigatório")
         String nome,
        @Email(message = "Email primário inválido")
        @NotBlank(message = "Email é obrigatório")
        String email1,
        @Email(message = "Email segundário inválido")
         String email2,
        @NotBlank(message = "Telefone é obrigatório")
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
        @NotNull(message = "Sexo é obrigatório")
        Sexo sexo,
        @NotBlank(message = "Cep é obrigatório")
        @Pattern(
                regexp = "\\d{5}-?\\d{3}",
                message = "CEP inválido"
        )
         String cep,
        @NotBlank(message = "Bairro é obrigatório")
        String bairro,
        @NotBlank(message = "Rua é obrigatória")
         String rua,
        @NotNull(message = "Número de residência é obrigatório")
        Integer numeroCasa,
         String comp,
        @JsonFormat(pattern = "dd/MM/yyyy")
        @NotNull(message = "Data de nascimento é obrigatória")
        java.time.LocalDate datanasc,
        Role role,
        Tipo tipo
) {}