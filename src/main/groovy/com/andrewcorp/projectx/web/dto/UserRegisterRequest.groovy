package com.andrewcorp.projectx.web.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
class UserRegisterRequest {
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 20)
    private String username
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 40)
    private String password
    @NotBlank(message = "Full name cannot be blank")
    private String fullName

    String getUsername() {
        return username
    }

    void setUsername(String username) {
        this.username = username
    }

    String getPassword() {
        return password
    }

    void setPassword(String password) {
        this.password = password
    }

    String getFullName() {
        return fullName
    }

    void setFullName(String fullName) {
        this.fullName = fullName
    }
}
