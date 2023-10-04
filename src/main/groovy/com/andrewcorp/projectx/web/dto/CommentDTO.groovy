package com.andrewcorp.projectx.web.dto


import java.time.LocalDateTime

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
record CommentDTO(
        String id,
        String postId,
        String username,
        String commentContent,
        LocalDateTime timestamp) implements Serializable {
}