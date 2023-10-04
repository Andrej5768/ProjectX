package com.andrewcorp.projectx.service

import com.andrewcorp.projectx.persistence.model.Like
import com.andrewcorp.projectx.persistence.model.Post
import com.andrewcorp.projectx.persistence.repository.LikeRepository
import com.andrewcorp.projectx.web.error.LikeNotFoundException
import groovy.util.logging.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
@Slf4j
@Service
class LikeService {
    private final LikeRepository likeRepository

    LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository
    }

    Like likePost(Post post, String userId, String username) {
        log.info("Liking post with id {} for user with id {}", post.id, userId)
        def like = new Like(postId: post.id, userId: userId, username: username, timestamp: LocalDateTime.now())
        likeRepository.save(like)
    }

    void unlikePost(String postId, String userId) {
        log.info("Unliking post with id {} for user with id {}", postId, userId)
        def like = likeRepository.findByPostIdAndUserId(postId, userId)
        if (!like) {
            log.error("Like with postId {} and userId {} not found", postId, userId)
            throw new LikeNotFoundException("Like with postId ${postId} and userId ${userId} not found")
        }
        likeRepository.delete(like)
    }

    List<Like> getLikesForPost(String postId) {
        log.info("Getting likes for post with id {}", postId)
        likeRepository.findAllByPostId(postId)
    }

    void deleteLikesByPost(String postId) {
        log.info("Deleting likes for post with id {}", postId)
        likeRepository.deleteAllByPostId(postId)
    }
}
