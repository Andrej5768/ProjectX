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
    private String id
    private String postId
    private String userId
    private String username
    private String commentContent
    private LocalDateTime timestamp

    Comment() {
    }

    String getId() {
        return id
    }

    void setId(String id) {
        this.id = id
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

    String getCommentContent() {
        return commentContent
    }

    void setCommentContent(String commentContent) {
        this.commentContent = commentContent
    }

    String getPostId() {
        return postId
    }

    void setPostId(String postId) {
        this.postId = postId
    }

    LocalDateTime getTimestamp() {
        return timestamp
    }

    void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp
    }
}
