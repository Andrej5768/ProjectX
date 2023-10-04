package com.andrewcorp.projectx.persistence.repository

import com.andrewcorp.projectx.persistence.model.Comment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findAllByPostId(String postId)

    Page<Comment> findAllByUserIdIn(List<String> postIds, Pageable pageable)
}
