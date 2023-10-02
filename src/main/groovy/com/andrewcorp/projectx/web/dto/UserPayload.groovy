package com.andrewcorp.projectx.web.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
class UserPayload {
    @NotBlank
    @Size(min = 3, max = 20)
    final String username
    @NotBlank
    @Size(min = 6, max = 40)
    final String password

    UserPayload(String username, String password) {
        this.username = username
        this.password = password
    }

    String getUsername() {
        return username
    }

    String getPassword() {
        return password
    }
}
