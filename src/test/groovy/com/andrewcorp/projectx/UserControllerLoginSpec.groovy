package com.andrewcorp.projectx


import com.andrewcorp.projectx.web.dto.UserPayload
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
// An integration test for user login functionality
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerLoginSpec extends BasicTest {


    def "Logging in with valid credentials should return HTTP status 200 (OK)"() {
        given:
        def validUsername = "testuser"
        def validPassword = "testpassword"

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new UserPayload(username: validUsername, password: validPassword)))
        )

        then:
        result.andExpect(status().isOk())
    }

    def "Logging in with invalid credentials should return HTTP status 401 (Unauthorized)"() {
        given:
        def invalidUsername = "invaliduser"
        def invalidPassword = "invalidpassword"

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"${invalidUsername}\", \"password\": \"${invalidPassword}\"}")
        )

        then:
        result.andExpect(status().isUnauthorized())
    }
}
