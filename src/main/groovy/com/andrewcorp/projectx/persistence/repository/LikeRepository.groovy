package com.andrewcorp.projectx.persistence.repository;

import com.andrewcorp.projectx.persistence.model.Like
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author Andrew
 * @since 02.10.2023
 */
interface LikeRepository extends MongoRepository<Like, String> {
    List<Like> findAllByPostId(String postId);

    Like findByPostIdAndUserId(String postId, String userId);

    Page<Like> findAllByUserIdIn(List<String> userIds, Pageable pageable);

    void deleteAllByPostId(String postId);
}
