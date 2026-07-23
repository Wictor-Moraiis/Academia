package com.wictor.dto.user;

import com.wictor.enums.Sexo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

@Schema(description = "Dados necessários para cadastrar um usuário")
public record UserDto(

        @Schema(description = "CPF do usuário", example = "12345678901")
        @NotBlank(message = "Cpf é obrigatório")
        @Pattern(regexp = "\\d{11}|\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}",
                message = "CPF deve possuir 11 dígitos")
        String cpf,

        @Schema(description = "Senha do usuário", example = "MinhaSenha123")
        @Pattern(
                regexp = "\\d{6,}",
                message = "Senha deve possuir no mínimo 6 dígitos")
        String senha,

        @Schema(description = "Nome completo do usuário", example = "João Silva")
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @Schema(description = "E-mail principal", example = "joao@email.com")
        @Email(message = "Email primário inválido")
        @NotBlank(message = "Email é obrigatório")
        String email1,

        @Schema(description = "E-mail secundário", example= "joao2@email.com")
        @Email(message = "Email segundário inválido")
        String email2,

        @Schema(description = "Telefone principal", example = "11987654321")
        @NotBlank(message = "Telefone é obrigatório")
        @Pattern(regexp = "\\d{2}9\\d{8}|\\(\\d{2}\\)\\s?9\\d{4}-\\d{4}",
                message = "Telefone deve estar no formato 11987654321 ou (11) 98765-4321")
        String tel1,

        @Schema(description = "Telefone secundário", example = "11999998888")
        @Pattern(regexp = "\\d{2}9\\d{8}|\\(\\d{2}\\)\\s?9\\d{4}-\\d{4}",
                message = "Telefone deve estar no formato 11987654321 ou (11) 98765-4321")
        String tel2,

        @Schema(description = "Sexo do usuário", example = "MASCULINO")
        @NotNull(message = "Sexo é obrigatório")
        Sexo sexo,

        @Schema(description = "CEP", example = "05890-000")
        @NotBlank(message = "Cep é obrigatório")
        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP inválido")
        String cep,

        @Schema(description = "Bairro", example = "Jardim São Luís")
        @NotBlank(message = "Bairro é obrigatório")
        String bairro,

        @Schema(description = "Rua", example = "Rua das Flores")
        @NotBlank(message = "Rua é obrigatória")
        String rua,

        @Schema(description = "Número da residência", example = "150")
        @NotNull(message = "Número de residência é obrigatório")
        Integer numeroCasa,

        @Schema(description = "Complemento", example = "Apartamento 12")
        String comp,

        @Schema(description = "Data de nascimento", example = "2005-06-15")
        @NotNull(message = "Data de nascimento é obrigatória")
        LocalDate datanasc
){}