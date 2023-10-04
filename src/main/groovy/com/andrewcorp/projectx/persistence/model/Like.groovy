package com.andrewcorp.projectx.persistence.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

import java.time.LocalDateTime

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */

@Document(collection = "likes")
class Like {
    @Id
    private String id
    private String postId
    private String userId
    private String username
    private LocalDateTime timestamp

    Like() {
    }

    String getId() {
        return id
    }

    void setId(String id) {
        this.id = id
    }

    String getPostId() {
        return postId
    }

    void setPostId(String postId) {
        this.postId = postId
    }

    String getUserId() {
        return userId
    }

    void setUserId(String userId) {
        this.userId = userId
    }

    String getUsername() {
        return username
    }

    void setUsername(String username) {
        this.username = username
    }

    LocalDateTime getTimestamp() {
        return timestamp
    }

    void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp
    }
}
