package com.andrewcorp.projectx.web.dto

import groovy.transform.CompileStatic
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
class UserRegisterRequest implements Serializable {
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 20)
    String username
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 40)
    String password
    @NotBlank(message = "Full name cannot be blank")
    String fullName

    UserRegisterRequest() {
    }

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
