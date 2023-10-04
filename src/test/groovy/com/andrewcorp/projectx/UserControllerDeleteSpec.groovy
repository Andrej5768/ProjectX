package com.andrewcorp.projectx

import com.andrewcorp.projectx.persistence.model.User
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

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerDeleteSpec extends BasicTest {

    String userIdDelete

    void setup() {
        userIdDelete = userRepository.save(new User(username: "testusertodelete", password: "testpassword", fullName: "Test User")).id
    }

    def "Deleting an existing user should return HTTP status 204 (No Content)"() {
        given:
        def userId = userIdDelete

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/users/${userId}")
                .header("Authorization", "Bearer ${jwtProvider.generateToken("testusertodelete", userId)}")
                .contentType(MediaType.APPLICATION_JSON)
        )

        then:
        result.andExpect(status().isNoContent())
    }

    def "Deleting a non-existing user should return HTTP status 404 (Not Found)"() {
        given:
        def userId = UUID.randomUUID().toString()

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/users/${userId}")
                .header("Authorization", "Bearer ${jwtProvider.generateToken("testusertodelete", userId)}")
                .contentType(MediaType.APPLICATION_JSON)
        )

        then:
        result.andExpect(status().isNotFound())
    }
}