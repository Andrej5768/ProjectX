package com.andrewcorp.projectx

import com.andrewcorp.projectx.persistence.model.Comment
import com.andrewcorp.projectx.persistence.model.Like
import com.andrewcorp.projectx.persistence.model.Post
import com.andrewcorp.projectx.persistence.model.User
import com.andrewcorp.projectx.service.FeedService
import com.andrewcorp.projectx.web.controller.FeedController
import com.andrewcorp.projectx.web.dto.PagedFeedDTO
import com.andrewcorp.projectx.web.error.UserNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import java.time.LocalDateTime


/**
 *
 * @author Andrew
 * @since 04.10.2023
 */
@SpringBootTest
@AutoConfigureMockMvc
class FeedControllerSpec extends BasicTest {

    @Autowired
    FeedService feedService

    def "getUserStream should return empty PagedFeedDTO when user exists"() {
        given:
        def page = 0
        def pageSize = 10

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/feed")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer ${token}")
                .param("page", page.toString())
                .param("pageSize", pageSize.toString())
        )

        then:
        result.andExpect(status().isOk())
    }

    def "getUserStream should return PagedFeedDTO when user exists"() {
        given:
        def page = 0
        def pageSize = 10
        initFollowers(userId)

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/feed")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer ${token}")
                .param("page", page.toString())
                .param("pageSize", pageSize.toString())
        )

        then:
        result.andExpect(status().isOk())
    }

    def "getUserStream should return HTTP status 404 (Not Found) when user does not exist"() {
        given:
        def page = 0
        def pageSize = 10
        def invalidUserId = UUID.randomUUID().toString()

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/feed/${invalidUserId}")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer ${token}")
                .param("page", page.toString())
                .param("pageSize", pageSize.toString())
        )

        then:
        result.andExpect(status().isNotFound())
    }

    void initFollowers(String userId) {
        for (int i = 0; i < 10; i++) {
            def follower = new User(username: "testfollower${i}", password: passwordEncoder.encode("testpassword"))
            follower = userRepository.save(follower)
            initPosts(follower.id)
            followService.followUser(userId, follower.id)
        }
    }

    void initPosts(String userId) {
        for (int i = 0; i < 10; i++) {
            def post = new Post(userId: userId, postContent: "This is a test post ${i}")
            post = postRepository.save(post)
            def comment = new Comment(userId: userId, postId: post.id, username: userRepository.findById(userId).get().username, commentContent: "This is a test comment ${i}", timestamp: LocalDateTime.now())
            commentRepository.save(comment)
            def like = new Like(userId: userId, postId: post.id, timestamp: LocalDateTime.now())
            likeRepository.save(like)
        }
    }

}
