package com.andrewcorp.projectx.persistence.repository;

import com.andrewcorp.projectx.persistence.model.Like
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author Andrew
 * @since 02.10.2023
 */
interface LikeRepository extends MongoRepository<Like, Long> {
    List<Like> findAllByPostId(Long postId);

    Like findByPostIdAndUserId(Long postId, Long userId);

    void deleteAllByPostId(Long postId);
}
