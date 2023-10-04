package com.andrewcorp.projectx.persistence.repository

import com.andrewcorp.projectx.persistence.model.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
/**
 *
 * @author Andrew
 * @since 02.10.2023
 */

interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findAllByUserId(String userId)

    Page<Post> findAllByUserIdIn(List<String> userIds, Pageable pageable)
}
