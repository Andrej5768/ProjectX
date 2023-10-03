package com.andrewcorp.projectx.web.dto

import groovy.transform.CompileStatic
import jakarta.annotation.Nullable

import java.time.LocalDateTime

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
@CompileStatic
class CommentDTO {
    private Long id
    private String username
    private String content
    private LocalDateTime timestamp

    Long getId() {
        return id
    }

    void setId(Long id) {
        this.id = id
    }

    String getUsername() {
        return username
    }

    void setUsername(String username) {
        this.username = username
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
}
