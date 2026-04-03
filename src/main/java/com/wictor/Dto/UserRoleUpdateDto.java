package com.wictor.Dto;

import com.wictor.enums.Role;
import com.wictor.enums.Tipo;

public record UserRoleUpdateDto(
        Role role,
        Tipo tipo
) {}
