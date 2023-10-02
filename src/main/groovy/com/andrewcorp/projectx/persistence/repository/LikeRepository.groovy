package com.andrewcorp.projectx.persistence.repository;

import com.andrewcorp.projectx.persistence.model.Like
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author Andrew
 * @since 02.10.2023
 */
interface LikeRepository extends MongoRepository<Like, Long> {
    List<Like> findAllByPostId(Long postId);

    List<Long> findAllByUserIdIn(List<Long> userIds);
}
