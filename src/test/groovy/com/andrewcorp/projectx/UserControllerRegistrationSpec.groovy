package com.andrewcorp.projectx


import com.andrewcorp.projectx.service.UserService
import com.andrewcorp.projectx.web.dto.UserDTO
import com.andrewcorp.projectx.web.dto.UserRegisterRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.AutoCleanup
import spock.lang.Specification
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import spock.lang.Subject
/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerRegistrationSpec extends BasicTest {

    def "Registering a new user should return HTTP status 201 (Created)"() {
        given:
        def newUser = [
                "username": "testusernonexist",
                "password": "testpassword",
                "fullName": "Test User"
        ]

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newUser))
        )

        then:
        result.andExpect(status().isCreated())
    }

    def "Registering a user with an existing username should return HTTP status 409 (Conflict)"() {
        given:
        def existingUser = [
                "username": "existinguser",
                "password": "existingpassword",
                "fullName": "Existing User"
        ]

        when:
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(existingUser))
        )

        and:
        def duplicateUser = [
                "username": "existinguser",
                "password": "newpassword",
                "fullName": "New Password User"
        ]

        def result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(duplicateUser))
        )

        then:
        result.andExpect(status().isConflict())
    }
}
