package com.andrewcorp.projectx.web.dto


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
class PostDTO implements Serializable {
    @Nullable
    String id
    @NotNull(message = "User ID cannot be null")
    @NotBlank(message = "User ID cannot be blank")
    String userId
    @NotBlank(message = "Post content cannot be blank")
    @NotNull(message = "Post content cannot be null")
    @Size(min = 10, max = 2048)
    String postContent
    @Nullable
    LocalDateTime timestamp
    @Nullable
    int likes = 0
    @Nullable
    List<String> likedBy = []
    @Nullable
    List<CommentDTO> comments = []

    PostDTO() {
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

    String getPostContent() {
        return postContent
    }

    void setPostContent(String content) {
        this.postContent = content
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
