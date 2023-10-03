package com.andrewcorp.projectx.persistence.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
/**
 *
 * @author Andrew
 * @since 02.10.2023
 */

@Document(collection = "likes")
class Like {
    @Id
    private Long id
    private Long postId
    private Long userId
    private String username

    Like() {
    }

    Long getId() {
        return id
    }

    void setId(Long id) {
        this.id = id
    }

    Long getPostId() {
        return postId
    }

    void setPostId(Long postId) {
        this.postId = postId
    }

    Long getUserId() {
        return userId
    }

    void setUserId(Long userId) {
        this.userId = userId
    }

    String getUsername() {
        return username
    }

    void setUsername(String username) {
        this.username = username
    }
}
