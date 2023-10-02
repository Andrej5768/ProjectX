package com.andrewcorp.projectx.persistence.repository

import com.andrewcorp.projectx.persistence.model.Post
import org.springframework.data.mongodb.repository.MongoRepository
/**
 *
 * @author Andrew
 * @since 02.10.2023
 */

interface PostRepository extends MongoRepository<Post, Long> {
    List<Post> findAllByUserId(Long userId)

    List<Post> findAllByUserIdIn(List<Long> userIds)
}
