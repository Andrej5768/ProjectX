package com.andrewcorp.projectx

import com.andrewcorp.projectx.persistence.model.User
import com.andrewcorp.projectx.web.dto.UserResponse
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
/**
 *
 * @author Andrew
 * @since 04.10.2023
 */

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerLogoutSpec extends BasicTest {

    def "User can successfully log out"() {
        given:
        def username = "test2user"
        def password = "test2password"
        def id = userRepository.save(new User(username: username, password: passwordEncoder.encode(password))).id
        String token = jwtProvider.generateToken(username, id)
        jwtProvider.validateToken(_) >> true
        jwtProvider.getUserIdFromToken(_) >> id

        when:
        def result = mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"${username}\", \"password\":\"${password}\"}"))
                .andExpect(status().isOk())

        mockMvc.perform(post("/api/users/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer ${token}"))
                .andExpect(status().isOk())

        then:
        SecurityContextHolder.getContext().authentication == null
    }
}
