package com.andrewcorp.projectx.persistence.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

import java.time.LocalDateTime

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
@Document(collection = "comments")
class Comment {
    @Id
    private Long id
    private Long postId
    private Long userId
    private String content
    private LocalDateTime timestamp

    Long getId() {
        return id
    }

    void setId(Long id) {
        this.id = id
    }

    Long getUserId() {
        return userId
    }

    void setUserId(Long userId) {
        this.userId = userId
    }

    String getContent() {
        return content
    }

    void setContent(String content) {
        this.content = content
    }

    Long getPostId() {
        return postId
    }

    void setPostId(Long postId) {
        this.postId = postId
    }

    LocalDateTime getTimestamp() {
        return timestamp
    }

    void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp
    }
}
