package com.andrewcorp.projectx


import com.andrewcorp.projectx.web.dto.UserDTO
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
class UserControllerUpdateSpec extends BasicTest {


    def "should update user profile with valid data"() {
        given:
        UserDTO updatedUserDto = new UserDTO(
                id: userId,
                username: "UpdatedUsername",
                fullName: "Updated Full Name",
                followers: [],
                following: []
        )

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/users/${userId}")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer ${token}")
                .content(asJsonString(updatedUserDto))
        )

        then:
        result.andExpect(status().isOk())
    }

    def "should handle user not authorize when updating profile (403)"() {
        given:
        UserDTO updatedUserDto = new UserDTO(
                id: userId,
                username: "UpdatedUsername",
                fullName: "Updated Full Name",
                followers: [],
                following: []
        )

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/users/${UUID.randomUUID().toString()}")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer ${token}")
                .content(asJsonString(updatedUserDto))
        )

        then:
        result.andExpect(status().isForbidden())
    }

    def "should handle invalid user data when updating profile"() {
        given:
        UserDTO updatedUserDto = new UserDTO(
                id: userId,
                username: "",
                fullName: "Updated Full Name",
                followers: [],
                following: []
        )

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/users/${userId}")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer ${token}")
                .content(asJsonString(updatedUserDto))
        )

        then:
        result.andExpect(status().isBadRequest())
    }
}
