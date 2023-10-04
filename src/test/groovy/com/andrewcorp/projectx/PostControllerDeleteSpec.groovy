package com.andrewcorp.projectx

import com.andrewcorp.projectx.persistence.model.Post
import spock.lang.Specification
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerDeleteSpec extends BasicTest {

    String postId

    void setup() {
        postId = postRepository.save(new Post(userId: userId, postContent: "This is a test post")).id
    }

    def "Deleting an existing post should return HTTP status 204 (No Content)"() {
        given:
        def postId = postId

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/posts/${postId}/delete")
                .header("Authorization", "Bearer ${token}")
                .contentType(MediaType.APPLICATION_JSON)
        )

        then:
        result.andExpect(status().isNoContent())
    }

    def "Deleting a non-existing post should return HTTP status 404 (Not Found)"() {
        given:
        def postId = UUID.randomUUID().toString() // Replace with a non-existing post ID

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/posts/${postId}/delete")
                .header("Authorization", "Bearer ${token}")
                .contentType(MediaType.APPLICATION_JSON)
        )

        then:
        result.andExpect(status().isNotFound())
    }
}

