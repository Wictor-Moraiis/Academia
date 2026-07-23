package com.wictor.dto.user;

import com.wictor.enums.Role;
import com.wictor.enums.Sexo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Dados retornados de um usuário")
public record UserResponseDto(

        @Schema(description = "Identificador do usuário", example = "1")
        Integer id,

        @Schema(description = "Nome completo", example = "João Silva")
        String nome,

        @Schema(description = "E-mail principal", example = "joao@email.com")
        String email1,

        @Schema(description = "E-mail secundário", example = "joao2@email.com")
        String email2,

        @Schema(description = "Telefone principal", example = "11987654321")
        String tel1,

        @Schema(description = "Telefone secundário", example = "11999998888")
        String tel2,

        @Schema(description = "Sexo", example = "MASCULINO")
        Sexo sexo,

        @Schema(description = "CEP", example = "05890-000")
        String cep,

        @Schema(description = "Bairro", example = "Jardim São Luís")
        String bairro,

        @Schema(description = "Rua", example = "Rua das Flores")
        String rua,

        @Schema(description = "Número da residência", example = "150")
        Integer numeroCasa,

        @Schema(description = "Complemento", example = "Apartamento 12")
        String comp,

        @Schema(description = "Data de nascimento", example = "2005-06-15")
        LocalDate datanasc,

        @Schema(description = "Perfil de acesso", example = "ALUNO")
        Role role
){}
