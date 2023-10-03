package com.andrewcorp.projectx.persistence.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

import java.time.LocalDateTime

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
@Document(collection = "posts")
class Post {
    @Id
    private Long id
    private Long userId
    private String postContent
    private LocalDateTime timestamp
    private List<Like> likes = []
    private List<Comment> comments = []

    Post() {
    }

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
        return postContent
    }

    void setContent(String content) {
        this.postContent = content
    }

    LocalDateTime getTimestamp() {
        return timestamp
    }

    void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp
    }

    List<Like> getLikes() {
        return likes
    }

    void setLikes(List<Like> likes) {
        this.likes = likes
    }

    List<Comment> getComments() {
        return comments
    }

    void setComments(List<Comment> comments) {
        this.comments = comments
    }
}
