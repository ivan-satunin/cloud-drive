package com.me.clouddrive.controller.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserPayload(
        @NotBlank(message = "The username must be non-empty")
        @NotNull
        String username,
        @NotBlank(message = "The email must be non-empty")
        @NotNull
        String email,
        @NotNull
        @NotBlank(message = "The password must be non-empty")
        String password
) {
}
