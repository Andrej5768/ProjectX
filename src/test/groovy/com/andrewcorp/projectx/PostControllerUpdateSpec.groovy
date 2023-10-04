package com.andrewcorp.projectx

import com.andrewcorp.projectx.persistence.model.Post
import com.andrewcorp.projectx.web.dto.PostDTO;
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
class PostControllerUpdateSpec extends BasicTest {

    String postId

    void setup() {
        postId = postRepository.save(new Post(userId: userId, postContent: "This is a test post")).id
    }

    def "Updating an existing post should return HTTP status 200 (OK)"() {
        given:
        def postDto = new PostDTO(
                id: postId,
                userId: userId,
                postContent: "Updated post content."
        )

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/posts/update")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer ${token}")
                .content(asJsonString(postDto))
        )

        then:
        result.andExpect(status().isOk())
    }

    def "Updating a non-existent post should return HTTP status 404 (Not Found)"() {
        given:
        def postDto = new PostDTO(
                id: UUID.randomUUID().toString(),
                userId: userId,
                postContent: "Updated post content."
        )

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/posts/update")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer ${token}")
                .content(asJsonString(postDto))
        )

        then:
        result.andExpect(status().isNotFound())
    }

    def "Updating a post with invalid content length should return HTTP status 400 (Bad Request)"() {
        given:
        def postDto = new PostDTO(
                id: postId,
                userId: userId,
                postContent: "Short"
        )

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/posts/update")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer ${token}")
                .content(asJsonString(postDto))
        )

        then:
        result.andExpect(status().isBadRequest())
    }

    def "Updating a post with invalid userId should return HTTP status 400 (Bad Request)"() {
        given:
        def postDto = new PostDTO(
                id: postId,
                userId: UUID.randomUUID().toString(),
                postContent: "Updated post content"
        )

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/posts/update")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer ${token}")
                .content(asJsonString(postDto))
        )

        then:
        result.andExpect(status().isForbidden())
    }
}
