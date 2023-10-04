package com.andrewcorp.projectx.web.dto


import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
class UserPayload implements Serializable {
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 20)
    private String username
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 40)
    private String password

    UserPayload() {
    }

    String getUsername() {
        return username
    }

    String getPassword() {
        return password
    }
}
