package com.andrewcorp.projectx.web.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

/**
 *
 * @author Andrew
 * @since 04.10.2023
 */
class CommentRequest implements Serializable {
    @NotNull
    @NotBlank
    @Size(min = 3, max = 2048)
    String commentContent

    CommentRequest() {
    }

    String getCommentContent() {
        return commentContent
    }

    void setCommentContent(String commentContent) {
        this.commentContent = commentContent
    }
}