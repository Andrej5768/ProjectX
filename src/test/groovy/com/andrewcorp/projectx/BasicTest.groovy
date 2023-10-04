package com.andrewcorp.projectx


import com.andrewcorp.projectx.persistence.model.User
import com.andrewcorp.projectx.persistence.repository.CommentRepository
import com.andrewcorp.projectx.persistence.repository.LikeRepository
import com.andrewcorp.projectx.persistence.repository.PostRepository
import com.andrewcorp.projectx.persistence.repository.UserRepository
import com.andrewcorp.projectx.security.JwtProvider
import com.andrewcorp.projectx.service.FollowService
import com.andrewcorp.projectx.service.PostService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

/**
 *
 * @author Andrew
 * @since 03.10.2023
 */
@SpringBootTest
@AutoConfigureMockMvc
class BasicTest extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    PostService postService

    @Autowired
    PostRepository postRepository

    @Autowired
    UserRepository userRepository

    @Autowired
    CommentRepository commentRepository

    @Autowired
    PasswordEncoder passwordEncoder

    @Autowired
    LikeRepository likeRepository

    @Autowired
    FollowService followService

    @Autowired
    JwtProvider jwtProvider

    String token
    String userId

    void setup() {
        userRepository.deleteAll()
        userId = userRepository.save(new User(username: "testuser", password: passwordEncoder.encode("testpassword"))).id
        token = jwtProvider.generateToken("testuser", userId)
    }

    // Helper method to convert objects to JSON strings
    String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj)
        } catch (Exception e) {
            throw new RuntimeException(e)
        }
    }
}
