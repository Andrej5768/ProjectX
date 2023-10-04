package com.andrewcorp.projectx.web.dto


import java.time.LocalDateTime

/**
 * @author Andrew
 * @since 02.10.2023
 */
record LikeDTO(String id,
               String userId,
               String postId,
               LocalDateTime timestamp) implements Serializable {
}