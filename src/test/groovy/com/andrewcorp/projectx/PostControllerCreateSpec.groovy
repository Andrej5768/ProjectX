package com.andrewcorp.projectx

import com.andrewcorp.projectx.web.dto.PostDTO
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
@SpringBootTest
@AutoConfigureMockMvc
class PostControllerCreateSpec extends BasicTest {

    void "Create a new post"() {
        given:
        def postDTO = new PostDTO(userId: userId, postContent: "This is a test post")

        when:
        def result = mockMvc.perform(post("/api/posts/create")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $token")
                .content(asJsonString(postDTO)))
                .andExpect(status().isCreated())

        then:
        result.andExpect(status().is2xxSuccessful())
    }

    void "Fail to create a new post with invalid content"() {
        given:
        def postDTO = new PostDTO(userId: userId, postContent: "")

        when:
        def result = mockMvc.perform(post("/api/posts/create")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $token")
                .content(asJsonString(postDTO)))
                .andExpect(status().isBadRequest())

        then:
        result.andExpect(status().isBadRequest())
    }

    void "Fail to create a new post with invalid user id"() {
        given:
        def postDTO = new PostDTO(userId: UUID.randomUUID().toString(), postContent: "")

        when:
        def result = mockMvc.perform(post("/api/posts/create")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $token")
                .content(asJsonString(postDTO)))
                .andExpect(status().isBadRequest())

        then:
        result.andExpect(status().isBadRequest())
    }
}
