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
    def likeRepository

    Like likePost(Post post, Long userId, String username) {
        log.info("Liking post with id {} for user with id {}", post.id, userId)
        def like = new Like(postId: post.id, userId: userId, username: username)
        likeRepository.save(like)
    }

    void unlikePost(Long postId, Long userId) {
        log.info("Unliking post with id {} for user with id {}", postId, userId)
        def like = likeRepository.findByPostIdAndUserId(postId, userId)
        if (!like) {
            throw new LikeNotFoundException("Like with postId ${postId} and userId ${userId} not found")
        }
        likeRepository.delete(like)
    }

    List<Like> getLikesForPost(Long postId) {
        log.info("Getting likes for post with id {}", postId)
        likeRepository.findAllByPostId(postId)
    }

    void deleteLikesByPost(Long postId) {
        log.info("Deleting likes for post with id {}", postId)
        likeRepository.deleteAllByPostId(postId)
    }
}
