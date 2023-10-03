package com.andrewcorp.projectx.web.dto

import groovy.transform.CompileStatic
import jakarta.annotation.Nullable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

import java.time.LocalDateTime

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
class PostDTO {
    Long id
    Long userId
    @NotBlank
    @NotNull
    @Size(min = 10, max = 2048)
    String content
    LocalDateTime timestamp
    int likes = 0
    List<String> likedBy = []
    List<CommentDTO> comments = []

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

    LocalDateTime getTimestamp() {
        return timestamp
    }

    void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp
    }

    int getLikes() {
        return likes
    }

    void setLikes(int likes) {
        this.likes = likes
    }

    List<String> getLikedBy() {
        return likedBy
    }

    void setLikedBy(List<String> likedBy) {
        this.likedBy = likedBy
    }

    List<CommentDTO> getComments() {
        return comments
    }

    void setComments(List<CommentDTO> comments) {
        this.comments = comments
    }
}
