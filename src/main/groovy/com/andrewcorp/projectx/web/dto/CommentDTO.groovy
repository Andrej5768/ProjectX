package com.andrewcorp.projectx.web.dto

import jakarta.annotation.Nullable

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
class CommentDTO {
    @Nullable
    private Long id
    @Nullable
    private String username
    private String content
    @Nullable
    private String timestamp

    Long getId() {
        return id
    }

    void setId(Long id) {
        this.id = id
    }

    @Nullable
    String getUsername() {
        return username
    }

    void setUsername(@Nullable String username) {
        this.username = username
    }

    String getContent() {
        return content
    }

    void setContent(String content) {
        this.content = content
    }

    String getTimestamp() {
        return timestamp
    }

    void setTimestamp(String timestamp) {
        this.timestamp = timestamp
    }
}
