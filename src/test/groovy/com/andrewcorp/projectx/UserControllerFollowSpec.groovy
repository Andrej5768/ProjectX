package com.andrewcorp.projectx

import com.andrewcorp.projectx.persistence.model.User
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerFollowSpec extends BasicTest {

    String userFollowId
    String followerToken

    void setup() {
        userFollowId = userRepository.save(new User(username: "tesfollowtuser", password: passwordEncoder.encode("testpassword"))).id
        followerToken = jwtProvider.generateToken("tesfollowtuser", userFollowId)
    }

    def "Subscribing to another user should return HTTP status 200 (OK)"() {
        given:
        def targetUserId = userId

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/subscriptions/subscribe")
                .header("Authorization", "Bearer ${followerToken}")
                .param("targetUserId", targetUserId.toString())
        )

        then:
        result.andExpect(status().isOk())
    }

    def "Subscribing to invalid user should return HTTP status 404"() {
        given:
        def targetUserId = UUID.randomUUID().toString()

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/subscriptions/subscribe")
                .header("Authorization", "Bearer ${followerToken}")
                .param("targetUserId", targetUserId.toString())
        )

        then:
        result.andExpect(status().isNotFound())
    }

    def "Subscribing to another with subscriptions user should return HTTP status 409"() {
        given:
        def targetUserId = userId
        followService.followUser(userFollowId, targetUserId)

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/subscriptions/subscribe")
                .header("Authorization", "Bearer ${followerToken}")
                .param("targetUserId", targetUserId.toString())
        )

        then:
        result.andExpect(status().isConflict())
    }

    def "Subscribing to another user should return HTTP status 200 (OK)"() {
        given:
        def targetUserId = userId

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/subscriptions/subscribe")
                .header("Authorization", "Bearer ${followerToken}")
                .param("targetUserId", targetUserId.toString())
        )

        then:
        result.andExpect(status().isOk())
    }

    def "Unsubscribing from another user should return HTTP status 200 (OK)"() {
        given:
        def targetUserId = userId
        followService.followUser(userFollowId, targetUserId)

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/subscriptions/unsubscribe")
                .header("Authorization", "Bearer ${followerToken}")
                .param("targetUserId", targetUserId.toString())
        )

        then:
        result.andExpect(status().isOk())
    }

    def "Unsubscribing from another invalid user should return HTTP status 404"() {
        given:
        def targetUserId = UUID.randomUUID().toString()

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/subscriptions/unsubscribe")
                .header("Authorization", "Bearer ${token}")
                .param("targetUserId", targetUserId.toString())
        )

        then:
        result.andExpect(status().isNotFound())
    }

    def "Unsubscribing from another user without subscriptions should return HTTP status 409"() {
        given:
        def targetUserId = userId

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/subscriptions/unsubscribe")
                .header("Authorization", "Bearer ${token}")
                .param("targetUserId", targetUserId.toString())
        )

        then:
        result.andExpect(status().isBadRequest())
    }
}
