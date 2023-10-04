package com.andrewcorp.projectx

import com.andrewcorp.projectx.persistence.model.Post
import com.andrewcorp.projectx.persistence.model.User
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
class PostControllerCommentSpec extends BasicTest {

    String postId
    String commenterId
    String commenterToken

    void setup() {
        postId = postRepository.save(new Post(userId: userId, postContent: "This is a test post")).id
        commenterId = userRepository.save(new User(username: "testcommenter", password: passwordEncoder.encode("testpassword"))).id
        commenterToken = jwtProvider.generateToken("testcommenter", commenterId)
    }

    def "Commenting on a post should return HTTP status 200 (OK)"() {
        given:
        def postId = postId // Replace with a valid post ID
        def commentContent = "This is a test comment."

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/posts/${postId}/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer ${commenterToken}")
                .content("{\"commentContent\": \"${commentContent}\"}")
        )

        then:
        result.andExpect(status().isOk())
    }

    def "Commenting on a non-existent post should return HTTP status 404 (Not Found)"() {
        given:
        def postId = UUID.randomUUID().toString()
        def commentContent = "This is a test comment."

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/posts/${postId}/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer ${commenterToken}")
                .content("{\"commentContent\": \"${commentContent}\"}")
        )

        then:
        result.andExpect(status().isNotFound())
    }

    def "Commenting on a post with invalid content length should return HTTP status 400 (Bad Request)"() {
        given:
        def postId = postId
        def commentContent = "Sh" // Content length is less than 3 characters

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/posts/${postId}/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer ${commenterToken}")
                .content("{\"commentContent\": \"${commentContent}\"}")
        )

        then:
        result.andExpect(status().isBadRequest())
    }

    //get comments for post
    def "Getting comments for a post should return HTTP status 200 (OK)"() {
        given:
        def postId = postId
        postService.createCommentForPost(postId, userId, "This is a test comment.")

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/posts/${postId}/comments")
                .header("Authorization", "Bearer ${commenterToken}")
        )

        then:
        result.andExpect(status().isOk())
    }
}

