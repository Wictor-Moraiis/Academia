package com.wictor.dto.user;

import com.wictor.enums.Role;
import com.wictor.enums.Tipo;

public record UserRoleUpdateDto(
        Role role,
        Tipo tipo
) {}
