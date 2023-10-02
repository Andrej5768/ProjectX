package com.andrewcorp.projectx.persistence.repository

import com.andrewcorp.projectx.persistence.model.Comment
import org.springframework.data.mongodb.repository.MongoRepository

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
interface CommentRepository extends MongoRepository<Comment, Long> {
}
