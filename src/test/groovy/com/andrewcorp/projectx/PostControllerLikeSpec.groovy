package com.andrewcorp.projectx

import com.andrewcorp.projectx.persistence.model.Like
import com.andrewcorp.projectx.persistence.model.Post
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerLikeSpec extends BasicTest {

    String postId

    void setup() {
        postId = postRepository.save(new Post(userId: userId, postContent: "This is a test post")).id
    }

    def "Liking a post should return HTTP status 200 (OK)"() {
        given:
        def postId = postId

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/posts/${postId}/like")
                .header("Authorization", "Bearer ${token}")
                .contentType(MediaType.APPLICATION_JSON)
        )

        then:
        result.andExpect(status().isOk())
    }

    def "Unliking a post should return HTTP status 200 (OK)"() {
        given:
        def postId = postId
        likeRepository.save(new Like(postId: postId, userId: userId, username: userRepository.findById(userId).get().username))

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/posts/${postId}/unlike")
                .header("Authorization", "Bearer ${token}")
                .contentType(MediaType.APPLICATION_JSON)
        )

        then:
        result.andExpect(status().isOk())
    }

    def "Liking a non-existent post should return HTTP status 404 (Not Found)"() {
        given:
        def postId = UUID.randomUUID().toString()

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/posts/${postId}/like")
                .header("Authorization", "Bearer ${token}")
                .contentType(MediaType.APPLICATION_JSON)
        )

        then:
        result.andExpect(status().isNotFound())
    }

    def "Unliking a non-existent post should return HTTP status 404 (Not Found)"() {
        given:
        def postId = UUID.randomUUID().toString()

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/posts/${postId}/unlike")
                .header("Authorization", "Bearer ${token}")
                .contentType(MediaType.APPLICATION_JSON)
        )

        then:
        result.andExpect(status().isNotFound())
    }
}

