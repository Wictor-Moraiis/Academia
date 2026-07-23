package com.wictor.dto.user;

import com.wictor.enums.Sexo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

@Schema(description = "Dados para atualização de um usuário")
public record UserUpdateDto(

        @Schema(description = "Novo CPF do usuário", example = "12345678901")
        @Pattern(regexp = "\\d{11}|\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve possuir 11 dígitos")
        String cpf,

        @Schema(description = "Nova senha do usuário", example = "NovaSenha123")
        @Pattern(regexp = "\\d{6,}", message = "Senha deve possuir no mínimo 6 dígitos")
        String senha,

        @Schema(description = "Novo nome completo", example = "João da Silva")
        String nome,

        @Schema(description = "Novo e-mail principal", example = "joao@email.com")
        @Email(message = "Email primário inválido")
        String email1,

        @Schema(description = "Novo e-mail secundário", example = "joao2@email.com")
        @Email(message = "Email segundário inválido")
        String email2,

        @Schema(description = "Novo telefone principal", example= "11987654321")
        @Pattern(regexp = "\\d{2}9\\d{8}|\\(\\d{2}\\)\\s?9\\d{4}-\\d{4}",
                message = "Telefone deve estar no formato 11987654321 ou (11) 98765-4321")
        String tel1,

        @Schema(description = "Novo telefone secundário", example = "11999998888")
        @Pattern(regexp = "\\d{2}9\\d{8}|\\(\\d{2}\\)\\s?9\\d{4}-\\d{4}",
                message = "Telefone deve estar no formato 11987654321 ou (11) 98765-4321")
        String tel2,

        @Schema(description = "Sexo do usuário", example = "MASCULINO")
        Sexo sexo,

        @Schema(description = "Novo CEP", example = "05890-000")
        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP inválido")
        String cep,

        @Schema(description = "Novo bairro", example = "Jardim São Luís")
        String bairro,

        @Schema(description = "Nova rua", example = "Rua das Flores")
        String rua,

        @Schema(description = "Novo número da residência", example = "150")
        Integer numeroCasa,

        @Schema(description = "Novo complemento", example = "Apartamento 12")
        String comp,

        @Schema(description = "Nova data de nascimento", example = "2005-06-15")
        LocalDate datanasc
){}